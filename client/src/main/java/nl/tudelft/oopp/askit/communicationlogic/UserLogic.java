package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.banUserHttp;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class UserLogic {
    private static final Gson gson = new Gson();

    /**
     * Ban a user using their ID.
     *
     * @param userId UUID of the user to ban
     * @param roleId moderator code
     */
    public static void banUser(UUID userId, String roleId, String roomId) {
        List<String> sendList = List.of(
                userId.toString(),
                roleId,
                roomId
        );
        String parsedList = gson.toJson(sendList);

        try {
            HttpResponse<String> response = banUserHttp(parsedList);
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
