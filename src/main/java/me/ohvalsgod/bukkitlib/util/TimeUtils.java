package me.ohvalsgod.bukkitlib.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    // Static utility class -- cannot be created.
    private TimeUtils() {
    }

    /**
     * Delegate to TimeUtils#formatIntoMMSS for backwards compat
     */
    public static String formatIntoHHMMSS(int secs) {
        return formatIntoMMSS(secs);
    }

    /**
     * Formats the time into a format of HH:MM:SS. Example: 3600 (1 hour) displays as '01:00:00'
     *
     * @param secs The input time, in seconds.
     * @return The HH:MM:SS formatted time.
     */
    public static String formatIntoMMSS(int secs) {
        // Calculate the seconds to display:
        int seconds = secs % 60;
        secs -= seconds;

        // Calculate the minutes:
        long minutesCount = secs / 60;
        long minutes = minutesCount % 60;
        minutesCount -= minutes;

        long hours = minutesCount / 60;
        return (hours > 0 ? (hours < 10 ? "0" : "") + hours + ":" : "") + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    /**
     * Formats time into a detailed format. Example: 600 seconds (10 minutes) displays as '10 minutes'
     *
     * @param secs The input time, in seconds.
     * @return The formatted time.
     */
    public static String formatIntoDetailedString(int secs) {
        if (secs == 0) {
            return "0 seconds";
        }
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        String fDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "");
        String fHours = (hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "");
        String fMinutes = (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "");
        String fSeconds = (seconds > 0 ? " " + seconds + " second" + (seconds > 1 ? "s" : "") : "");

        return ((fDays + fHours + fMinutes + fSeconds).trim());
    }

    /**
     * Formats time into a format of MM/dd/yyyy HH:mm.
     *
     * @param date The Date instance to format.
     * @return The formatted time.
     */
    public static String formatIntoCalendarString(Date date) {
        return (dateFormat.format(date));
    }

    /**
     * Parses a string, such as '1h4m25s' into a number of seconds.
     *
     * @param time The string to attempt to parse.
     * @return The number of seconds 'in' the given string.
     */
    public static long parseTime(String time) {
        if (time.equals("0") || time.equals("")) {
            return (0);
        }

        if (time.equalsIgnoreCase("perm") || time.equalsIgnoreCase("permanent")) {
            return 2147483647L;
        }

        String[] lifeMatch = new String[]{"M", "w", "d", "h", "m", "s"};
        int[] lifeInterval = new int[]{604800*4, 604800, 86400, 3600, 60, 1};
        long seconds = 0;

        for (int i = 0; i < lifeMatch.length; i++) {
            Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time);

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }

        return (seconds)*1000;
    }

    public static String formatToLongDetailedString(long millis) {
        long days = TimeUnit.DAYS.toDays(millis);
        long years = days/365;
        days %= 365;
        long months = days/30;
        days %= 30;
        long weeks = days/7;
        days %= 7;

        String output = "";
        output += (years > 0 ? years + " years":"");
        output += (months > 0 ? (output.isEmpty() ? "":", ") + months + " months":"");
        output += (weeks > 0 ? (output.isEmpty() ? "":", ") + weeks + " weeks":"");
        output += (days > 0 ? (output.isEmpty() ? "":", ") + days + "days":"");

        return output;
    }

    /**
     * Gets the seconds between date A and date B. This will never return a negative number.
     *
     * @param a Date A
     * @param b Date B
     * @return The number of seconds between date A and date B.
     */
    public static int getSecondsBetween(Date a, Date b) {
        return (Math.abs((int) (a.getTime() - b.getTime()) / 1000));
    }

}