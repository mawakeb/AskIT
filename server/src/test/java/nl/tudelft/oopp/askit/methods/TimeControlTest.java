package nl.tudelft.oopp.askit.methods;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class TimeControlTest {

    @Test
    void getMilisecondsPassed() {
        int mili = TimeControl.getMilisecondsPassed(ZonedDateTime.now());
        assertTrue(mili < 10000);
    }
}