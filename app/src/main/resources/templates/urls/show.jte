@import hexlet.code.dto.UrlPage
@import java.text.SimpleDateFormat
@import io.javalin.http.Context

@param UrlPage page
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

    <h1>Сайт: ${page.getUrl().getName()}</h1>

    <table class="table table-bordered" data-test="url">
        <tbody>
        <tr>
            <td>ID</td>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <td>Имя</td>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <td>Дата создания</td>
            <td>
                @if(page.getUrl().getCreatedAt() != null)
                    ${new SimpleDateFormat("dd.MM.yyyy HH:mm").format(page.getUrl().getCreatedAt())}
                @endif
            </td>
        </tr>
        </tbody>
    </table>

    <h2 class="mt-5">Проверки</h2>
    <form action="/urls/${page.getUrl().getId()}/checks" method="post">
        <button type="submit" class="btn btn-primary">Запустить проверку</button>
    </form>
`
)