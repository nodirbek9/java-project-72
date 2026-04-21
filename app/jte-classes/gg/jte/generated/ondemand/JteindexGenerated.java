package gg.jte.generated.ondemand;
import io.javalin.http.Context;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,3,3,5,5,6,6,7,7,8,8,9,9,9,9,10,10,10,13,13,14,14,15,15,40,40,40,40,40,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx) {
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
				jteOutput.writeContent("\n\n    <div class=\"row\">\n        <div class=\"col-12 col-md-10 col-lg-8 mx-auto border rounded-3 bg-light p-5\">\n            <h1 class=\"display-3\">Анализатор страниц</h1>\n            <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\n            <form action=\"/urls\" method=\"post\" class=\"row\">\n                <div class=\"col-8\">\n                    <label for=\"url-name\" class=\"visually-hidden\">Url для проверки</label>\n                    <input\n                            id=\"url-name\"\n                            type=\"text\"\n                            name=\"url\"\n                            class=\"form-control form-control-lg\"\n                            placeholder=\"https://www.example.com\"\n                            required\n                    >\n                </div>\n                <div class=\"col-2\">\n                    <input type=\"submit\" class=\"btn btn-primary btn-lg ms-3 px-5 text-uppercase mx-3\"\n                           value=\"Проверить\">\n                </div>\n            </form>\n        </div>\n    </div>\n");
			}
		}, null);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.getOrDefault("ctx", null);
		render(jteOutput, jteHtmlInterceptor, ctx);
	}
}
