package me.kotisis.stocks;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.kotisis.stocks.model.Order;
import me.kotisis.stocks.model.Stock;
import me.kotisis.stocks.model.Trade;

@Named
@Path("/exchange")
@Produces({MediaType.APPLICATION_JSON})
public class ExchangeService {

	final static Logger log = LoggerFactory.getLogger(ExchangeService.class);

	@Context
	private UriInfo uriInfo;

	@Inject
	private Exchange exchange;

	@Inject
	private OrderLog orderLog;

	@Inject
	private TradeLog tradeLog;

	@GET
	@Path("stock/{symbol}")
	public StockDecorator getStock(@PathParam("symbol") String symbol) {
		if (symbol == null || symbol.isEmpty()) {
			throw new BadRequestException();
		}

		log.info("Retrieving stock with symbol " + symbol);

		Stock stock = exchange.getStockBySymbol(symbol);

		if (stock == null) {
			throw new NotFoundException();
		}

		return new StockDecorator(stock);
	}

	@POST
	@Path("order")
	public Response placeOrder(Order order) {
		if (order == null) {
			throw new BadRequestException();
		}

		try {
			String serializedOrder = new ObjectMapper().writeValueAsString(order);
			log.info("Placing order " + serializedOrder);
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage(), e);
		}

		Order placedOrder = exchange.placeOrder(order);
		URI orderUri = uriInfo.getAbsolutePathBuilder().path(placedOrder.getId().toString()).build();

		log.info("Placed order URI: " + orderUri);

		return Response.created(orderUri).build();
	}

	@GET
	@Path("order/{id}")
	public Response getOrder(@PathParam("id") BigInteger id) {
		if (id == null) {
			throw new BadRequestException();
		}

		log.info("Retrieving order " + id);

		Order order = orderLog.get(id);

		if (order == null) {
			throw new NotFoundException();
		}

		return Response.ok(order).build();
	}

	@GET
	@Path("trade/{id}")
	public Response getTrade(@PathParam("id") Long id) {
		if (id == null) {
			throw new BadRequestException();
		}

		log.info("Retrieving trade " + id);

		Trade trade = tradeLog.getById(id);

		if (trade == null) {
			throw new NotFoundException();
		}

		return Response.ok(trade).build();
	}

	@GET
	@Path("trade")
	public Response getTrades(@QueryParam("stock") String stockSymbol) {
		log.info("Retrieving trades for stock: " + stockSymbol);

		List<Trade> trades = tradeLog.getTrades(stockSymbol, null);

		if (trades == null) {
			throw new NotFoundException();
		}

		return Response.ok(trades).build();
	}

	@GET
	@Path("index")
	public Response getIndex() {
		log.info("Retrieving All Share Index...");

		BigDecimal allShareIndex = exchange.getAllShareIndex();

		return Response.ok(allShareIndex).build();
	}

	/*@GET
	@Path("trades")
	public Response getTradeByOrder(@QueryParam("order") BigInteger orderId) {
		if (orderId == null) {
			throw new BadRequestException();
		}

		log.info("Retrieving trade for order " + orderId);

		
	}*/
}
