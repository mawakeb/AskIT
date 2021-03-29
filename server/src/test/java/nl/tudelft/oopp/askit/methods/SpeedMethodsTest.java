package nl.tudelft.oopp.askit.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SpeedMethodsTest {

    @Test
    void generateList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(0);
        }

        assertEquals(list, SpeedMethods.generateList());
    }

    @Test
    void getBiggestSpeedIndex() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(0);
        }
        list.add(2);

        // Should return the index of highest speed
        assertEquals(SpeedMethods.getBiggestSpeedIndex(list), 4);
    }

    @Test
    void roomSpeedWeighted() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(0);
        }
        list.add(1);

        // Should return the highest speed
        int weighted = SpeedMethods.roomSpeedWeighted(4, 0.1, list);

        assertEquals(4, weighted);

        // Should return the default value (2)
        int thresholdNotPassed = SpeedMethods.roomSpeedWeighted(11, 0.1, list);

        assertEquals(2, thresholdNotPassed);
    }
}