package me.kotisis.stocks.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * A {@link Stock} with fixed dividend.
 * Fixed dividend is in percent format.
 *
 */
public class PreferredStock extends Stock {

	private BigDecimal fixedDividend;

	public PreferredStock() {
		super();
	}

	public PreferredStock(String symbol, BigInteger lastDividend, BigDecimal fixedDividend, BigInteger parValue) throws Exception {
		super(symbol, lastDividend, parValue);

		validateFixedDividend(fixedDividend);
		this.fixedDividend = fixedDividend;
	}

	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}

	public void setFixedDividend(BigDecimal fixedDividend) throws Exception {
		validateFixedDividend(fixedDividend);
		this.fixedDividend = fixedDividend;
	}

	private void validateFixedDividend(BigDecimal fixedDividend) throws Exception {
		if (fixedDividend.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("fixedDividend");
		}
	}

	@Override
	public BigDecimal calculateDividendYield() {
		if (getPrice().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal fixedDividendPercentRate = 
				fixedDividend.divide(BigDecimal.valueOf(100));
		BigDecimal dividend = fixedDividendPercentRate.multiply(BigDecimal.valueOf(
				getParValue().longValue()));
		return dividend.divide(getPrice(), 6, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal calculatePERatio() {
		return null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PreferredStock");
		sb.append("{symbol='").append(getSymbol()).append('\'');
		sb.append(", lastDividend=").append(getLastDividend());
		sb.append(", fixedDividend=").append(getFixedDividend());
		sb.append(", parValue=").append(getParValue());
		sb.append(", price=").append(getPrice());
		sb.append('}');
		return sb.toString();
	}

}
