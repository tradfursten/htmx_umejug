@import se.umejug.exchangeday.Breadcrumb
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param se.umejug.exchangeday.models.Order order
@param java.math.BigDecimal sum
@param String qr

@template.layout(title = exchangeDay.getName(),
    breadcrumb = new Breadcrumb(exchangeDay, order, null),
    content = @`
    <form action="/exchange_days/${exchangeDay.getId()}/orders/${order.getId()}/rows"
          method="post"
          hx-post="/exchange_days/${exchangeDay.getId()}/orders/${order.getId()}/rows"
          hx-target="#order-list"
          hx-swap="beforeend" >
        <input type="number" name="sellerId" id="sellerId" placeholder="Seller id"/>
        <input type="number" name="price" id="price" placeholder="Price"/>
        <button type="submit">Add row</button>
    </form>

    <table>
        <thead>
            <tr>
                <th>Seller</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody id="order-list">
        @for(se.umejug.exchangeday.models.OrderRow row : order.getOrderRows())
            @template.components.orderRow(row = row)
        @endfor
        </tbody>
        <tfoot>
        <tr>
            <td />
            <td>
                @template.components.orderSum(exchangeDayId = exchangeDay.getId(), orderId = order.getId(), sum = sum)
            </td>
        </tr>
        </tfoot>
    </table>

    @template.components.orderQr(exchangeDayId = exchangeDay.getId(), orderId = order.getId(), qr = qr)
`)