package store.global.util;

import java.text.DecimalFormat;

public class Formatter {

    private static final String GREETING_HEADER = "==============W 편의점================";

    public static String formatNumber(int input) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(input);
    }

}
