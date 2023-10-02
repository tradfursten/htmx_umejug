@param Long exchangeDayId
@param Long sellerId
@param String qr

<div id="qrcode"
     hx-get="/exchange_days/${exchangeDayId}/seller/${sellerId}/qr"
     hx-trigger="update-sum from:body">
    $unsafe{qr}
</div>