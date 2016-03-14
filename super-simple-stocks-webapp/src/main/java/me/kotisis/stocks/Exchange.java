package me.kotisis.stocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.kotisis.stocks.model.CommonStock;
import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.PreferredStock;
import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

@ApplicationScoped
public class Exchange {

	final static Logger log = LoggerFactory.getLogger(Exchange.class);

	private Map<String, Stock> listedStocks;

	@Inject
	private OrderLog orderLog;

	@Inject
	private TradeLog tradeLog;

	private BigDecimal allShareIndex;

	@PostConstruct
	public void init() throws Exception {
		log.info("Starting Exchange...");

		listedStocks = new HashMap<String, Stock>();

		addStock(new CommonStock("TEA", BigInteger.valueOf(0), BigInteger.valueOf(100)));
		addStock(new CommonStock("POP", BigInteger.valueOf(8), BigInteger.valueOf(100)));
		addStock(new CommonStock("ALE", BigInteger.valueOf(23), BigInteger.valueOf(60)));
		addStock(new PreferredStock("GIN", BigInteger.valueOf(8), BigDecimal.valueOf(2), BigInteger.valueOf(100)));
		addStock(new CommonStock("JOE", BigInteger.valueOf(13), BigInteger.valueOf(250)));

		updateAllShareIndex();

		log.info("Exchange started.");
	}

	public void addStock(Stock stock) {
		listedStocks.put(stock.getSymbol(), stock);
	}

	public Stock getStockBySymbol(String symbol) {
		Stock stock = listedStocks.get(symbol);
		return stock;
	}

	public BigDecimal calculatePrice(Stock stock) {
		long interval = 15*60*1000;
		long cutoffTime = new Date().getTime() - interval;
		List<Trade> trades = tradeLog.getTrades(stock.getSymbol(), new Date(cutoffTime));

		if (trades.isEmpty()) {
			return stock.getPrice();
		}

		return new WeightedAveragePrice().calculate(trades);
	}

	public void updateAllShareIndex() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (Stock stock : listedStocks.values()) {
			stats.addValue(stock.getPrice().doubleValue());
		}
		allShareIndex = BigDecimal.valueOf(stats.getGeometricMean());
	}

	public Order placeOrder(Order order) {
		//TODO: validate order
		return orderLog.put(order);
	}

	public Map<String, Stock> getListedStocks() {
		return listedStocks;
	}

	protected BigDecimal getAllShareIndex() {
		return allShareIndex;
	}

	protected void setAllShareIndex(BigDecimal allShareIndex) {
		this.allShareIndex = allShareIndex;
	}

}
