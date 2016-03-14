package me.kotisis.stocks.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class CommonStock extends Stock {

	public CommonStock() {
		super();
	}

	public CommonStock(String symbol, BigInteger lastDividend, BigInteger parValue) {
		super(symbol, lastDividend, parValue);
	}

	@Override
	public BigDecimal calculateDividendYield() {
		if (getPrice().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		return BigDecimal.valueOf(getLastDividend().longValue()).
				divide(getPrice(), 6, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal calculatePERatio() {
		if (getLastDividend().compareTo(BigInteger.ZERO) == 0) {
			return null;
		}
		return getPrice().divide(BigDecimal.valueOf(getLastDividend().longValue()), 3, RoundingMode.HALF_UP);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CommonStock");
		sb.append("{symbol='").append(getSymbol()).append('\'');
		sb.append(", lastDividend=").append(getLastDividend());
		sb.append(", parValue=").append(getParValue());
		sb.append(", price=").append(getPrice());
		sb.append('}');
		return sb.toString();
	}

}
