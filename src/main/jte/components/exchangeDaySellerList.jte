@import se.umejug.exchangeday.models.Seller
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param boolean displaySellers
@param boolean displayOrders


<span class="numberOfSellers">
            <h3>Number of sellers: ${exchangeDay.getSellerList().size()}</h3>
    @if(displaySellers)
        <a href="/exchange_days/${exchangeDay.getId()}?display_sellers=false&display_orders=${displayOrders}">(hide)</a>
    @else
        <a href="/exchange_days/${exchangeDay.getId()}?display_sellers=true&display_orders=${displayOrders}">(show)</a>
    @endif
        </span>
<div class="sellers">
    @if(displaySellers)
        @for(Seller seller : exchangeDay.getSellerList())
            <li><a href="/exchange_days/${exchangeDay.getId()}/sellers/${seller.getId()}">${seller.getSellerNumber()} - ${seller.getName()}</a></li>
        @endfor
    @endif
</div>
