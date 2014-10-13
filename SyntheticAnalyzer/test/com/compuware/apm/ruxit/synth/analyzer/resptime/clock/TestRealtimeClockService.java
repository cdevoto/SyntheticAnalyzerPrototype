package com.compuware.apm.ruxit.synth.analyzer.resptime.clock;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.clock.RealtimeClockService.newRealtimeClock;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockListener;

public class TestRealtimeClockService {

	@Test
	public void test() throws InterruptedException {
		final RealtimeClockService clock = newRealtimeClock()
				.withInitialDelay(0)
				.withPeriod(1)
				.withTimeUnit(TimeUnit.SECONDS)
				.build();
		
		final Set<Long> ticks = new LinkedHashSet<>();
		final Semaphore semaphore = new Semaphore(1);
		ClockListener listener = new ClockListener () {
            int count = 0;
            
			@Override
			public void onClockTick(long time) {
				ticks.add(time);
				if (count == 0) {
					try {
						semaphore.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (count >= 4) {
					semaphore.release();
					clock.stop();
				}
				count++;
				System.out.println(time);
			}
			
		};
		clock.addClockListener(listener);
		
		clock.start();
		Thread.sleep(1000);
		semaphore.acquire();
		
		assertThat(ticks.size(), is(5));
		Set<Long> intervals = new HashSet<>();
		Long current = null;
		for (long tick : ticks) {
			if (current == null) {
				current = tick;
				continue;
			}
			long interval = tick - current;
			intervals.add(interval);
			current = tick;
		}
		
		for (long interval : intervals) {
			assertThat (interval >= 1000 - 5 && interval <= 1000 + 5, is(true));
		}
		semaphore.release();
	}

}
