package me.kotisis.stocks;

import java.math.BigDecimal;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

@Named
public class ExchangeTradeObserver {

	@Inject
	private Exchange exchange;

	public void updateStockPrice(@Observes Trade currentTrade) {
		Stock stock = exchange.getStockBySymbol(currentTrade.getStock());
		if (stock != null) {
			BigDecimal price = exchange.calculatePrice(stock);
			stock.setPrice(price);
			exchange.addStock(stock);
			exchange.updateAllShareIndex();
		}
	}

}
