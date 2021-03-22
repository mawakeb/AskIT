package nl.tudelft.oopp.demo.methods;

import java.time.ZonedDateTime;

public class TimeControl {
    /** Returns milliseconds passed since room was opened.
     *
     * @param roomTime LocalDateTime when room was opened
     * @return int milliseconds passed
     */
    public static int getMilisecondsPassed(ZonedDateTime roomTime) {
        long temp = ZonedDateTime.now().toInstant().toEpochMilli()
                - roomTime.toInstant().toEpochMilli();
        return (int)temp;
    }

}
