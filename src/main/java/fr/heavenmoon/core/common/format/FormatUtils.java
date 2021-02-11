package fr.heavenmoon.core.common.format;

public class FormatUtils {

    public static String graySpacer()
    {
        return "§m                                                     §7";
    }

    public static String medGraySpacer()
    {
        return "§m                                        §7";
    }

    public static String spacesSpacer()
    {
        return "";
    }

    public static String center(String text)
    {
        int maxWidth = 80;
        int length = (int) Math.round((maxWidth - 1.4D * text.length()) / 2.0D);
        String spaces = spaces(length);
        return spaces + text;
    }

    public static String spaces(int length)
    {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            spaces.append(" ");
        }
        return spaces.toString();
    }

}
