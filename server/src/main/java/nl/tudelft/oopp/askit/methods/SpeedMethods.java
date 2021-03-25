package nl.tudelft.oopp.askit.methods;

import java.util.ArrayList;
import java.util.List;

public class SpeedMethods {
    public static List<Integer> generateList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(0);
        }
        return list;
    }
}
