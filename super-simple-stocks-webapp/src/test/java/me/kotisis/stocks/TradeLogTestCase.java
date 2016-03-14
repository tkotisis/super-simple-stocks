package me.kotisis.stocks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import me.kotisis.stocks.model.Trade;

public class TradeLogTestCase {

	TradeLog tradeLog;

	@Before
	public void setUp() throws Exception {
		tradeLog = new TradeLog();
		tradeLog.init();
	}

	@Test
	public void testGetTradesAfterCertainDate() throws Exception {
		Date now = new Date();
		Trade mockTradeBefore = Mockito.mock(Trade.class);
		when(mockTradeBefore.getTimestamp()).thenReturn(now);
		Trade mockTradeAfter = Mockito.mock(Trade.class);
		int hour = 60*60*1000;
		Date futureDate = new Date(now.getTime() + hour);
		when(mockTradeAfter.getTimestamp()).thenReturn(futureDate);

		LinkedList<Trade> tradeList = new LinkedList<Trade>();
		tradeList.add(mockTradeBefore);
		tradeList.add(mockTradeAfter);
		tradeLog.getStockTradesMap().put("foo", tradeList);

		List<Trade> trades = tradeLog.getTrades("foo", new Date());
		assertFalse(trades.isEmpty());
		assertTrue(trades.size() == 1);
		assertEquals(futureDate, trades.get(0).getTimestamp());
	}
}
