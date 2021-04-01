package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.getSpeedHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.sendSpeedHttp;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class SpeedLogic {
    private static final Gson gson = new Gson();

    /** Sends the speed to the server to inform the lecturer.
     *
     * @param speed int that represents users chosen speed
     * @param userId UUID of the user
     * @param roomId UUID of the room
     */
    public static void sendSpeed(int speed, UUID userId, String roomId) {
        HttpResponse<String> response;
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

    /** Gets the speed of current room based on votes by students.
     *
     * @param roomId UUID of the room
     * @return the speed that is calculated on the server. In case of error, return default (2)
     */
    public static int getSpeed(String roomId, String roleId) {
        List<String> sendList = List.of(
                roomId,
                roleId
        );
        String parsedList = gson.toJson(sendList);

        HttpResponse<String> response;
        try {
            response = getSpeedHttp(parsedList);

            if (response.statusCode() != 200) {
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("message").toString());
                return 2; // Default value;
            }
            return Integer.parseInt(response.body());
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
        return 2; // Default value;
    }
}
