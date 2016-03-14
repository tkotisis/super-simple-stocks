package me.kotisis.stocks;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;
import me.kotisis.stocks.model.Stock;

@Named
@ApplicationScoped
public class OrderBook {

	private Map<String, Queue<BigInteger>> stockOrdersMap;
	private Map<BigInteger, Order> orders;

	@PostConstruct
	public void init() {
		this.stockOrdersMap = new HashMap<String, Queue<BigInteger>>();
		this.orders = new HashMap<BigInteger, Order>();
	}

	public void putOrder(Order order) {
		orders.put(order.getId(), order);

		Queue<BigInteger> stockOrderQueue = stockOrdersMap.get(order.getStock());
		if (stockOrderQueue == null) {
			stockOrderQueue = new LinkedBlockingQueue<BigInteger>();
		}
		stockOrderQueue.add(order.getId());
		stockOrdersMap.put(order.getStock(), stockOrderQueue);
	}

	public void deleteOrder(Order order) {
		orders.remove(order.getId());

		for (Iterator<BigInteger> it = stockOrdersMap.get(order.getStock()).iterator(); it.hasNext();) {
			BigInteger orderId = (BigInteger) it.next();
			if (orderId.compareTo(order.getId()) == 0) {
				it.remove();
			}
		}
	}

	public Order getById(BigInteger id) {
		return orders.get(id);
	}

	public boolean isEmpty() {
		return orders.isEmpty();
	}

	/**
	 * Returns first {@link Order} in {@link OrderBook} with same price and 
	 * quantity and of opposite type as argument {@link Order}.
	 * 
	 * @param order the {@link Order} to be matched.
	 * @return {@link Order} matching argument order, 
	 * or {@code null} if none is found.
	 */
	protected Order findMatchingOrder(Order order) {
		OrderType matchingOrderType = null;
		switch (order.getType()) {
		case BUY:
			matchingOrderType = OrderType.SELL;
			break;
		case SELL:
			matchingOrderType = OrderType.BUY;
			break;

		default:
			break;
		}

		Order matchingOrder = null;
		for (Order bookOrder : getOrdersByStock(order.getStock())) {
			if (bookOrder.getType() != matchingOrderType ||
					bookOrder.getPrice().compareTo(order.getPrice()) != 0 ||
					bookOrder.getQuantity().compareTo(order.getQuantity()) != 0 ) {
				continue;
			}

			matchingOrder = bookOrder;
		}
		return matchingOrder;
	}

	/**
	 * Returns open orders for the stock specified in argument.
	 * 
	 * @param stockSymbol symbol of {@link Stock} whose associated orders should be returned
	 * @return the open {@link Order}s for specified {@link Stock} or an empty {@link List} if there are none.
	 */
	public List<Order> getOrdersByStock(String stockSymbol) {
		Queue<BigInteger> orderIds = stockOrdersMap.get(stockSymbol);

		if (orderIds == null || orderIds.isEmpty()) {
			return new LinkedList<Order>();
		}

		List<Order> stockOrders = new ArrayList<Order>();
		for (BigInteger orderId : orderIds) {
			stockOrders.add(orders.get(orderId));
		}

		return stockOrders;
	}
}
