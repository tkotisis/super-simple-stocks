package me.kotisis.stocks.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public abstract class Stock {

	private String symbol;
	private BigInteger lastDividend;
	private BigInteger parValue;
	private BigDecimal price;

	public Stock() {}

	public Stock(String symbol, BigInteger lastDividend, BigInteger parValue) {
		this.symbol = symbol;
		this.lastDividend = lastDividend;
		this.parValue = parValue;
		this.price = BigDecimal.valueOf(parValue.longValue());
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigInteger getLastDividend() {
		return lastDividend;
	}

	public void setLastDividend(BigInteger lastDividend) {
		this.lastDividend = lastDividend;
	}

	public BigInteger getParValue() {
		return parValue;
	}

	public void setParValue(BigInteger parValue) {
		this.parValue = parValue;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the stocks' dividend yield, accurate to 6 decimal digits, 
	 * and rounded half way up ({@link RoundingMode}).
	 */
	public abstract BigDecimal calculateDividendYield();

	/**
	 * @return the stocks' P/E ratio, accurate to 3 decimal digits, and rounded 
	 * half way up ({@link RoundingMode}), or {@code null} if undefined/unavailable.
	 */
	public abstract BigDecimal calculatePERatio();

}
