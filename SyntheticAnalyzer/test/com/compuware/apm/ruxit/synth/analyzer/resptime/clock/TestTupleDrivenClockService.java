package com.compuware.apm.ruxit.synth.analyzer.resptime.clock;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.clock.TupleDrivenClockService.newSimulatedClock;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockListener;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.TupleSourceInputService;

public class TestTupleDrivenClockService {
	private static final Attribute<Long> ID = new Attribute<>(Long.class, "ID");  
	private static final Attribute<String> NAME = new Attribute<>(String.class, "NAME");
	private static final Attribute<Long> TEST_TIME = new Attribute<>(Long.class, "TEST_TIME");
	private static final Attributes SCHEMA = Attributes.newAttributes()
			.withAttribute(ID)
			.withAttribute(NAME)
			.withAttribute(TEST_TIME)
			.build();


	@Test
	public void testTicksOnTupleReceived() {
		final long startTime = 1412100426085L;
		final long period = 5;
		final TimeUnit timeUnit = TimeUnit.MINUTES;
		final long interval = TimeUnit.MILLISECONDS.convert(period, timeUnit);
		
		final TupleSourceInputService tupleSource = new TupleSourceInputService();
		final TupleDrivenClockService clock = newSimulatedClock()
				.withTupleSource(tupleSource)
				.withStartTime(startTime)
				.withTimeAttribute(TEST_TIME)
				.withPeriod(5)
				.withTimeUnit(TimeUnit.MINUTES)
				.build();
		
		final Tuple.Builder tupleBuilder = TupleImpl.newTuple(SCHEMA);
		final List<Long> actual = new ArrayList<>();
		final List<Long> expected = new ArrayList<>();
		
		clock.addClockListener(new ClockListener () {
			@Override
			public void onClockTick(long time) {
				actual.add(time);
			}
			
		});
		
		clock.start();
		tupleSource.start();
		
		long time = startTime + TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
		
		// Add a tuple to the tuple source and confirm that this advances the
		// clock in five-minute increments up to and including the test time associated
		// with the tuple.
		Tuple tuple = tupleBuilder
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.withValue(TEST_TIME, time)
				.build();
		tupleSource.notify(tuple);
		tupleBuilder.clearValues();
        
		long lastTick = startTime;
		for (long nextTick = startTime + interval; nextTick <= time; nextTick += interval) {
			expected.add(nextTick);
			lastTick = nextTick;
		}
		assertThat(actual, equalTo(expected));
		assertThat(clock.getTime(), equalTo(tuple.get(TEST_TIME)));
		
		Set<Long> actualIntervals = new HashSet<>();
		Long last = null;
		for (long tick : actual) {
			if (last == null) {
				last = tick;
				return;
			}
			long actualInterval = tick - last;
			actualIntervals.add(actualInterval);
			last = tick;
		}
		
		assertThat(actualIntervals.size(), equalTo(1));
		assertThat(actualIntervals.iterator().next(), equalTo(interval));

		// Add another tuple to the tuple source and confirm that this advances the
		// clock in five-minute increments up to but not including the test time associated
		// with the tuple.  The test time of the tuple is not included because it does not
		// fall evenly within a five-minute interval.

		time += TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS) + 5L;
		tuple = tupleBuilder
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.withValue(TEST_TIME, time)
				.build();
		tupleSource.notify(tuple);
		tupleBuilder.clearValues();
		
		for (long nextTick = lastTick + interval; nextTick <= time; nextTick += interval) {
			expected.add(nextTick);
			lastTick = nextTick;
		}
		assertThat(actual, equalTo(expected));
		assertThat(clock.getTime(), equalTo(tuple.get(TEST_TIME) - 5L));
		
		actualIntervals = new HashSet<>();
		last = null;
		for (long tick : actual) {
			if (last == null) {
				last = tick;
				return;
			}
			long actualInterval = tick - last;
			actualIntervals.add(actualInterval);
			last = tick;
		}
		
		assertThat(actualIntervals.size(), equalTo(1));
		assertThat(actualIntervals.iterator().next(), equalTo(interval));
		
		// Poke the clock directly by specifying a time and confirm that this advances the
		// clock in five-minute increments up to and including the specified time.
		time += TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS) + 5L;
		clock.notify(time);
	
		for (long nextTick = lastTick + interval; nextTick <= time; nextTick += interval) {
			expected.add(nextTick);
			lastTick = nextTick;
		}
		assertThat(actual, equalTo(expected));
		assertThat(clock.getTime(), equalTo(tuple.get(TEST_TIME) - 5L));
		
		actualIntervals = new HashSet<>();
		last = null;
		for (long tick : actual) {
			if (last == null) {
				last = tick;
				return;
			}
			long actualInterval = tick - last;
			actualIntervals.add(actualInterval);
			last = tick;
		}
		
		assertThat(actualIntervals.size(), equalTo(1));
		assertThat(actualIntervals.iterator().next(), equalTo(interval));
		
		tupleSource.stop();
		clock.stop();

	}

}
