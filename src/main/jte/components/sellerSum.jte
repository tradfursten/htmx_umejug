@param Long exchangeDayId
@param Long sellerId
@param java.math.BigDecimal sum

<strong id="sum"
        hx-get="/exchange_days/${exchangeDayId}/seller/${sellerId}/sum"
        hx-trigger="update-sum from:body">${sum.setScale(2, java.math.BigDecimal.ROUND_HALF_UP)}</strong> kr
