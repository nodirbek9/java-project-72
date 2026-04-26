package hexlet.code.dto;
import java.util.List;

public final class UrlsPage {
    private List<UrlWithLastCheck> urls;

    public UrlsPage(List<UrlWithLastCheck> urls) {
        this.urls = urls;
    }

    public List<UrlWithLastCheck> getUrls() {
        return urls;
    }
}
