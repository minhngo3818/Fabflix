package services.movies;

import java.util.Arrays;

public class MoviesUtils {
    public static String formatBooleanKeywords(String keywords) {
        String[] keyList = keywords.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < keyList.length; i++) {
            keyList[i] = "+" + keyList[i];
            keyList[i] += "*";

            stringBuilder.append(keyList[i]);
            if (i < keyList.length - 1)
                stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
