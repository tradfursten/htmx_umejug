package se.umejug.exchangeday;

import se.umejug.exchangeday.models.ExchangeDay;
import se.umejug.exchangeday.models.Order;
import se.umejug.exchangeday.models.Seller;

public record Breadcrumb(ExchangeDay exchangeDay, Order order, Seller seller) {
}
