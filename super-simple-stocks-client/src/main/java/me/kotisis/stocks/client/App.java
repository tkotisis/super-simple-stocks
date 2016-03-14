package me.kotisis.stocks.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import me.kotisis.stocks.model.CommonStock;
import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.OrderType;
import me.kotisis.stocks.model.PreferredStock;
import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

public class App 
{
	private static final String DASHES_SEPARATOR = "-----------------------" + 
			"--------------------------------------------------------";

	public static void main( String[] args ) {

		Scanner sc = new Scanner(System.in);
		boolean exit = false;

		do {
			System.out.println(DASHES_SEPARATOR);
			System.out.println("GBCE Exchange Client");
			System.out.println(DASHES_SEPARATOR);
			System.out.println("Choose a number to select function:");
			System.out.println("1: View stock");
			System.out.println("2: View order");
			System.out.println("3: View trade");
			System.out.println("4: Place order");
			System.out.println("5: View GBCE All Share Index");
			System.out.println("6: Exit program");


			int function = sc.nextInt();
			sc.nextLine();

			switch (function) {
			case 1:
				viewStockInfo(sc);
				break;
			case 2:
				viewOrderInfo(sc);
				break;
			case 3:
				viewTradeInfo(sc);
				break;
			case 4:
				placeOrder(sc);
				break;
			case 5:
				viewIndex();
				break;
			case 6:
				exit = true;
				break;

			default:
				break;
			}

		} while (!exit);

		sc.close();
	}

	private static void viewTradeInfo(Scanner sc) {
		System.out.println(DASHES_SEPARATOR);
		System.out.println("View Trade");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter trade id:");
		long tradeId = sc.nextLong();

		ExchangeClient exchange = new ExchangeClient();
		Trade trade = exchange.getTrade(tradeId);

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Trade " + trade.getId());
		System.out.println(DASHES_SEPARATOR);
		Date timestamp = trade.getTimestamp();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println("Date: " + formatter.format(timestamp));
		System.out.println("Stock: " + trade.getStock());
		System.out.println("Price: " + trade.getPrice());
		System.out.println("Quantity: " + trade.getQuantity());
	}

	private static void viewOrderInfo(Scanner sc) {
		System.out.println(DASHES_SEPARATOR);
		System.out.println("View Order");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter order id:");
		BigInteger orderId = sc.nextBigInteger();

		ExchangeClient exchange = new ExchangeClient();
		Order order = exchange.getOrder(orderId);

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Order " + order.getId());
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Type: " + order.getType());
		System.out.println("Stock: " + order.getStock());
		System.out.println("Price: " + order.getPrice());
		System.out.println("Quantity: " + order.getQuantity());

		long tradeId = order.getTrade();
		String tradeOutput = (tradeId != 0) ? String.valueOf(tradeId) : "-";
		System.out.println("Trade: " + tradeOutput);
	}

	private static void viewIndex() {
		ExchangeClient exchange = new ExchangeClient();
		BigDecimal allShareIndex = exchange.getAllShareIndex();

		String pattern = "#####.###";
		DecimalFormat decimalFormat = (DecimalFormat)
				NumberFormat.getNumberInstance(Locale.UK);
		decimalFormat.applyPattern(pattern);

		System.out.println(DASHES_SEPARATOR);
		System.out.println("All Share Index: " + 
				decimalFormat.format(allShareIndex));
		System.out.println(DASHES_SEPARATOR);
	}

	private static void placeOrder(Scanner sc) {
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Placing New Order");
		System.out.println(DASHES_SEPARATOR);

		Stock stock = viewStockInfo(sc);

		Order order = new Order();
		order.setStock(stock.getSymbol());

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Order Type");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter order type:");
		System.out.println("1: Buy");
		System.out.println("2: Sell");
		OrderType orderType = null;
		switch (sc.nextInt()) {
		case 1:
			orderType = OrderType.BUY;
			break;
		case 2:
			orderType = OrderType.SELL;
			break;

		default:
			break;
		}
		order.setType(orderType);

		sc.nextLine();

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Price");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter " + orderType + " price in pennies:");
		BigInteger price = BigInteger.valueOf(Integer.valueOf(sc.nextLine()));
		order.setPrice(price);

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Quantity");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter number of shares to " + orderType + ":");
		BigInteger quantity = BigInteger.valueOf(Integer.valueOf(sc.nextLine()));
		order.setQuantity(quantity);

		ExchangeClient exchange = new ExchangeClient();
		Order placedOrder = exchange.placeOrder(order);

		System.out.println(DASHES_SEPARATOR);
		System.out.println("Placed Order");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Order ID: " + placedOrder.getId());
	}

	private static Stock viewStockInfo(Scanner sc) {
		System.out.println(DASHES_SEPARATOR);
		System.out.println("View Stock");
		System.out.println(DASHES_SEPARATOR);
		System.out.println("Enter stock symbol:");
		String symbol = sc.nextLine();

		ExchangeClient exchange = new ExchangeClient();
		Stock stock = exchange.getStockBySymbol(symbol);

		System.out.println(DASHES_SEPARATOR);
		System.out.println(stock.getSymbol());
		System.out.println(DASHES_SEPARATOR);

		String type = "";
		if (stock instanceof PreferredStock) {
			type = "Preferred";
		} else if (stock instanceof CommonStock) {
			type = "Common";
		}
		System.out.println("Type: " + type);

		NumberFormat priceFormat = NumberFormat.getCurrencyInstance(Locale.UK);
		System.console().writer().println("Price: " + 
				priceFormat.format(penniesToPounds(stock.getPrice())));

		System.console().writer().println("Par Value: " + 
				priceFormat.format(penniesToPounds(stock.getParValue())));

		System.console().writer().println("Last Dividend: " + 
				priceFormat.format(penniesToPounds(stock.getLastDividend())));

		if (stock instanceof PreferredStock) {
			BigDecimal fixedDividend = ((PreferredStock) stock).getFixedDividend();
			BigDecimal percentDividend = fixedDividend.divide(BigDecimal.valueOf(100));
			System.console().writer().println("Fixed Dividend: " + 
					NumberFormat.getPercentInstance(Locale.UK).format(percentDividend));
		}
		System.console().writer().println("Dividend Yield: " + 
				NumberFormat.getInstance().format(stock.calculateDividendYield()));

		String peRatio = "";
		if (stock.calculatePERatio() == null) {
			peRatio = "-";
		} else {
			peRatio = NumberFormat.getInstance().format(stock.calculatePERatio());
		}
		System.console().writer().println("P/E Ratio: " + peRatio);

		return stock;
	}

	private static BigDecimal penniesToPounds(BigDecimal pennies) {
		return pennies.divide(BigDecimal.valueOf(100));
	}

	private static BigDecimal penniesToPounds(BigInteger pennies) {
		BigDecimal decimalPennies = BigDecimal.valueOf(pennies.longValue());
		return penniesToPounds(decimalPennies);
	}

}
