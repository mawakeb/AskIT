package nl.tudelft.oopp.demo.methods;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class TimeControlTest {

    @Test
    void getMilisecondsPassed() {
        TimeControl.getMilisecondsPassed(LocalDateTime.now(ZoneOffset.UTC));
    }
}