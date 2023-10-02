@import se.umejug.exchangeday.models.ExchangeDay
@import se.umejug.exchangeday.Breadcrumb
@param java.util.List<ExchangeDay> exchangeDays

@template.layout(title = "Exchange Day!",
    breadcrumb = new Breadcrumb(null, null, null),
    content = @`
    <form action="/exchange_days" method="post">
        <input type="text" name="name" id="name" placeholder="Name"/>
        <input type="text" name="swishNumber" id="swishNumber" placeholder="Swish number"/>
        <button type="submit">Create exchangeday</button>
    </form>

    <ul>
        @for(ExchangeDay exchangeDay : exchangeDays)
            <li>@template.components.exchangeDayLink(exchangeDay = exchangeDay)</li>
        @endfor
    </ul>
`)



