package mx.iteso.petgo.utils;

import java.util.Calendar;
import java.util.Date;

public class Parser {
    public static String parseFromDate(Date date) {
        String dateString = getYear(date) + "-";
        dateString += getMonth(date) + "-";
        dateString += getDay(date) + " ";
        dateString += getHours(date) + ":";
        dateString += getMinutes(date);
        return dateString;
    }

    private static String getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    private static String getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.MONTH));
    }

    private static String getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static String getHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    private static String getMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.MINUTE));
    }

}
