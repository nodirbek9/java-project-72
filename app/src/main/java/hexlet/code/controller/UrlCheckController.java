package hexlet.code.controller;

import hexlet.code.service.UrlCheckService;
import hexlet.code.service.UrlService;
import io.javalin.http.Context;

public final class UrlCheckController {

    private final UrlService urlService;
    private final UrlCheckService urlCheckService;

    public UrlCheckController(UrlService urlService, UrlCheckService urlCheckService) {
        this.urlService = urlService;
        this.urlCheckService = urlCheckService;
    }

    public void create(Context ctx) {
        try {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            var url = urlService.findById(id);

            if (url.isEmpty()) {
                ctx.status(404);
                ctx.result("URL not found");
                return;
            }

            try {
                urlCheckService.performCheck(url.get().getName(), id);
                ctx.sessionAttribute("flash", "Страница успешно проверена");
                ctx.sessionAttribute("flashType", "success");
            } catch (Exception e) {
                ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
                ctx.sessionAttribute("flashType", "danger");
            }

            ctx.redirect("/urls/" + id);
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Internal server error");
        }
    }
}
