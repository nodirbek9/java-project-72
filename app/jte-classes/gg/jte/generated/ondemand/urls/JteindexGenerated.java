package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import java.text.SimpleDateFormat;
import io.javalin.http.Context;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,5,5,5,5,8,8,10,10,11,11,12,12,13,13,14,14,14,14,15,15,15,18,18,19,19,20,20,34,34,36,36,36,38,38,38,38,38,38,38,41,41,42,42,42,43,43,47,47,50,50,50,50,50,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page, Context ctx) {
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
				jteOutput.writeContent("\n\n    <h1>Сайты</h1>\n\n    <table class=\"table table-striped\" data-test=\"urls\">\n        <thead>\n        <tr>\n            <th>ID</th>\n            <th>Имя</th>\n            <th>Последняя проверка</th>\n            <th>Код ответа</th>\n        </tr>\n        </thead>\n        <tbody>\n        ");
				for (Url url : page.getUrls()) {
					jteOutput.writeContent("\n            <tr>\n                <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("</td>\n                <td>\n                    <a href=\"/urls/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(url.getId());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\n                </td>\n                <td>\n                    ");
					if (url.getCreatedAt() != null) {
						jteOutput.writeContent("\n                        ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(url.getCreatedAt()));
						jteOutput.writeContent("\n                    ");
					}
					jteOutput.writeContent("\n                </td>\n                <td></td>\n            </tr>\n        ");
				}
				jteOutput.writeContent("\n        </tbody>\n    </table>\n");
			}
		}, null);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		Context ctx = (Context)params.getOrDefault("ctx", null);
		render(jteOutput, jteHtmlInterceptor, page, ctx);
	}
}
