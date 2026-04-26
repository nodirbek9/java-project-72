package hexlet.code.dto;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

public final class UrlWithLastCheck {
    private Url url;
    private UrlCheck lastCheck;

    public UrlWithLastCheck(Url url, UrlCheck lastCheck) {
        this.url = url;
        this.lastCheck = lastCheck;
    }

    public Url getUrl() {
        return url;
    }

    public UrlCheck getLastCheck() {
        return lastCheck;
    }
}
