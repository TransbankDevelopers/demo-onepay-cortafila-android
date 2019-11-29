package cl.transbank.onepay.pos.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyFormat {

    static NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));

    public static String formatBigDecimalToCurrency(BigDecimal amount) {
        return currency.format(amount);
    }

    public static BigDecimal parse(final String amount) throws ParseException {

        if (currency instanceof DecimalFormat) {
            ((DecimalFormat) currency).setParseBigDecimal(true);
        }
        return (BigDecimal) currency.parse(amount.replaceAll("^\\d.,",""));
    }
}
