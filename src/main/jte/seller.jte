@import se.umejug.exchangeday.Breadcrumb
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param se.umejug.exchangeday.models.Seller seller
@param java.math.BigDecimal sum
@param String qr

@template.layout(title = "Seller: " + seller.getName(),
    breadcrumb = new Breadcrumb(exchangeDay, null, seller),
    content = @`
    <table>
        <thead>
            <tr>
                <th>Order</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody id="order-list">
        @for(se.umejug.exchangeday.models.Order order : exchangeDay.findOrdersBySeller(seller))
            @template.components.sellerOrder(order = order, seller = seller, exchangeDayId = exchangeDay.getId())
        @endfor
        </tbody>
        <tfoot>
        <tr>
            <td />
            <td>
                @template.components.sellerSum(exchangeDayId = exchangeDay.getId(), sellerId = seller.getId(), sum = sum)
            </td>
        </tr>
        </tfoot>
    </table>

    @template.components.sellerQr(exchangeDayId = exchangeDay.getId(), sellerId = seller.getId(), qr = qr)
`)