package me.kotisis.stocks;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;

@RunWith(CdiRunner.class)
public class TradeFacilitatorIT {

	final static Logger log = LoggerFactory.getLogger(TradeFacilitatorIT.class);

	private static final int INITIAL_ORDER_COUNT = 10;
	private class MatchOrderOperation implements Runnable {

		private int i;

		public MatchOrderOperation(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			Order order = orderLog.put(
					new Order(OrderType.SELL, "GIN", BigInteger.valueOf(i), 
							BigInteger.valueOf(2*i)));
			log.info("Placed sell order " + order.getId() + 
					" (price: " + order.getPrice() + 
					", quantity: " + order.getQuantity() + ")");
		}

	}

	@Inject
	private OrderBook orderBook;

	@Inject
	private OrderLog orderLog;

	@Inject
	TradeFacilitator tradeFacilitator;

	@Test
	public void testSynchronizedTradeMatchAllOrders() throws Exception {
		// Initialize order book and order log
		for (int i = 0; i < INITIAL_ORDER_COUNT; i++) {
			Order order = new Order(OrderType.BUY, "GIN", 
					BigInteger.valueOf(i+1), BigInteger.valueOf(2*(i+1)));
			orderLog.put(order);
		}

		// Create threads placing matching orders
		ThreadGroup threadGroup = new ThreadGroup("OrderPlacerGroup");
		int threadCount = INITIAL_ORDER_COUNT;
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(threadGroup, new MatchOrderOperation(i + 1));
			thread.start();
		}

		waitFinish(threadGroup);

		assertEquals(INITIAL_ORDER_COUNT + threadCount, orderLog.getOrderCount().intValue());
		assertEquals(threadCount - INITIAL_ORDER_COUNT, orderBook.getOrdersByStock("GIN").size());
	}

	@Test
	public void testSynchronizedTradeMatchHalfOrders() throws Exception {
		// Initialize order book and order log
		for (int i = 0; i < INITIAL_ORDER_COUNT; i++) {
			Order order = new Order(OrderType.BUY, "GIN", 
					BigInteger.valueOf(i+1), BigInteger.valueOf(2*(i+1)));
			orderLog.put(order);
		}

		// Create threads placing two matching orders for each one in the order book
		ThreadGroup threadGroup = new ThreadGroup("OrderPlacerGroup");
		int threadCount = INITIAL_ORDER_COUNT + 10;
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(threadGroup, new MatchOrderOperation(i + 1));
			thread.start();
		}

		waitFinish(threadGroup);

		assertEquals(INITIAL_ORDER_COUNT + threadCount, orderLog.getOrderCount().intValue());
		assertEquals(threadCount - INITIAL_ORDER_COUNT, orderBook.getOrdersByStock("GIN").size());
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
