package hexlet.code.util;

public class StringTruncator {

    private static final int MAX_LENGTH = 200;
    private static final String SUFFIX = "...";

    public static String truncate(String text) {
        if (text == null || text.length() <= MAX_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_LENGTH) + SUFFIX;
    }
}
