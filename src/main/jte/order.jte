@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param se.umejug.exchangeday.models.Order order
@param java.math.BigDecimal sum
@param String qr

@template.layout(title = exchangeDay.getName(), content = @`
    <h1>New order: ${exchangeDay.getName()}</h1>
    <form action="/exchange_days/${exchangeDay.getId()}/orders/${order.getId()}/rows" method="post">
        <input type="number" name="sellerId" id="sellerId" placeholder="Seller id"/>
        <input type="number" name="price" id="price" placeholder="Price"/>
        <button type="submit">Add row</button>
    </form>

    <table>
        <thead>
            <tr>
                <td>Seller</td>
                <td>Price</td>
            </tr>
        </thead>
        <tbody>
        @for(se.umejug.exchangeday.models.OrderRow row : order.getOrderRows())
            <tr>
                <td>${row.getSeller().getId()}</td>
                <td>${row.getPrice()} kr</td>
            </tr>
        @endfor
        </tbody>
        <tfoot>
        <tr>
            <td />
            <td>
                <strong id="sum">${sum}</strong> kr
            </td>
        </tr>
        </tfoot>
    </table>
    <div id="qrcode" hx-get="/orders/qr" hx-trigger="update-sum from:body">
            $unsafe{qr}
    </div>
`)