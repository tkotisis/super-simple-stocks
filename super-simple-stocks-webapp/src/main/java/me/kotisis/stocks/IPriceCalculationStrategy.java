package me.kotisis.stocks;

import java.math.BigDecimal;
import java.util.List;

import me.kotisis.stocks.model.Trade;

public interface IPriceCalculationStrategy {

	public BigDecimal calculate(List<Trade> trades);

}
