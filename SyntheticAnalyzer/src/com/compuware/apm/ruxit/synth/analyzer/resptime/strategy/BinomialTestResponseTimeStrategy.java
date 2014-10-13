package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.DEBUG;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.DEFAULT_ANOMALY_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MAX_QUEUE_SIZE;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MAX_QUEUE_TIME_WINDOW;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MIN_EVALUATION_GAP;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MIN_SAMPLE_SIZE;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.OUT_OF_ORDER_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.SAMPLE_SIZE_STRATEGY_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.RESPONSE_TIME;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_TIME;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.BinomialTestUtil;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.SimpleParserUtil;

public class BinomialTestResponseTimeStrategy extends AbstractResponseTimeStrategy {

	private static final double ALERT_ALPHA = 0.05;
	private static final double DEALERT_ALPHA = 0.10;
	private static final double ALPHA_COOLDOWN = -0.005;
	
	private static final Attribute<Long> QUEUE_TUPLE_TIME = new Attribute<>(Long.class, "QUEUE_TUPLE_TIME");
	private static final Attribute<Boolean> QUEUE_TUPLE_BREACH = new Attribute<>(Boolean.class, "QUEUE_TUPLE_BREACH");
	private static final Attributes QUEUE_TUPLE_SCHEMA = Attributes.newAttributes()
			.withAttribute(QUEUE_TUPLE_TIME)
			.withAttribute(QUEUE_TUPLE_BREACH)
			.build();

	
	private long timeOfLastEval = 0;
	private int numBreaches = 0;
	private PriorityQueue<Tuple> queue = new PriorityQueue<>(this.config.get(MAX_QUEUE_SIZE) + 1, new QueueTupleComparator());	
    private Tuple mostRecentErrorTuple = null;
    private Tuple mostRecentNormalTuple = null;
    private double alpha = ALERT_ALPHA;

	public static Builder newBinomialTestResponseTimeStrategy (Attributes keyAttributes) {
    	return new Builder(keyAttributes);
    }
    
    private BinomialTestResponseTimeStrategy (Builder builder) {
    	super(builder);
    }
    
	@Override
	protected final void handleOnTupleReceived(Tuple tuple) {
		long tupleTime = tuple.get(TEST_TIME);
		if (tupleTime < getCurrentTime() - config.get(OUT_OF_ORDER_THRESHOLD)) {
			return; // the observation received is too stale, so we ignore it
		}

		Tuple queueTuple = newQueueTuple(tuple);
		addTupleToQueue(queueTuple);
		
		long testTime = tuple.get(TEST_TIME);
		if (testTime >= this.timeOfLastTuple) {
		    this.timeOfLastTuple = testTime;
		    if (queueTuple.get(QUEUE_TUPLE_BREACH)) {
		    	this.mostRecentErrorTuple = tuple;
		    } else {
		    	this.mostRecentNormalTuple = tuple;
		    }
		}

		
		adjustQueueBasedOnConstraints();
		
		if (isEligibleForEval()) {
			performEval();
		}
	}

	@Override
	protected final void handleOnClockTick(long time) {
		this.timeOfLastTick = time;
		boolean queueChanged = adjustQueueBasedOnConstraints();
		
		if (queueChanged && isEligibleForEval()) {
			performEval();
		}
	}

	private void performEval() {
		this.timeOfLastEval = getCurrentTime();
		if (status == Status.NORMAL && shouldAlert()) {
			generateEvent(AnalyzerEvent.Type.ALERT, this.timeOfLastTuple, getMostRecentErrorTuple());
		} else if (status == Status.ALERT && !shouldAlert()) {
			generateEvent(AnalyzerEvent.Type.RETURN_TO_NORMAL, this.timeOfLastTuple, getMostRecentNormalTuple());
		}
	}
	
	private boolean shouldAlert() {
		if (queue.size() > config.get(SAMPLE_SIZE_STRATEGY_THRESHOLD)) {
			double errorRate = getErrorRate();
			if (errorRate > config.get(DEFAULT_ANOMALY_THRESHOLD)) {
				return true;
			}
			return false;
		} else {
			 double p = BinomialTestUtil.getCumulativeProbability(queue.size(), numBreaches, config.get(DEFAULT_ANOMALY_THRESHOLD));
			boolean shouldAlert = p < this.alpha;
			updateAlpha(shouldAlert, p);
			// TODO: replace the following clause with logging statements
			if (config.get(DEBUG)) {
			   System.out.printf("time=%d, key=%s, numBreaches=%d, queueSize=%d, errorRate=%,.10f, p=%,.10f, shouldAlert=%s, alpha=%,.4f%n", getCurrentTime(), SimpleParserUtil.toString(this.key),numBreaches, queue.size(), getErrorRate(), p, String.valueOf(shouldAlert), this.alpha);
			}
			return shouldAlert;
		}		
	}

