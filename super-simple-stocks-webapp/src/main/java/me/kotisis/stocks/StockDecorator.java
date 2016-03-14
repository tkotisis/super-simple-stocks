package me.kotisis.stocks;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import me.kotisis.stocks.model.CommonStock;
import me.kotisis.stocks.model.PreferredStock;
import me.kotisis.stocks.model.Stock;

public class StockDecorator {

	private Stock stock;

	public StockDecorator() {}

	public StockDecorator(Stock stock) {
		this.stock = stock;
	}

	public Stock getStock() {
		return stock;
	}

	@JsonTypeInfo(use=Id.NAME, include=As.EXTERNAL_PROPERTY, property="type")
	@JsonSubTypes({ @Type(value = CommonStock.class, name = "common"), @Type(value = PreferredStock.class, name = "preferred") })
	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
