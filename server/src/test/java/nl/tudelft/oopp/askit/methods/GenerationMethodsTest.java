package nl.tudelft.oopp.askit.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GenerationMethodsTest {

    @Test
    public void checkIfRandomStringGetsCreated() {
        String randString = GenerationMethods.randomString();

        assertEquals(randString.length(), 15);
        assertNotNull(randString);
    }
}