	private void updateAlpha(boolean shouldAlert, double p) {
		if (shouldAlert && this.status == Status.NORMAL) {
			this.alpha = DEALERT_ALPHA;
		} else if (!shouldAlert && this.status == Status.ALERT) {
			this.alpha = ALERT_ALPHA;
		} else if (p > ALERT_ALPHA && this.alpha > ALERT_ALPHA) {
			this.alpha += ALPHA_COOLDOWN; 
		}
	}

	private double getErrorRate() {
		return ((double) numBreaches) / queue.size();
	}

	private boolean isEligibleForEval () {
		return queue.size() >= config.get(MIN_SAMPLE_SIZE) && 
				getCurrentTime() - config.get(MIN_EVALUATION_GAP) >= this.timeOfLastEval;
	}
	
	
	private boolean adjustQueueBasedOnConstraints() {
		long currentTime = getCurrentTime();
		boolean queueChanged = false;
		
		// Remove records from the head of the queue if the
		// the queue is too big
		while (queue.size() > this.config.get(MAX_QUEUE_SIZE)) {
			queueChanged = true;
			removeTupleFromQueue();
		}
		
		// Age off any records from the head of the queue if they fall
		// outside the queue time window
		while (!queue.isEmpty() && queue.peek().get(QUEUE_TUPLE_TIME) <= currentTime
						- config.get(MAX_QUEUE_TIME_WINDOW)) {
			 queueChanged = true;
			 removeTupleFromQueue();
		}
		
		// If the queue becomes empty when it was full, or full when it was
		// empty, adjust the strategy state and notify listeners.
		adjustStrategyState();
		
		return queueChanged;
	}

	private void adjustStrategyState() {
		if (queue.isEmpty() && state == State.ACTIVE) {
			if (status == Status.ALERT) {
				generateEvent(AnalyzerEvent.Type.TIME_OUT, getCurrentTime(), TupleImpl.NULL);
			}
			state = State.IDLE;
			notifyStrategyListeners(this);
		} else if (!queue.isEmpty() && state == State.IDLE) {
			state = State.ACTIVE;
			notifyStrategyListeners(this);
		}
	}

	private Tuple getMostRecentNormalTuple() {
		return this.mostRecentNormalTuple;
	}

	private Tuple getMostRecentErrorTuple() {
		return this.mostRecentErrorTuple;
	}

	private void addTupleToQueue(Tuple queueTuple) {
		queue.add(queueTuple);
        if (queueTuple.get(QUEUE_TUPLE_BREACH)) {
        	numBreaches++;
        }
	}

	private void removeTupleFromQueue() {
		Tuple queueTuple = queue.poll();
        if (queueTuple.get(QUEUE_TUPLE_BREACH)) {
        	numBreaches--;
        }
        if (queue.isEmpty()) {
        	this.mostRecentErrorTuple = null;
        	this.mostRecentNormalTuple = null;
        }
	}

	private Tuple newQueueTuple (Tuple sourceTuple) {
		boolean breach = isThresholdBreach(sourceTuple);
		Tuple queueTuple = TupleImpl.newTuple(QUEUE_TUPLE_SCHEMA)
				.withValue(QUEUE_TUPLE_TIME, sourceTuple.get(TEST_TIME))
				.withValue(QUEUE_TUPLE_BREACH, breach)
				.build();
		return queueTuple;
	}

	private boolean isThresholdBreach(Tuple tuple) {
		double responseTime = tuple.get(RESPONSE_TIME);
		double threshold = thresholds.getThreshold(this.key);
		return responseTime >= threshold;
	}
	
	private long getCurrentTime () {
		return Math.max(this.timeOfLastTick, this.timeOfLastTuple);
	}

	public static class Builder extends AbstractResponseTimeStrategy.Builder {
		
		private Builder (Attributes keyAttributes) {
			super(keyAttributes);
		}

		@Override
		protected final AbstractResponseTimeStrategy doBuild() {
			return new BinomialTestResponseTimeStrategy(this);
		}
	}
	
	private static class QueueTupleComparator implements Comparator<Tuple> {

		@Override
		public int compare(Tuple t1, Tuple t2) {
			long testTime1 = t1.get(QUEUE_TUPLE_TIME);
			long testTime2 = t2.get(QUEUE_TUPLE_TIME);
			long diff = testTime1 - testTime2;
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			} 
			return 0;
		}
		
	}
	


}
