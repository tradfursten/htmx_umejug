@import se.umejug.exchangeday.Breadcrumb
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param boolean displaySellers
@param boolean displayOrders

@template.layout(title = "Exchange Day: " + exchangeDay.getName(),
    breadcrumb = new Breadcrumb(exchangeDay, null, null),
    content = @`
    <form action="/exchange_days/${exchangeDay.getId()}/orders" method="post">
        <button type="submit">Create new order</button>
    </form>
    <form action="/exchange_days/${exchangeDay.getId()}/sellers?display_sellers=${displaySellers}&display_order=${displayOrders}" method="post">
        <input type="text" name="name" id="name" placeholder="Name"/>
        <input type="text" name="swishNumber" id="swishNumber" placeholder="Swish number"/>
        <button type="submit">Add Seller</button>
    </form>
    <div>
        @template.components.exchangeDaySellerList(exchangeDay = exchangeDay, displaySellers = displaySellers, displayOrders = displayOrders)
    </div>

    <div>
        @template.components.exchangeDayOrderList(exchangeDay = exchangeDay, displaySellers = displaySellers, displayOrders = displayOrders)
    </div>

`)