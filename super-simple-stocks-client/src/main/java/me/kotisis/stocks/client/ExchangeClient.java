 package me.kotisis.stocks.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

public class ExchangeClient {

	private URI uri;
	private Client client;

	public ExchangeClient() {
		uri = UriBuilder.
				fromUri("http://localhost/exchange").port(8080).build();
		client = ClientBuilder.newClient();
	}

	public Stock getStockBySymbol(String symbol) {
		Response response = client.target(uri).path("stock/").path(symbol).request().get();
		StockDecorator stockDecorator = response.readEntity(StockDecorator.class);
		return stockDecorator.getStock();
	}

	public Order placeOrder(Order order) {
		Response response = client.target(uri).path("order").request().post(
				Entity.entity(order, MediaType.APPLICATION_JSON));
		URI orderURI = response.getLocation();
		response = client.target(orderURI).request().get();
		return response.readEntity(Order.class);
	}

	public BigDecimal getAllShareIndex() {
		Response response = client.target(uri).path("index").request().get();
		return response.readEntity(BigDecimal.class);
	}

	public Order getOrder(BigInteger orderId) {
		Response response = client.target(uri).path("order").
				path(orderId.toString()).request().get();
		return response.readEntity(Order.class);
	}

	public Trade getTrade(long tradeId) {
		Response response = client.target(uri).path("trade").
				path(String.valueOf(tradeId)).request().get();
		return response.readEntity(Trade.class);
	}

}
