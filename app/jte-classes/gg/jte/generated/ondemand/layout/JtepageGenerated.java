package gg.jte.generated.ondemand.layout;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtepageGenerated {
	public static final String JTE_NAME = "layout/page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,35,35,35,35,42,42,43,43,43,44,44,46,46,53,53,53,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content, Content footer) {
		jteOutput.writeContent("\n  <!DOCTYPE html>\n  <html lang=\"ru\">\n  <head>\n      <meta charset=\"UTF-8\">\n      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n      <title>Анализатор страниц</title>\n      <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n  </head>\n  <body class=\"d-flex flex-column min-vh-100\">\n      <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n          <div class=\"container-fluid\">\n              <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\n              <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\">\n                  <span class=\"navbar-toggler-icon\"></span>\n              </button>\n              <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n                  <ul class=\"navbar-nav\">\n                      <li class=\"nav-item\">\n                          <a class=\"nav-link\" href=\"/\">Главная</a>\n                      </li>\n                      <li class=\"nav-item\">\n                          <a class=\"nav-link\" href=\"/urls\">Сайты</a>\n                      </li>\n                  </ul>\n              </div>\n          </div>\n      </nav>\n\n      <main class=\"flex-grow-1\">\n          <div class=\"container-lg mt-5\">\n              ");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\n          </div>\n      </main>\n\n      <footer class=\"footer mt-auto py-3 bg-light\">\n          <div class=\"container-lg\">\n              <span class=\"text-muted\">\n                  ");
		if (footer != null) {
			jteOutput.writeContent("\n                      ");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(footer);
			jteOutput.writeContent("\n                  ");
		} else {
			jteOutput.writeContent("\n                      created by <a href=\"https://github.com/nodirbek9\" target=\"_blank\">Nodirbek</a>\n                  ");
		}
		jteOutput.writeContent("\n              </span>\n          </div>\n      </footer>\n\n      <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>\n  </body>\n  </html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		Content footer = (Content)params.getOrDefault("footer", null);
		render(jteOutput, jteHtmlInterceptor, content, footer);
	}
}
