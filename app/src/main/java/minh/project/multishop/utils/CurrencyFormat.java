package minh.project.multishop.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    public static String currencyFormat(int price){
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(price);
    }
}
