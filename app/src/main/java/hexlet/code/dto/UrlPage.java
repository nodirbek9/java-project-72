package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.util.List;

public final class UrlPage {
    private Url url;
    private List<UrlCheck> urlChecks;

    public UrlPage(Url url, List<UrlCheck> urlChecks) {
        this.url = url;
        this.urlChecks = urlChecks;
    }

    public Url getUrl() {
        return url;
    }

    public List<UrlCheck> getUrlChecks() {
        return urlChecks;
    }
}
