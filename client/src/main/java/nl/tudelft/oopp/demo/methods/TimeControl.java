package nl.tudelft.oopp.demo.methods;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeControl {
    /** Returns milliseconds passed since room was opened.
     *
     * @param roomTime LocalDateTime when room was opened
     * @return int milliseconds passed
     */
    public static int getMilisecondsPassed(LocalDateTime roomTime) {
        long temp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - roomTime.toInstant(roomTime.).toEpochMilli();
        return (int)temp;
    }

    public static String getPrettyTime(int milliseconds) {
        int secondsTotal = milliseconds/1000;
        int second = secondsTotal % 60;
        int minute = (secondsTotal / 60) % 60;
        int hour = (secondsTotal /3600) % 100;

        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        return time;
    }


}
