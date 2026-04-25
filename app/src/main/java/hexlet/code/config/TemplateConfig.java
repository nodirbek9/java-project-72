package hexlet.code.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

public class TemplateConfig {

    public static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = TemplateConfig.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
