package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.UUID;

import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.methods.TimeControl;
import nl.tudelft.oopp.demo.views.RoomSceneDisplay;


public class ServerCommunication {

    private static final Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();

    // method to supply mock client
    public static void setHttpClient(HttpClient client) {
        ServerCommunication.client = client;
    }


    /**
     * Tries to send specified request to server and catch any exceptions.
     *
     * @param request HttpRequest to send.
     * @return response object or null.
     */
    static HttpResponse<String> getStringHttpResponse(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Send question to server.
     *
     * @param text text content of question.
     * @param roomId UUID of the lecture room to connect
     */
    // TODO: make a user object, so the ID doesnt need to be null
    public static void sendQuestion(String text, String roomId, ZonedDateTime roomTime) throws ServiceConfigurationError {
        Question userQuestion = new Question(text, 0, UUID.fromString(roomId), null, TimeControl.getMilisecondsPassed(roomTime));
        String parsedQuestion = gson.toJson(userQuestion);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(parsedQuestion))
                .uri(URI.create("http://localhost:8080/send/question")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            throw new ServiceConfigurationError("Doesn't return 200");
        }
    }

    /**
     * get questions from server.
     * @param roomId UUID of the lecture room to connect
     * @return list of all questions on the server.
     */
    public static List<Question> getQuestions(String roomId) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/get/questions?q=" + roomId)).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }

    /**
     * Connects to the server endpoint to upvote a single question.
     * no verification prevents from calling multiple times on the same question.
     * that condition should be checked beforehand (assuming that situation is not wanted).
     *
     * @param id the ID of the question
     */
    public static void upvoteQuestion(UUID id) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(id.toString()))
                .uri(URI.create("http://localhost:8080/send/upvote")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Create a new room and returns access links.
     *
     * @param name the String title of the room to create.
     * @param openTime a LocalDateTime of the room to open
     * @return list of two strings, containing join links for staff and student respectively.
     */
    //TODO: add alert to inform user of errors when creating room.
    public static List<String> createRoom(String name, ZonedDateTime openTime)
            throws ServiceConfigurationError {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(name + "!@#" + openTime.toString()))
                .uri(URI.create("http://localhost:8080/room/create")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            throw new ServiceConfigurationError("Doesn't return 200");
            // return List.of(Integer.toString(response.statusCode()));
        }

        return gson.fromJson(response.body(), new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * Join a room using the specified link.
     *
     * @param link the join link entered by the user.
     */
    //TODO: polling
    //TODO: assign role and roleID to user
    public static void joinRoom(String link) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/join/link?q=" + link)).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        } else {
            try {
                List<String> responseList = gson.fromJson(response.body(),
                        new TypeToken<List<String>>() {}.getType());
                String[] links = link.split("/");
                String roomId = links[0];
                String roomName = responseList.get(0);
                String openTime = responseList.get(2);
                if (responseList.get(1).equals("student")) {
                    RoomSceneDisplay.open("/roomScene.fxml", roomId, roomName, openTime);
                } else if (responseList.get(1).equals("staff")) {
                    RoomSceneDisplay.open("/roomSceneStaff.fxml", roomId, roomName, openTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the current room using its specified ID.
     *
     * @param roomId UUID of the lecture room to connect
     */
    public static void closeRoom(String roomId) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(roomId))
                .uri(URI.create("http://localhost:8080/room/close")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Get the status of the current room.
     *
     * @param roomId UUID of the lecture room to connect
     * @return true iff the room exists and is open
     */
    public static boolean getRoomStatus(String roomId) {
        if (roomId == null) {
            return false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/room/status?id=" + roomId)).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return false;
        } else {
            // extract boolean value from string
            return response.body().equals(Boolean.TRUE.toString());
        }
    }

}
