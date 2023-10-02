@param Long exchangeDayId
@param Long orderId
@param java.math.BigDecimal sum

<strong id="sum"
        hx-get="/exchange_days/${exchangeDayId}/orders/${orderId}/sum"
        hx-trigger="update-sum from:body">${sum.setScale(2, java.math.BigDecimal.ROUND_HALF_UP)}</strong> kr
