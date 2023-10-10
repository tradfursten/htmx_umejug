@import gg.jte.Content

@param String title
@param Content content
@param se.umejug.exchangeday.Breadcrumb breadcrumb

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="/main.css" rel="stylesheet">
    <script src="https://unpkg.com/htmx.org@1.9.6" integrity="sha384-FhXw7b6AlE/jyjlZH5iHa/tTe9EpJ1Y55RjcgPbjeWMskSxZt1v9qkxLJWNJaGni" crossorigin="anonymous"></script>
    <meta name="htmx-config" content='{"useTemplateFragments":"true"}'>
</head>

<body>
@template.components.breadcrumbs(breadcrumb = breadcrumb)
<h1>${title}</h1>
<article>${content}</article>
</body>
</html>