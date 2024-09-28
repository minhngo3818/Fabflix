package utils;

public class ParseNumber {

    public static Integer parseInt(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double parseDouble(String integer) {
        try {
            return Double.parseDouble(integer);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
