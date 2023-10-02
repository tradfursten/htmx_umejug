@param Long exchangeDayId
@param Long orderId
@param String qr

<div id="qrcode"
     hx-get="/exchange_days/${exchangeDayId}/orders/${orderId}/qr"
     hx-trigger="update-sum from:body">
    $unsafe{qr}
</div>