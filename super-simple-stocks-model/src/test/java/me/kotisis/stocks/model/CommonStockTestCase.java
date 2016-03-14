package me.kotisis.stocks.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class CommonStockTestCase {

	CommonStock stock;

	@Before
	public void setUp() throws Exception {
		stock = new CommonStock();
	}

	@Test
	public void testCalculateDividendYieldHappyPath() throws Exception {
		stock.setLastDividend(BigInteger.TEN);
		stock.setPrice(BigDecimal.valueOf(200));
		String failureMessage = "Expected: " + 0.05 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.valueOf(0.05)) == 0);
	}

	@Test
	public void testCalculateDividendYieldForZeroDividend() throws Exception {
		stock.setLastDividend(BigInteger.ZERO);
		stock.setPrice(BigDecimal.valueOf(200));
		String failureMessage = "Expected: " + 0 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.ZERO) == 0);
	}

	@Test
	public void testCalculateDividendYieldForZeroPrice() throws Exception {
		stock.setLastDividend(BigInteger.TEN);
		stock.setPrice(BigDecimal.ZERO);
		String failureMessage = "Expected: " + 0 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.ZERO) == 0);
	}

	@Test
	public void testCalculateDividendYieldForLowDividendHighPrice() throws Exception {
		stock.setLastDividend(BigInteger.ONE);
		stock.setPrice(BigDecimal.valueOf(500000));
		double expected = 0.000002;
		String failureMessage = "Expected: " + expected + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.valueOf(expected)) == 0);
	}

	@Test
	public void testCalculatePERatioNullWhenZeroLastDividend() throws Exception {
		stock.setLastDividend(BigInteger.ZERO);
		stock.setPrice(BigDecimal.valueOf(150));
		assertNull(stock.calculatePERatio());
	}
}
