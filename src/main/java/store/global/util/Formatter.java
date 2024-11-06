package store.global.util;

import java.text.DecimalFormat;

public class Formatter {

    public static String formatNumber(int input) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(input);
    }

}
