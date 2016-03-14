package me.kotisis.stocks;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import me.kotisis.stocks.model.Order;

@Named
@ApplicationScoped
public class OrderLog {

	@Inject
	private Event<Order> orderAddedEvent;

	private BigInteger orderCount;

	private Map<BigInteger, Order> orderMap;

	private final Lock lock = new ReentrantLock();

	@PostConstruct
	public void init() {
		this.orderMap = new HashMap<BigInteger, Order>();
		this.orderCount = BigInteger.ZERO;
	}

	public Order put(Order order) {
		lock.lock();

		Order currentOrder;
		try {
			orderCount = orderCount.add(BigInteger.ONE);
			currentOrder = new Order(orderCount, order.getType(), order.getStock(), order.getPrice(),
					order.getQuantity());
		} finally {
			lock.unlock();
		}

		orderMap.put(currentOrder.getId(), currentOrder);

		orderAddedEvent.fire(currentOrder);

		return currentOrder;
	}

	public Order get(BigInteger id) {
		return orderMap.get(id);
	}

	protected BigInteger getOrderCount() {
		return orderCount;
	}

}
