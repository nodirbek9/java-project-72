@import hexlet.code.dto.UrlsPage
@import hexlet.code.model.Url
@import java.text.SimpleDateFormat
@import io.javalin.http.Context

@param UrlsPage page
@param Context ctx = null

@template.layout.page(
content = @`
    @if(ctx != null && ctx.sessionAttribute("flash") != null)
        !{String flash = ctx.sessionAttribute("flash");}
        !{String flashType = ctx.sessionAttribute("flashType");}
        <div class="alert alert-${flashType} alert-dismissible fade show" role="alert">
            ${flash}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        !{ctx.consumeSessionAttribute("flash");}
        !{ctx.consumeSessionAttribute("flashType");}
    @endif

    <h1>Сайты</h1>

    <table class="table table-striped" data-test="urls">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Последняя проверка</th>
            <th>Код ответа</th>
        </tr>
        </thead>
        <tbody>
        @for(Url url : page.getUrls())
            <tr>
                <td>${url.getId()}</td>
                <td>
                    <a href="/urls/${url.getId()}">${url.getName()}</a>
                </td>
                <td>
                    @if(url.getCreatedAt() != null)
                        ${new SimpleDateFormat("dd.MM.yyyy HH:mm").format(url.getCreatedAt())}
                    @endif
                </td>
                <td></td>
            </tr>
        @endfor
        </tbody>
    </table>
`
)