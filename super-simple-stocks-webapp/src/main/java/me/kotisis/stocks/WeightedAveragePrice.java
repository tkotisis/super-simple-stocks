package me.kotisis.stocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import me.kotisis.stocks.model.Trade;

public class WeightedAveragePrice implements IPriceCalculationStrategy {

	@Override
	public BigDecimal calculate(List<Trade> trades) {
		BigDecimal stockPrice = BigDecimal.ZERO;
		BigInteger priceQuantProductsSum = BigInteger.ZERO;
		BigInteger quantSum = BigInteger.ZERO;
		for (Trade trade : trades) {
			quantSum = quantSum.add(trade.getQuantity());
			priceQuantProductsSum = priceQuantProductsSum.add(trade.getPrice().multiply(trade.getQuantity()));
		}
		stockPrice = BigDecimal.valueOf(priceQuantProductsSum.longValue()).divide(BigDecimal.valueOf(quantSum.longValue()), 3, RoundingMode.HALF_UP);

		return stockPrice;
	}

}
