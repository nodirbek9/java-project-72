package hexlet.code.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParser {

    public static ParsedHtml parse(String html) {
        Document doc = Jsoup.parse(html);

        String title = doc.title();
        var h1Element = doc.selectFirst("h1");
        String h1 = h1Element != null ? h1Element.text() : "";
        var descriptionElement = doc.selectFirst("meta[name=description]");
        String description = descriptionElement != null ? descriptionElement.attr("content") : "";

        return new ParsedHtml(
                StringTruncator.truncate(title),
                StringTruncator.truncate(h1),
                StringTruncator.truncate(description)
        );
    }

    public record ParsedHtml(String title, String h1, String description) {
    }
}
