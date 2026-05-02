package hexlet.code.controller;

import hexlet.code.service.UrlCheckService;
import hexlet.code.service.UrlService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public final class UrlCheckController {

    private final UrlService urlService;
    private final UrlCheckService urlCheckService;

    public UrlCheckController(UrlService urlService, UrlCheckService urlCheckService) {
        this.urlService = urlService;
        this.urlCheckService = urlCheckService;
    }

    public void create(Context ctx) throws Exception {
        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        if (id == null) {
            throw new BadRequestResponse("Invalid ID");
        }

        var url = urlService.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));

        try {
            urlCheckService.performCheck(url.getName(), url.getId());
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashType", "success");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flashType", "danger");
        }

        ctx.redirect("/urls/" + id);
    }
}
