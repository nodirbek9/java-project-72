package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.UrlPage;
import java.text.SimpleDateFormat;
import io.javalin.http.Context;
@SuppressWarnings("unchecked")
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,7,7,9,9,10,10,11,11,12,12,13,13,13,13,14,14,14,17,17,18,18,19,19,21,21,21,27,27,27,31,31,31,36,36,37,37,37,38,38,45,45,45,45,61,61,63,63,63,64,64,64,65,65,65,66,66,66,67,67,67,69,69,70,70,70,71,71,74,74,77,77,77,77,77,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlPage page, Context ctx) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    ");
				if (ctx != null && ctx.sessionAttribute("flash") != null) {
					jteOutput.writeContent("\n        ");
					String flash = ctx.sessionAttribute("flash");
					jteOutput.writeContent("\n        ");
					String flashType = ctx.sessionAttribute("flashType");
					jteOutput.writeContent("\n        <div class=\"alert alert-");
					jteOutput.setContext("div", "class");
					jteOutput.writeUserContent(flashType);
					jteOutput.setContext("div", null);
					jteOutput.writeContent(" alert-dismissible fade show\" role=\"alert\">\n            ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(flash);
					jteOutput.writeContent("\n            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\"></button>\n        </div>\n        ");
					ctx.consumeSessionAttribute("flash");
					jteOutput.writeContent("\n        ");
					ctx.consumeSessionAttribute("flashType");
					jteOutput.writeContent("\n    ");
				}
				jteOutput.writeContent("\n\n    <h1>Сайт: ");
				jteOutput.setContext("h1", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("</h1>\n\n    <table class=\"table table-bordered\" data-test=\"url\">\n        <tbody>\n        <tr>\n            <td>ID</td>\n            <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getId());
				jteOutput.writeContent("</td>\n        </tr>\n        <tr>\n            <td>Имя</td>\n            <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("</td>\n        </tr>\n        <tr>\n            <td>Дата создания</td>\n            <td>\n                ");
				if (page.getUrl().getCreatedAt() != null) {
					jteOutput.writeContent("\n                    ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(page.getUrl().getCreatedAt()));
					jteOutput.writeContent("\n                ");
				}
				jteOutput.writeContent("\n            </td>\n        </tr>\n        </tbody>\n    </table>\n\n    <h2 class=\"mt-5\">Проверки</h2>\n    <form action=\"/urls/");
				jteOutput.setContext("form", "action");
				jteOutput.writeUserContent(page.getUrl().getId());
				jteOutput.setContext("form", null);
				jteOutput.writeContent("/checks\" method=\"post\">\n        <button type=\"submit\" class=\"btn btn-primary\">Запустить проверку</button>\n    </form>\n\n    <table class=\"table table-bordered table-hover mt-3\" data-test=\"checks\">\n        <thead>\n        <tr>\n            <th>ID</th>\n            <th>Код ответа</th>\n            <th>title</th>\n            <th>h1</th>\n            <th>description</th>\n            <th>Дата проверки</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
				for (var check : page.getUrlChecks()) {
					jteOutput.writeContent("\n            <tr>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getId());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getStatusCode());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getTitle());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getH1());
					jteOutput.writeContent("</td>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getDescription());
					jteOutput.writeContent("</td>\n                <td>\n                    ");
					if (check.getCreatedAt() != null) {
						jteOutput.writeContent("\n                        ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(check.getCreatedAt()));
						jteOutput.writeContent("\n                    ");
					}
					jteOutput.writeContent("\n                </td>\n            </tr>\n        ");
				}
				jteOutput.writeContent("\n        </tbody>\n    </table>\n");
			}
		}, null);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlPage page = (UrlPage)params.get("page");
		Context ctx = (Context)params.getOrDefault("ctx", null);
		render(jteOutput, jteHtmlInterceptor, page, ctx);
	}
}
