package me.kotisis.stocks;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;

import me.kotisis.stocks.WeightedAveragePrice;
import me.kotisis.stocks.model.Trade;

public class WeightedAveragePriceTestCase {

	@Test
	public void testCalculatePriceFromSingleTrade() {
		Trade mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.TEN);
		when(mockTrade.getQuantity()).thenReturn(BigInteger.TEN);

		ArrayList<Trade> trades = new ArrayList<Trade>();
		trades.add(mockTrade);

		WeightedAveragePrice weightedAvg = new WeightedAveragePrice();
		assertTrue(BigDecimal.TEN.compareTo(weightedAvg.calculate(trades)) == 0);
	}

	@Test
	public void testCalculatePriceFromMultipleTradesSimple() throws Exception {
		ArrayList<Trade> trades = new ArrayList<Trade>();

		Trade mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(100));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(2000));
		trades.add(mockTrade);
		mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(120));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(1000));
		trades.add(mockTrade);
		mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(130));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(500));
		trades.add(mockTrade);

		WeightedAveragePrice weightedAvg = new WeightedAveragePrice();
		BigDecimal price = weightedAvg.calculate(trades);
		assertTrue(BigDecimal.valueOf(110).compareTo(price) == 0);
	}

	@Test
	public void testCalculatePriceFromMultipleTradesRoundUp() throws Exception {
		ArrayList<Trade> trades = new ArrayList<Trade>();

		Trade mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(102));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(2000));
		trades.add(mockTrade);
		mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(107));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(1000));
		trades.add(mockTrade);
		mockTrade = Mockito.mock(Trade.class);
		when(mockTrade.getPrice()).thenReturn(BigInteger.valueOf(109));
		when(mockTrade.getQuantity()).thenReturn(BigInteger.valueOf(500));
		trades.add(mockTrade);

		WeightedAveragePrice weightedAvg = new WeightedAveragePrice();
		BigDecimal price = weightedAvg.calculate(trades);
		assertTrue("Expected: 104.429, Actual: " + price, BigDecimal.valueOf(104.429).compareTo(price) == 0);
	}
}
