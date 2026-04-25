package hexlet.code.util;

import java.net.URI;

public class UrlNormalizer {

    public static String normalize(String inputUrl) throws Exception {
        var uri = new URI(inputUrl);
        var url = uri.toURL();

        String normalizedUrl = url.getProtocol() + "://" + url.getHost();
        if (url.getPort() != -1) {
            normalizedUrl += ":" + url.getPort();
        }

        return normalizedUrl;
    }
}
