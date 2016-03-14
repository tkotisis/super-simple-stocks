package me.kotisis.stocks.model;

public enum OrderType {
	BUY, SELL;

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
