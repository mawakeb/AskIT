package nl.tudelft.oopp.askit.methods;

import java.util.ArrayList;
import java.util.List;

public class SpeedMethods {

    /**
     * Generates the list for speeds.
     *
     * @return list of speeds with initial 0 values.
     */
    public static List<Integer> generateList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(0);
        }
        return list;
    }

    /**
     * Calculates the speed that occurs the most times.
     *
     * @param list list containing speed amounts
     * @return from 0 to 4. In case of a tie, should return the highest speed
     */
    public static int getBiggestSpeedIndex(List<Integer> list) {
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) >= max) {
                max = list.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Calculates the room speed based on the weight. Speed is from 0 - 4
     * It returns the biggest speed, if there is a tie, it returns the "fastest" speed
     *
     * @param size   amount of students
     * @param weight percentage threshold that needs to be exceeded
     * @param speeds List containing all the speed amounts
     * @return the new speed, if it passes the weight threshold, otherwise the default value (2)
     */
    public static int roomSpeedWeighted(int size, double weight, List<Integer> speeds) {
        int biggestSpeedIndex = getBiggestSpeedIndex(speeds); // Index of a speed from 0 to 4
        int biggestSpeedSize =
                speeds.get(biggestSpeedIndex); // How much people voted for that speed
        double percentage = (double) biggestSpeedSize / (double) size; // relative amount of votes
        // Checks if percentage is at least bigger than the set weight
        if (percentage > weight) {
            return biggestSpeedIndex;
        }
        // Returns default
        return 2;
    }
}
