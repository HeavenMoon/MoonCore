package fr.heavenmoon.core.common.format;

public class FormatUtils {

    public static String graySpacer() {
        return "-----------------------------------------------------";
    }

    public static String spacesSpacer() {
        return "";
    }

    public static String center(String text) {
        int maxWidth = 80;
        int length = (int) Math.round((maxWidth - 1.4 * text.length()) / 2);
        String spaces = spaces(length);
        return spaces + text;
    }

    public static String spaces(int length) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < length; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

}
