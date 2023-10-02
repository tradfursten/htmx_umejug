@param se.umejug.exchangeday.models.Order order
@param se.umejug.exchangeday.models.Seller seller
@param Long exchangeDayId

    <tr>
        <td>
            <a href="/exchange_days/${exchangeDayId}/orders/${order.getId()}">${order.getId()}</a>
        </td>
        <td>${order.getSumBySeller(seller)} kr</td>
    </tr>
