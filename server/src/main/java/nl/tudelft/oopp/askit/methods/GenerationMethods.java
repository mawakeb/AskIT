package nl.tudelft.oopp.askit.methods;

import java.util.Random;

public class GenerationMethods {
    /**
     * generate random String to use as room s codes.
     *
     * @return String code of 15 characters.
     */
    public static String randomString() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        char[] characters = ("abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").toCharArray();

        for (int i = 0; i < 15; ++i) {
            int randomInt = random.nextInt(characters.length);
            result.append(characters[randomInt]);
        }

        return result.toString();
    }
}
