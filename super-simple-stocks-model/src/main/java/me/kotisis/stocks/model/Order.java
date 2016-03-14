package me.kotisis.stocks.model;

import java.math.BigInteger;

public class Order {

	private BigInteger id;
	private OrderType type;
	private String stock;
	private BigInteger price;
	private BigInteger quantity;
	private long trade;

	public Order() {}

	public Order(OrderType type, String stock, BigInteger price, BigInteger quantity) {
		this.type = type;
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
	}

	public Order(BigInteger id, OrderType type, String stock, BigInteger price, BigInteger quantity) {
		this.id = id;
		this.type = type;
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public BigInteger getPrice() {
		return price;
	}

	public void setPrice(BigInteger price) {
		this.price = price;
	}

	public BigInteger getQuantity() {
		return quantity;
	}

	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	public long getTrade() {
		return trade;
	}

	public void setTrade(long trade) {
		this.trade = trade;
	}

}
