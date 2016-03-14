package me.kotisis.stocks.model;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PreferredStockTestCase {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	PreferredStock stock;

	@Before
	public void setUp() throws Exception {
		stock = new PreferredStock();
	}

	@Test
	public void testThrowExceptionWhenNegativeFixedDividend() throws Exception {
		thrown.expect(ValidationException.class);
		stock.setFixedDividend(BigDecimal.valueOf(-1));
	}

	@Test
	public void testCalculateDividendYield() throws Exception {
		stock.setFixedDividend(BigDecimal.valueOf(2));
		stock.setParValue(BigInteger.valueOf(100));
		stock.setPrice(BigDecimal.valueOf(40));
		String failureMessage = "Expected: " + 0.05 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.valueOf(0.05)) == 0);
	}

	@Test
	public void testCalculateDividendYieldWhenZeroPercentDividend() throws Exception {
		stock.setFixedDividend(BigDecimal.ZERO);
		stock.setParValue(BigInteger.valueOf(100));
		stock.setPrice(BigDecimal.valueOf(120));
		String failureMessage = "Expected: " + 0 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.ZERO) == 0);
	}

	@Test
	public void testCalculateDividendYieldWhenZeroPrice() throws Exception {
		stock.setFixedDividend(BigDecimal.valueOf(3));
		stock.setParValue(BigInteger.valueOf(100));
		stock.setPrice(BigDecimal.ZERO);
		String failureMessage = "Expected: " + 0 + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.ZERO) == 0);
	}

	@Test
	public void testCalculateDividendYieldWhenLowParHighPrice() throws Exception {
		stock.setFixedDividend(BigDecimal.ONE);
		stock.setParValue(BigInteger.TEN);
		stock.setPrice(BigDecimal.valueOf(50000));
		double expected = 0.000002;
		String failureMessage = "Expected: " + expected + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.valueOf(expected)) == 0);
	}

	/**
	 * Yield is computed (and practically is) zero, if it cannot be represented with accuracy 
	 * of 6 decimal places.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCalculateZeroYieldWhenVeryLowParHighPrice() throws Exception {
		stock.setFixedDividend(BigDecimal.ONE);
		stock.setParValue(BigInteger.ONE);
		stock.setPrice(BigDecimal.valueOf(50000));
		double expected = 0;
		String failureMessage = "Expected: " + expected + ", Actual: " + stock.calculateDividendYield();
		assertTrue(failureMessage, stock.calculateDividendYield().compareTo(BigDecimal.valueOf(expected)) == 0);
	}

}
