package nl.tudelft.oopp.askit.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class SerializingControl {

    /** Create modified Gson object for creating and parsing JSON strings.
     * With added support for writing/reading ZonedDateTime (not possible by default)
     * Uses code from:
     * https://stackoverflow.com/questions/36408192/converting-zoneddatetime-type-to-gson
     * @return the Gson object
     */
    public static Gson getGsonObject() {
        return new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
                    @Override
                    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                        out.value(value.toString());
                    }

                    @Override
                    public ZonedDateTime read(JsonReader in) throws IOException {
                        return ZonedDateTime.parse(in.nextString());
                    }
                })
                .enableComplexMapKeySerialization()
                .create();
    }
}
