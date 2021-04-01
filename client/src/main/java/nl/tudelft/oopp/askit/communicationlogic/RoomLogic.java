package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.closeRoomHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.createRoomHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getRoomStatusHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.joinRoomHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.setSlowModeHttp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

import nl.tudelft.oopp.askit.data.Room;
import nl.tudelft.oopp.askit.methods.SerializingControl;
import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class RoomLogic {

    private static final Gson gson = SerializingControl.getGsonObject();

    /**
     * Create a new room and returns access links.
     *
     * @param name the String title of the room to create.
     * @return returns 2 links, one for staff one for student
     */
    public static List<String> createRoom(String name, ZonedDateTime openTime) {
        List<String> sendList = List.of(
                name,
                openTime.toString()
        );
        String parsedList = gson.toJson(sendList);

        HttpResponse<String> response;
        try {
            response = createRoomHttp(parsedList);

            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("error").toString());

                return List.of(json.get("error").toString());
            }
            return gson.fromJson(response.body(), new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
            return List.of(e.getMessage());
        }
    }

    /**
     * Join a room using the specified link, opens the appropriate window for the lecture.
     *
     * @param link the join link entered by the user.
     */
    //TODO: polling
    public static List<String> joinRoom(String link) {
        try {
            HttpResponse<String> response = joinRoomHttp(link);
            if (response.statusCode() != 200) {
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("message").toString());

            } else {
                return gson.fromJson(response.body(),
                        new TypeToken<List<String>>() {
                        }.getType());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
        return null;
    }

    /**
     * Closes the specified room, so no more questions can be added.
     *
     * @param roomId id of the room that needs to be closed
     */
    public static void closeRoom(String roomId) {
        HttpResponse<String> response;
        try {
            response = closeRoomHttp(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }

    /**
     * Get the status of the current room.
     *
     * @param roomId parsed UUID of the lecture room to connect
     * @return true iff the room exists and is open
     */
    public static Room getRoomStatus(String roomId) {
        if (roomId == null) {
            return null;
        }

        try {
            HttpResponse<String> response = getRoomStatusHttp(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return null;
            } else {
                // extract boolean value from string
                return gson.fromJson(response.body(), Room.class);
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
            return null;
        }
    }

    /**
     * Set the slow mode of a room.
     *
     * @param roomId parsed UUID of the User
     * @param seconds amount of seconds between questions for slow mode, 0 to disable slow mode
     */
    public static void setSlowMode(String roomId, int seconds) {
        if (roomId == null) {
            return;
        }

        try {
            HttpResponse<String> response = setSlowModeHttp(roomId, seconds);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }
}
