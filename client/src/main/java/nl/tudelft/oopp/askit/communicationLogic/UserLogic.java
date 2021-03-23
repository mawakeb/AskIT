package nl.tudelft.oopp.askit.communicationLogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.banUserHttp;

import java.net.http.HttpResponse;
import java.util.UUID;

import nl.tudelft.oopp.askit.views.ErrorDisplay;

public class UserLogic {

    /**
     * Ban a user using their ID.
     *
     * @param userId UUID of the user to ban
     */
    public static void banUser(UUID userId) {
        try {
            HttpResponse<String> response = banUserHttp(userId.toString());
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }
}
