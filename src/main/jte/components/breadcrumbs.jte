@param se.umejug.exchangeday.Breadcrumb breadcrumb

<nav>
    <ul class="breadcrumb">
        <li><a href="/">Home</a></li>
       @if(breadcrumb.exchangeDay() != null)
           <li><a href="/exchange_days/${breadcrumb.exchangeDay().getId()}">${breadcrumb.exchangeDay().getName()}</a></li>
       @endif
    </ul>

</nav>
