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
        long temp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - roomTime.toInstant(ZoneOffset.ofHours(0)).toEpochMilli();
        return (int)temp;
    }

}
