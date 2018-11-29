package cl.transbank.onepay.pos.utils;

public class StringFormatter {
    public static String formatOtt(String ott) {
        String firstPart = ott.substring(0, 4);
        String secondPart = ott.substring(4);

        return firstPart + " - " + secondPart;
    }
}
