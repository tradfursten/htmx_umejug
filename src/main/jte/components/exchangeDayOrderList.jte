@import java.math.BigDecimal
@import se.umejug.exchangeday.models.Order
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param boolean displayOrders
@param boolean displaySellers


<span class="numberOfSellers">
            <h3>Number of orders: ${exchangeDay.getOrderList().size()} - total: ${exchangeDay.getSum()}</h3>
    @if(displayOrders)
        <a href="/exchange_days/${exchangeDay.getId()}?display_orders=false&display_sellers=${displaySellers}">(hide)</a>
    @else
        <a href="/exchange_days/${exchangeDay.getId()}?display_orders=true&display_sellers=${displaySellers}">(show)</a>
    @endif
        </span>
<div class="orders">
    @if(displayOrders)
        @for(Order order : exchangeDay.getOrderList())
            <li><a href="/exchange_days/${exchangeDay.getId()}/orders/${order.getId()}">${order.getId()} - ${order.getSum()} kr</a></li>
        @endfor
    @endif
</div>
