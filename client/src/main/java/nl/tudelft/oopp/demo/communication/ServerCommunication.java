package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.views.ErrorDisplay;
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
    static HttpResponse<String> getStringHttpResponse(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    /**
     * Send question to server.
     *
     * @param parsedQuestion Question that is parsed and needs to be sent.
     */
    // TODO: make a user object, so the ID doesnt need to be null.
    public static HttpResponse<String> sendQuestionHTTP(String parsedQuestion)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(parsedQuestion))
                .uri(URI.create("http://localhost:8080/send/question")).build();
        return getStringHttpResponse(request);
    }

    /**
     * get questions from server.
     *
     * @param roomId UUID of the lecture room to connect
     * @return list of all questions on the server.
     */
    public static HttpResponse<String> getQuestionsHTTP(String roomId)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/get/questions?q=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint to upvote a single question.
     * no verification prevents from calling multiple times on the same question.
     * that condition should be checked beforehand (assuming that situation is not wanted).
     *
     * @param id the ID of the question
     */
    public static HttpResponse<String> upvoteQuestionHTTP(String id)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(id))
                .uri(URI.create("http://localhost:8080/send/upvote")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Create a new room and returns access links.
     *
     * @param name the String title of the room to create.
     * @return list of two strings, containing join links for staff and student respectively.
     */
    //TODO: add alert to inform user of errors when creating room.
    public static HttpResponse<String> createRoomHTTP(String name)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(name))
                .uri(URI.create("http://localhost:8080/room/create")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Join a room using the specified link.
     *
     * @param link the join link entered by the user.
     */
    //TODO: polling
    //TODO: assign role and roleID to user
    public static HttpResponse<String> joinRoomHTTP(String link)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/join/link?q=" + link)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Close the current room using its specified ID.
     *
     * @param roomId UUID of the lecture room to connect
     */
    public static HttpResponse<String> closeRoomHTTP(String roomId)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(roomId))
                .uri(URI.create("http://localhost:8080/room/close")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Get the status of the current room.
     *
     * @param roomId UUID of the lecture room to connect
     * @return true iff the room exists and is open
     */
    public static HttpResponse<String> getRoomStatusHTTP(String roomId)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/room/status?id=" + roomId)).build();
         return getStringHttpResponse(request);
    }


    public static void sendQuestion(String text, String roomId) {
        Question userQuestion = new Question(text, 0, UUID.fromString(roomId), null);
        String parsedQuestion = gson.toJson(userQuestion);
        try {
            HttpResponse<String> response = sendQuestionHTTP(parsedQuestion);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
    }

    public static List<Question> getQuestions(String roomId) {
        try {
            HttpResponse<String> response = getQuestionsHTTP(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return List.of();
            }
            return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
            }.getType());
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
        return List.of();
    }

    public static void upvoteQuestion(UUID id) {
        try {
            HttpResponse<String> response = upvoteQuestionHTTP(id.toString());

            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
    }

    public static List<String> createRoom(String name) {
        HttpResponse<String> response = null;
        try {
            response = createRoomHTTP(name);

            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return List.of();
            }
            return gson.fromJson(response.body(), new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
        return List.of();
    }

    public static void joinRoom(String link) {
        try {
            HttpResponse<String> response = joinRoomHTTP(link);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            } else {
                List<String> responseList = gson.fromJson(response.body(),
                        new TypeToken<List<String>>() {
                        }.getType());
                String[] links = link.split("/");
                String roomId = links[0];
                String roomName = responseList.get(0);
                if (responseList.get(1).equals("student")) {
                    RoomSceneDisplay.open("/roomScene.fxml", roomId, roomName);
                } else if (responseList.get(1).equals("staff")) {
                    RoomSceneDisplay.open("/roomSceneStaff.fxml", roomId, roomName);
                }
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
    }

    public static void closeRoom(String roomId) {
        HttpResponse<String> response = null;
        try {
            response = closeRoomHTTP(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
    }

    public static boolean getRoomStatus(String roomId) {
        if (roomId == null) {
            return false;
        }

        HttpResponse<String> response = null;
        try {
            response = getRoomStatusHTTP(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return false;
            } else {
                // extract boolean value from string
                return response.body().equals(Boolean.TRUE.toString());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.toString(), e.getMessage());
        }
        return false;
    }

}
