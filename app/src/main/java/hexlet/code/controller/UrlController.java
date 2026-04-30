package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlWithLastCheck;
import hexlet.code.dto.UrlsPage;
import hexlet.code.service.UrlCheckService;
import hexlet.code.service.UrlService;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public final class UrlController {

    private final UrlService urlService;
    private final UrlCheckService urlCheckService;

    public UrlController(UrlService urlService, UrlCheckService urlCheckService) {
        this.urlService = urlService;
        this.urlCheckService = urlCheckService;
    }

    public void index(Context ctx) {
        ctx.render("index.jte", Collections.singletonMap("ctx", ctx));
    }

    public void create(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url");
        String normalizedUrl;

        try {
            normalizedUrl = urlService.normalizeUrl(inputUrl);
        } catch (Exception e) {
            ctx.status(422);
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.render("index.jte", Collections.singletonMap("ctx", ctx));
            return;
        }

        var existingUrl = urlService.findByName(normalizedUrl);

        if (existingUrl.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "info");
            ctx.redirect("/urls/" + existingUrl.orElseThrow().getId());
        } else {
            var newUrl = urlService.save(normalizedUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect("/urls/" + newUrl.getId());
        }
    }

    public void list(Context ctx) throws SQLException {
        var urls = urlService.findAll();
        var urlsWithChecks = new ArrayList<UrlWithLastCheck>();

        for (var url : urls) {
            var lastCheck = urlCheckService.findLastCheckByUrlId(url.getId());
            urlsWithChecks.add(new UrlWithLastCheck(url, lastCheck));
        }

        var page = new UrlsPage(urlsWithChecks);
        var model = new HashMap<String, Object>();
        model.put("page", page);
        model.put("ctx", ctx);
        ctx.render("urls/index.jte", model);
    }

    public void show(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        if (id == null) {
            ctx.status(400);
            ctx.result("Invalid ID");
            return;
        }

        var url = urlService.findById(id)
            .orElseThrow(() -> new RuntimeException("URL not found with id: " + id));

        var checks = urlCheckService.findByUrlId(id);
        var page = new UrlPage(url, checks);
        var model = new HashMap<String, Object>();
        model.put("page", page);
        model.put("ctx", ctx);
        ctx.render("urls/show.jte", model);
    }
}
