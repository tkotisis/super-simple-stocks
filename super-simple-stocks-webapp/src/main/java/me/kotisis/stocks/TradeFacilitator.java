package me.kotisis.stocks;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.Trade;

@Named
public class TradeFacilitator {

	final static Logger log = LoggerFactory.getLogger(TradeFacilitator.class);

	@Inject
	private TradeLog tradeLog;

	@Inject
	private OrderBook orderBook;

	@Inject
	private OrderLog orderLog;

	private final Lock lock = new ReentrantLock();

	public void matchOrder(@Observes Order lastOrder) {
		lock.lock();

		try {
			if (orderBook.isEmpty()) {
				log.info("Order book empty. Putting order " + lastOrder.getId() + " into order book.");
				orderBook.putOrder(lastOrder);
				return;
			}
			Order matchingOrder = orderBook.findMatchingOrder(lastOrder);
			if (matchingOrder != null) {
				log.info("Found matching order: " + matchingOrder.getId() + " (for order " + lastOrder.getId() + ")");
				recordTrade(lastOrder, matchingOrder);
			} else {
				log.info("Matching order not found. Putting order " + lastOrder.getId() + " into order book.");
				orderBook.putOrder(lastOrder);
			} 
		} finally {
			lock.unlock();
		}
	}

	private void recordTrade(Order currentOrder, Order matchingOrder) {
		Trade trade = tradeLog.put(new Trade(new Date(), currentOrder.getStock(), 
				currentOrder.getPrice(), currentOrder.getQuantity()));

		orderLog.get(matchingOrder.getId()).setTrade(trade.getId());
		orderBook.deleteOrder(matchingOrder);
		orderLog.get(currentOrder.getId()).setTrade(trade.getId());

		log.info("Trade: " + trade.getId() + " between orders " + 
				currentOrder.getId() + " (price: " + currentOrder.getPrice() + 
				", quantity: " + currentOrder.getQuantity() + "), " + 
				matchingOrder.getId() + " (price: " + matchingOrder.getPrice() + 
				", quantity: " + matchingOrder.getQuantity() + ")");
	}

}
