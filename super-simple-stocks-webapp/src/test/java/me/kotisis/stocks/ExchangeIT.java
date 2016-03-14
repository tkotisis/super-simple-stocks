package me.kotisis.stocks;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import me.kotisis.stocks.model.Stock;

public class ExchangeIT {

	Exchange exchange;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		exchange = new Exchange();
		exchange.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateAllShareIndex() throws Exception {
		BigDecimal allShareIndex = exchange.getAllShareIndex();

		for (int i = 0; i < 500; i++) {
			calculateAllShareIndex(1000);
		}

		// There's a miniscule chance they might be equal
		assertNotEquals(allShareIndex.longValue(), 
				exchange.getAllShareIndex().longValue());
	}

	public void calculateAllShareIndex(int stocksCount) throws Exception {
		// Add a few more stocks with random prices
		RandomDataGenerator randomData = new RandomDataGenerator();
		for (int i = 0; i < stocksCount; i++) {
			Stock mockStock = Mockito.mock(Stock.class);
			BigDecimal price = BigDecimal.valueOf(randomData.nextLong(1, 1000000)).divide(BigDecimal.valueOf(1000));
			when(mockStock.getPrice()).thenReturn(price);
//			System.out.println("Adding stock with price: " + price);
			exchange.getListedStocks().put(String.valueOf(i), mockStock);
		}

		exchange.updateAllShareIndex();

		assertNotNull(exchange.getAllShareIndex());
		assertNotEquals(exchange.getAllShareIndex(), BigDecimal.ZERO);

		/*String pattern = "#####.###";
		DecimalFormat decimalFormat = (DecimalFormat)
				NumberFormat.getNumberInstance(Locale.UK);
		decimalFormat.applyPattern(pattern);

		System.out.println("GBCE All Share Index: " + 
				decimalFormat.format(exchange.getAllShareIndex()));*/
	}
}
