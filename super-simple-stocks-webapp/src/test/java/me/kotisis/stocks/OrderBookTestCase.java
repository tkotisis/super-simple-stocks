package me.kotisis.stocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;

public class OrderBookTestCase {

	OrderBook orderBook;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		orderBook = new OrderBook();
		orderBook.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindMatchingOrderHappyPath() {

		Order mockOrder = Mockito.mock(Order.class);
		when(mockOrder.getId()).thenReturn(BigInteger.ONE);
		when(mockOrder.getType()).thenReturn(OrderType.BUY);
		when(mockOrder.getPrice()).thenReturn(BigInteger.TEN);
		when(mockOrder.getQuantity()).thenReturn(BigInteger.TEN);

		Order mockMatchingOrder = Mockito.mock(Order.class);
		when(mockMatchingOrder.getId()).thenReturn(BigInteger.valueOf(2));
		when(mockMatchingOrder.getType()).thenReturn(OrderType.SELL);
		when(mockMatchingOrder.getPrice()).thenReturn(BigInteger.TEN);
		when(mockMatchingOrder.getQuantity()).thenReturn(BigInteger.TEN);
		orderBook.putOrder(mockMatchingOrder);

		assertEquals(mockMatchingOrder, orderBook.findMatchingOrder(mockOrder));
	}

	@Test
	public void testDoNotMatchOrdersDifferingInPrice() throws Exception {
		Order mockOrder = Mockito.mock(Order.class);
		when(mockOrder.getId()).thenReturn(BigInteger.ONE);
		when(mockOrder.getType()).thenReturn(OrderType.BUY);
		when(mockOrder.getPrice()).thenReturn(BigInteger.TEN);
		when(mockOrder.getQuantity()).thenReturn(BigInteger.TEN);

		Order mockNotMatchingOrder = Mockito.mock(Order.class);
		when(mockNotMatchingOrder.getId()).thenReturn(BigInteger.valueOf(2));
		when(mockNotMatchingOrder.getType()).thenReturn(OrderType.SELL);
		when(mockNotMatchingOrder.getPrice()).thenReturn(BigInteger.valueOf(100));
		when(mockNotMatchingOrder.getQuantity()).thenReturn(BigInteger.TEN);
		orderBook.putOrder(mockNotMatchingOrder);

		assertNull(orderBook.findMatchingOrder(mockOrder));
	}

	@Test
	public void testDoNotMatchOrdersDifferingInQuantity() throws Exception {
		Order mockOrder = Mockito.mock(Order.class);
		when(mockOrder.getId()).thenReturn(BigInteger.ONE);
		when(mockOrder.getType()).thenReturn(OrderType.BUY);
		when(mockOrder.getPrice()).thenReturn(BigInteger.TEN);
		when(mockOrder.getQuantity()).thenReturn(BigInteger.TEN);

		Order mockNotMatchingOrder = Mockito.mock(Order.class);
		when(mockNotMatchingOrder.getId()).thenReturn(BigInteger.valueOf(2));
		when(mockNotMatchingOrder.getType()).thenReturn(OrderType.SELL);
		when(mockNotMatchingOrder.getPrice()).thenReturn(BigInteger.TEN);
		when(mockNotMatchingOrder.getQuantity()).thenReturn(BigInteger.valueOf(100));
		orderBook.putOrder(mockNotMatchingOrder);

		assertNull(orderBook.findMatchingOrder(mockOrder));
	}

}
