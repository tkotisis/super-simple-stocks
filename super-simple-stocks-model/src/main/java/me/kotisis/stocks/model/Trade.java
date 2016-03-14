package me.kotisis.stocks.model;

import java.math.BigInteger;
import java.util.Date;

public class Trade {

	private long id;
	private Date timestamp;
	private String stock;
	private BigInteger price;
	private BigInteger quantity;

	public Trade() {}

	public Trade(Date timestamp, String stock, BigInteger price, BigInteger quantity) {
		this.timestamp = timestamp;
		this.stock = stock;
		this.price = price;
		this.quantity = quantity;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Date getTimestamp() {
		return timestamp;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Trade");
		/*sb.append("{symbol='").append(getSymbol()).append('\'');
		sb.append(", lastDividend=").append(getLastDividend());
		sb.append(", fixedDividend=").append(getFixedDividend());
		sb.append(", parValue=").append(getParValue());
		sb.append('}');*/
		return sb.toString();
	}

}
