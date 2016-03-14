package me.kotisis.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;

@RunWith(CdiRunner.class)
public class OrderLogTestCase {

	private static final int THREAD_COUNT = 400;

	private class PlaceOrderOperation implements Runnable {

		@Override
		public void run() {
			Order order = new Order(OrderType.BUY, "TEA", BigInteger.TEN, 
					BigInteger.TEN);
			Order loggedOrder = orderLog.put(order);
//			System.out.println("Created order with id: " + loggedOrder.getId());
		}

	}

	@Inject
	OrderLog orderLog;

	@Test
	public void testSynchronizedPutOrder() throws Exception {
		ThreadGroup threadGroup = new ThreadGroup("OrderPlacerGroup");
		for (int i = 0; i < THREAD_COUNT; i++) {
			Thread thread = new Thread(threadGroup, new PlaceOrderOperation());
			thread.start();
		}

		waitFinish(threadGroup);

		assertEquals(THREAD_COUNT, orderLog.getOrderCount().intValue());
		for (int i = 0; i < THREAD_COUNT; i++) {
			assertNotNull(orderLog.get(BigInteger.valueOf(i+1)));
		}
	}

	private static void waitFinish(ThreadGroup threadGroup) {
		while (threadGroup.activeCount() > 0) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
