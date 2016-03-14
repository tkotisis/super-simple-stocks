package me.kotisis.stocks;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import me.kotisis.stocks.model.CommonStock;
import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;
import me.kotisis.stocks.model.PreferredStock;
import me.kotisis.stocks.model.Trade;

public class ExchangeServiceIT {

	private static URI uri;
	private static Client client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		uri = UriBuilder.
				fromUri("http://localhost/exchange").port(8080).build();
		client = ClientBuilder.newClient();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldGetCommonStock() throws Exception {
		Response response = client.target(uri).path("stock/TEA").request().get();
		StockDecorator stockDecorator = response.readEntity(StockDecorator.class);
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));
//		assertNotNull(stockDecorator);
		assertTrue(stockDecorator.getStock() instanceof CommonStock);
		CommonStock stock = (CommonStock) stockDecorator.getStock();
		assertEquals("TEA", stock.getSymbol());
	}

	@Test
	public void shouldGetPreferredStock() throws Exception {
		Response response = client.target(uri).path("stock/GIN").request().get();
		StockDecorator stockDecorator = response.readEntity(StockDecorator.class);
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));

		assertTrue(stockDecorator.getStock() instanceof PreferredStock);
		PreferredStock stock = (PreferredStock) stockDecorator.getStock();
		assertEquals("GIN", stock.getSymbol());
		assertNotNull(stock.getFixedDividend());
	}

	@Test
	public void shouldNotFindStock() throws Exception {
		Response response = client.target(uri).path("stock/RUM").request().get();
		assertEquals(Response.Status.NOT_FOUND, Response.Status.fromStatusCode(response.getStatus()));
	}

	@Test
	public void shouldPlaceOrder() throws Exception {
		Order order = new Order(OrderType.BUY, "GIN", BigInteger.valueOf(120), BigInteger.valueOf(1000));
		Response response = client.target(uri).path("order").request().post(Entity.entity(order, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED, Response.Status.fromStatusCode(response.getStatus()));
		URI orderURI = response.getLocation();
		assertNotNull(orderURI);
		response = client.target(orderURI).request().get();
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));
		Order actualOrder = response.readEntity(Order.class);
		assertEquals(order.getType(), actualOrder.getType());
		assertEquals(order.getStock(), actualOrder.getStock());
		assertEquals(order.getPrice(), actualOrder.getPrice());
		assertEquals(order.getQuantity(), actualOrder.getQuantity());
	}

	@Test
	public void shouldExecuteTrade() throws Exception {
		Order sellOrder = new Order(OrderType.SELL, "TEA", BigInteger.valueOf(140), BigInteger.valueOf(1000));
		Response response = client.target(uri).path("order").request().post(Entity.entity(sellOrder, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED, Response.Status.fromStatusCode(response.getStatus()));

		Order buyOrder = new Order(OrderType.BUY, "TEA", BigInteger.valueOf(140), BigInteger.valueOf(1000));
		response = client.target(uri).path("order").request().post(Entity.entity(buyOrder, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED, Response.Status.fromStatusCode(response.getStatus()));
		URI orderURI = response.getLocation();
		assertNotNull(orderURI);
		response = client.target(orderURI).request().get();
		Order acceptedBuyOrder = response.readEntity(Order.class);

		response = client.target(uri).path("trade").path(String.valueOf(acceptedBuyOrder.getTrade())).request().get();
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));
		Trade trade = response.readEntity(Trade.class);
		assertEquals(acceptedBuyOrder.getStock(), trade.getStock());
		assertEquals(acceptedBuyOrder.getPrice(), trade.getPrice());
		assertEquals(acceptedBuyOrder.getQuantity(), trade.getQuantity());
	}

	@Test
	public void shouldUpdateStockPriceAfterTrade() throws Exception {
		String stock = "ALE";
		BigDecimal priceBefore = getCommonStockPrice(stock);

		BigInteger orderPrice = priceBefore.toBigInteger().add(BigInteger.valueOf(2));

		placeMatchingOrders(stock, orderPrice, BigInteger.valueOf(1500));

		BigDecimal priceAfter = getCommonStockPrice(stock);
		String failureMessage = "Expected Price: " + priceBefore + ", Actual Price: " + priceAfter;
		assertTrue(failureMessage, priceBefore.compareTo(priceAfter) != 0);
	}

	@Test
	public void shouldUpdateAllShareIndexAfterTrade() throws Exception {
		String stock = "ALE";
		BigDecimal priceBefore = getCommonStockPrice(stock);

		BigInteger orderPrice = priceBefore.toBigInteger().subtract(BigInteger.valueOf(2));

		placeMatchingOrders(stock, orderPrice, BigInteger.valueOf(2000));

		BigDecimal priceAfter = getCommonStockPrice(stock);
		String failureMessage = "Expected Price: " + priceBefore + ", Actual Price: " + priceAfter;
		assertTrue(failureMessage, priceBefore.compareTo(priceAfter) != 0);
	}

	private Response placeMatchingOrders(String stock, BigInteger orderPrice, BigInteger orderQuantity) {
		Order sellOrder = new Order(OrderType.SELL, stock, orderPrice, orderQuantity);
		Response response = client.target(uri).path("order").request().post(Entity.entity(sellOrder, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED, Response.Status.fromStatusCode(response.getStatus()));

		Order buyOrder = new Order(OrderType.BUY, stock, orderPrice, orderQuantity);
		response = client.target(uri).path("order").request().post(Entity.entity(buyOrder, MediaType.APPLICATION_JSON));
		assertEquals(Response.Status.CREATED, Response.Status.fromStatusCode(response.getStatus()));
		URI orderURI = response.getLocation();
		assertNotNull(orderURI);
		response = client.target(orderURI).request().get();
		Order acceptedBuyOrder = response.readEntity(Order.class);

		response = client.target(uri).path("trade").path(String.valueOf(acceptedBuyOrder.getTrade())).request().get();
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));

		return response;
	}

	private BigDecimal getCommonStockPrice(String stockSymbol) {
		Response response = client.target(uri).path("stock").path(stockSymbol).request().get();
		StockDecorator stockDecorator = response.readEntity(StockDecorator.class);
		assertEquals(Response.Status.OK, Response.Status.fromStatusCode(response.getStatus()));
		assertTrue(stockDecorator.getStock() instanceof CommonStock);
		CommonStock stock = (CommonStock) stockDecorator.getStock();
		return stock.getPrice();
	}
}
