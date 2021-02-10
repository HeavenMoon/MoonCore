package fr.heavenmoon.core.common.utils.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    public static String getArgumentsMin(String[] args, int startIndex) {
        if (startIndex < args.length) {
            String compiled = "";
            for (int i = startIndex; i < args.length; ++i) {
                compiled = compiled + args[i];
                compiled = compiled + " ";
            }
            return compiled.trim();
        }
        return null;
    }

    public static String getArgumentsMax(String[] args, int stopIndex) {
        if (stopIndex < args.length) {
            String compiled = "";
            for (int i = 0; i <= stopIndex; ++i) {
                compiled = compiled + args[i];
                if (i < stopIndex) {
                    compiled = compiled + " ";
                }
            }
            return compiled.trim();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<String> addArrays(List<String>... arrays) {
        List<String> finalArray = new ArrayList<String>();
        Arrays.asList(arrays).forEach(array -> {
            array.forEach(element -> {
                finalArray.add(element);
            });
        });
        return finalArray;
    }

}
