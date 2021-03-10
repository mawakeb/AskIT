package nl.tudelft.oopp.demo.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import nl.tudelft.oopp.demo.data.Question;

public class ServerCommunication {

    private static final Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();

    // constructor to supply mock client
    public ServerCommunication(HttpClient client) {
        ServerCommunication.client = client;
    }

    /**
     * Tries to send specified request to server and catch any exceptions.
     *
     * @param request HttpRequest to send.
     * @return response object or null.
     */
    private static HttpResponse<String> getStringHttpResponse(HttpRequest request) {
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
     */
    public static void sendQuestion(String text) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(text))
                .uri(URI.create("http://localhost:8080/send/question")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * get questions from server.
     *
     * @return list of all questions on the server.
     */
    public static List<Question> getQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/get/questions")).build();
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
    public static void upvoteQuestion(long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(Long.toString(id)))
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
     * @return list of two strings, containing join links for staff and student respectively.
     */
    //TODO: add alert to inform user of errors when creating room.
    public static List<String> createRoom(String name) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(name))
                .uri(URI.create("http://localhost:8080/create/room")).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return List.of(Integer.toString(response.statusCode()));
        }

        return gson.fromJson(response.body(), new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * Join a room using the specified link.
     *
     * @param link the join link entered by the user.
     */
    //TODO: link to roomScene and start polling
    public static void joinRoom(String link) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/join/link?q=" + link)).build();
        HttpResponse<String> response = getStringHttpResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }


}
