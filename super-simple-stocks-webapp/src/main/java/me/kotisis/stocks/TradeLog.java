package me.kotisis.stocks;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

@Named
@ApplicationScoped
public class TradeLog {

	@Inject
	private Event<Trade> tradeAddedEvent;

	Map<Long, Trade> trades;
	Map<String, LinkedList<Trade>> stockTradesMap;
	long tradeCount;

	@PostConstruct
	public void init() {
		trades = new HashMap<Long, Trade>();
		stockTradesMap = new HashMap<String, LinkedList<Trade>>();
		tradeCount = 0;
	}

	public Trade put(Trade trade) {
		trade.setId(++tradeCount);

		trades.put(trade.getId(), trade);

		LinkedList<Trade> stockTrades = stockTradesMap.get(trade.getStock());
		if (stockTrades == null) {
			stockTrades = new LinkedList<Trade>();
		}
		stockTrades.add(trade);
		stockTradesMap.put(trade.getStock(), stockTrades);

		tradeAddedEvent.fire(trade);

		return trade;
	}

	public Trade getById(long id) {
		return trades.get(id);
	}

	/**
	 * Returns trades for a certain {@link Stock}, filtered by argument {@link Date}.
	 * Only trades recorded after the argument timestamp will be returned.
	 * 
	 * @param stockId symbol of the stock, whose trades are to be retrieved.
	 * @param since timestamp, according to which trades are filtered.
	 * @return a {@link LinkedList} containing the {@link Trade} instances, 
	 * as specified by the arguments, or and empty {@link LinkedList} if there 
	 * are no such trades.
	 */
	public List<Trade> getTrades(String stockId, Date since) {
		LinkedList<Trade> tradesList = stockTradesMap.get(stockId);

		if (tradesList == null) {
			return new LinkedList<Trade>();
		}

		if (since != null) {
			LinkedList<Trade> filteredTrades = new LinkedList<Trade>();
			for (Iterator<Trade> it = tradesList.descendingIterator(); it.hasNext();) {
				Trade trade = (Trade) it.next();
				if (trade.getTimestamp().after(since)) {
					filteredTrades.add(trade);
				} else {
					break;
				}
			}
			tradesList = filteredTrades;
		}
		return tradesList;
	}

	protected Map<String, LinkedList<Trade>> getStockTradesMap() {
		return stockTradesMap;
	}

}
