@param se.umejug.exchangeday.models.OrderRow row
@param se.umejug.exchangeday.models.ExchangeDay exchangeDay
@param se.umejug.exchangeday.models.Order order
@param String qr
<tr>
    <td>${row.getSeller().getSellerNumber()}</td>
    <td>${row.getPrice().setScale(2)} kr</td>
</tr>
@template.components.orderSum(exchangeDayId = exchangeDay.getId(), orderId = order.getId(), sum = order.getSum())
@template.components.orderQr(exchangeDayId = exchangeDay.getId(), orderId = order.getId(), qr = qr)

