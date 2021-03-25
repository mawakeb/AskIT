package nl.tudelft.oopp.askit.communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerCommunication {

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
    static HttpResponse<String> getStringHttpResponse(HttpRequest request)
            throws IOException, InterruptedException {
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
     * @return HttpResponse object
     */

    public static HttpResponse<String> sendQuestionHttp(String parsedQuestion)
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
     * @return HttpResponse object
     */
    public static HttpResponse<String> getQuestionsHttp(String roomId)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/get/questions?q=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * get answered questions from server.
     *
     * @param roomId UUID of the lecture room to connect
     * @return HttpResponse object
     */
    public static HttpResponse<String> getAnsweredHttp(String roomId)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/get/answered?q=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint to upvote a single question.
     * no verification prevents from calling multiple times on the same question.
     * that condition should be checked beforehand (assuming that situation is not wanted).
     *
     * @param id the ID of the question
     * @return HttpResponse object
     */
    public static HttpResponse<String> upvoteQuestionHttp(String id)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(id))
                .uri(URI.create("http://localhost:8080/send/upvote")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint to answer a single question.
     * no verification prevents from calling multiple times on the same question.
     * that condition should be checked beforehand (assuming that situation is not wanted).
     *
     * @param id the ID of the question
     * @return HttpResponse object
     */
    public static HttpResponse<String> answerQuestionHttp(String id)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(id))
                .uri(URI.create("http://localhost:8080/send/answer")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Create a new room and returns access links.
     *
     * @param name the String title of the room to create.
     * @return HttpResponse object
     */
    public static HttpResponse<String> createRoomHttp(String name, String openTime)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(name + "!@#" + openTime))
                .uri(URI.create("http://localhost:8080/room/create")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Join a room using the specified link.
     *
     * @param link the join link entered by the user.
     * @return HttpResponse object
     */
    public static HttpResponse<String> joinRoomHttp(String link)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/room/join?q=" + link)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Close the current room using its specified ID.
     *
     * @param roomId parsed UUID of the lecture room to connect
     * @return HttpResponse object
     */
    public static HttpResponse<String> closeRoomHttp(String roomId)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(roomId))
                .uri(URI.create("http://localhost:8080/room/close")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Get the status of the current room.
     *
     * @param roomId parsed UUID of the lecture room to connect
     * @return HttpResponse object
     */
    public static HttpResponse<String> getRoomStatusHttp(String roomId)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/room/status?id=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Ban a user.
     *
     * @param userId parsed UUID of the User
     * @return HttpResponse object
     */
    public static HttpResponse<String> banUserHttp(String userId)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userId))
                .uri(URI.create("http://localhost:8080/send/ban")).build();
        return getStringHttpResponse(request);
    }
}