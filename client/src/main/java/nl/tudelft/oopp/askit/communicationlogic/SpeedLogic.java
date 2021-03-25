package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.sendSpeedHttp;

import java.net.http.HttpResponse;

import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class SpeedLogic {

    public static void sendSpeed(int speed) {
        HttpResponse<String> response = null;
        try {
            response = sendSpeedHttp(Integer.toString(speed));

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
