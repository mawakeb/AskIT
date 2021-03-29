package nl.tudelft.oopp.askit.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class SerializingControlTest {

    @Test
    void getGsonObject() {
        assertNotNull(SerializingControl.getGsonObject());
    }

    @Test
    void testZonedDateTime() {
        ZonedDateTime testDateTime = ZonedDateTime.now();

        // turn into json and back again
        Gson gson = SerializingControl.getGsonObject();
        String asString = gson.toJson(testDateTime);
        ZonedDateTime asObject = gson.fromJson(asString, ZonedDateTime.class);

        assertEquals(testDateTime, asObject);
    }
}