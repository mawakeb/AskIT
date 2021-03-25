package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.sendSpeedHttp;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class SpeedLogic {
    private static final Gson gson = new Gson();

    public static void sendSpeed(int speed, UUID userId, String roomId) {
        HttpResponse<String> response = null;
        List<String> sendList = List.of(
                Integer.toString(speed),
                userId.toString(),
                roomId
        );
        String parsedList = gson.toJson(sendList);

        try {
            response = sendSpeedHttp(parsedList);

            if (response.statusCode() != 200) {
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("message").toString());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }
}
