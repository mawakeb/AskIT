package nl.tudelft.oopp.askit.communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static final String serverLoc = "http://localhost:8080";

    // method to supply mock client
    public static void setHttpClient(HttpClient client) {
        ServerCommunication.client = client;
    }


    /**
     * Tries to send specified request to server and catch any exceptions.
     *
     * @param request HttpRequest to send.
     * @return response object.
     */
    static HttpResponse<String> getStringHttpResponse(HttpRequest request)
            throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
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
                .uri(URI.create(serverLoc + "/send/question")).build();
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

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(
                serverLoc + "/get/questions?q=" + roomId)).build();
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

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(
                serverLoc + "/get/answered?q=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint to upvote a single question.
     *
     * @param list the ID of the question and user
     * @return HttpResponse object
     */
    public static HttpResponse<String> upvoteQuestionHttp(String list)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(serverLoc + "/send/upvote")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint cancel the upvote of the specific question.
     * You cannot cancel upvote of a question if it wasn't upvoted previously
     *
     * @param ids the ID of the question and user
     * @return HttpResponse object
     */
    public static HttpResponse<String> cancelUpvoteHttp(String ids)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(ids))
                .uri(URI.create(serverLoc + "/send/cancelUpvote")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Connects to the server endpoint to answer a single question.
     *
     * @param list contains the ID of the question and role
     * @return HttpResponse object
     */
    public static HttpResponse<String> answerQuestionHttp(String list)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(serverLoc + "/send/answer")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Create a new room and returns access links.
     *
     * @param list that contains both name and open time
     * @return HttpResponse object
     */
    public static HttpResponse<String> createRoomHttp(String list)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(serverLoc + "/room/create")).build();
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

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(
                serverLoc + "/room/join?q=" + link)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Close the current room using its specified ID.
     *
     * @param list contains parsed UUID of the lecture room to connect and roleId
     * @return HttpResponse object
     */
    public static HttpResponse<String> closeRoomHttp(String list)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(serverLoc + "/room/close")).build();
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
                .uri(URI.create(serverLoc + "/room/status?id=" + roomId)).build();
        return getStringHttpResponse(request);
    }

    /**
     * Ban a user.
     *
     * @param list contains parsed UUID of the User and roleId
     * @return HttpResponse object
     */
    public static HttpResponse<String> banUserHttp(String list)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(list))
                .uri(URI.create(serverLoc + "/send/ban")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Sends users opinion on speed.
     *
     * @param parsedList parsed list of int speed, UUID userId, UUID roomId
     * @return HttpResponse object
     */
    public static HttpResponse<String> sendSpeedHttp(String parsedList)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(parsedList))
                .uri(URI.create(serverLoc + "/speed/send")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Gets user opinion on speed.
     *
     * @param parsedList contains parsed room UUID and roleId
     * @return HttpResponse object
     */
    public static HttpResponse<String> getSpeedHttp(String parsedList)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(parsedList))
                .uri(URI.create(serverLoc + "/speed/get")).build();
        return getStringHttpResponse(request);
    }

    /**
     * Set the slow mode of a room.
     *
     * @param roomId parsed UUID of the User
     * @param seconds amount of seconds between questions for slow mode, 0 to disable slow mode
     * @param roleId moderator code
     * @return HttpResponse object
     */
    public static HttpResponse<String> setSlowModeHttp(String roomId, int seconds, String roleId)
            throws IOException, InterruptedException {

        // creates POST with empty body
        // it's easier to pass multiple parameters in url
        // while the functionality of the method is still best described with POST
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create(serverLoc + "/room/slow"
                        + "?id=" + roomId + "&seconds=" + seconds + "&roleId=" + roleId
                )).build();
        return getStringHttpResponse(request);
    }

    /**
     * Get how long a user has to wait before asking a new question (regarding slow mode).
     *
     * @param userId user ID
     * @param roomId ID of the room the user belongs to
     * @return HttpResponse object
     */
    public static HttpResponse<String> getTimeLeftHttp(String userId, String roomId)
            throws IOException, InterruptedException {

        // creates POST with empty body
        // it's easier to pass multiple parameters in url
        // while the functionality of the method is still best described with POST
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(serverLoc + "/send/timeleft"
                        + "?uid=" + userId + "&rid=" + roomId
                )).build();
        return getStringHttpResponse(request);
    }
}
