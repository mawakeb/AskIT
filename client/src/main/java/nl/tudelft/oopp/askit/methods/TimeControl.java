package nl.tudelft.oopp.askit.methods;

import java.time.ZonedDateTime;

public class TimeControl {

    /**
     * Returns milliseconds passed since room was opened.
     *
     * @param roomTime LocalDateTime when room was opened
     * @return int milliseconds passed
     */
    public static int getMilisecondsPassed(ZonedDateTime roomTime) {
        long temp = ZonedDateTime.now().toInstant().toEpochMilli()
                - roomTime.toInstant().toEpochMilli();
        return (int) temp;
    }

    /**
     * Translates milliseconds into readable time.
     *
     * @param milliseconds int - time to translate
     * @return String in format of HH:MM:SS
     */
    public static String getPrettyTime(int milliseconds) {
        int secondsTotal = milliseconds / 1000;
        int second = secondsTotal % 60;
        int minute = (secondsTotal / 60) % 60;
        int hour = (secondsTotal / 3600) % 100;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }


}
