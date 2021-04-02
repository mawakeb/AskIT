package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.answerQuestionHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.cancelUpvoteHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getAnsweredHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getQuestionsHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getTimeLeftHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.sendQuestionHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.upvoteQuestionHttp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.views.ErrorDisplay;
import org.json.JSONObject;

public class QuestionLogic {
    private static final Gson gson = new Gson();

    /**
     * Sends a question to server.
     *
     * @param text   String that represents the question
     * @param roomId id of the room that it's being sent to
     * @return "SUCCESS" if the question was sent successfully, a status why not otherwise
     */
    public static String sendQuestion(String text, UUID roomId, UUID userId, String username,
                                      int roomTimeMili) {

        Question userQuestion = new Question(text, 0, roomId, userId, username, roomTimeMili);

        String parsedQuestion = gson.toJson(userQuestion);
        try {
            HttpResponse<String> response = sendQuestionHttp(parsedQuestion);
            if (response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                JsonObject jsonResponseBody = gson.fromJson(response.body(), JsonObject.class);
                if (response.statusCode() == 403) { // 403 = Forbidden
                    // when the server understood the request, but the question was
                    // rejected for other reasons, return the reason silently without ErrorDisplay
                    return jsonResponseBody.get("message").getAsString();
                } else {
                    ErrorDisplay.open(response);
                    return "EXCEPTION";
                }
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
            return "EXCEPTION";
        }
    }

    /**
     * Gets questions from server.
     *
     * @param roomId room that holds the wanted questions
     * @return list of questions
     */
    public static List<Question> getQuestions(String roomId) {
        try {
            HttpResponse<String> response = getQuestionsHttp(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return List.of();
            }
            return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
            }.getType());
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
        return List.of();
    }

    /**
     * Gets answered questions from server.
     *
     * @param roomId room that holds the wanted questions
     * @return list of questions
     */
    public static List<Question> getAnswered(String roomId) {
        try {
            HttpResponse<String> response = getAnsweredHttp(roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
                return List.of();
            }
            return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
            }.getType());
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
        return List.of();
    }

    /**
     * Up votes a question.
     *
     * @param id of the up voted question
     * @param userId id of the upVoting user
     */
    public static void upvoteQuestion(UUID id, UUID userId) {
        List<String> sendList = List.of(
                id.toString(),
                userId.toString()
        );
        String parsedList = gson.toJson(sendList);
        try {
            HttpResponse<String> response = upvoteQuestionHttp(parsedList);

            if (response.statusCode() != 200) {
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("message").toString());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }

    /**
     * Cancels an upvote of a question.
     *
     * @param id of the up voted question
     * @param userId of the user
     */
    public static void cancelUpvote(UUID id, UUID userId) {
        List<String> sendList = List.of(
                id.toString(),
                userId.toString()
        );
        String parsedList = gson.toJson(sendList);
        try {
            HttpResponse<String> response = cancelUpvoteHttp(parsedList);

            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }


    /**
     * Answers a question.
     *
     * @param id of the answered question
     * @param roleId moderator code
     */

    public static void answerQuestion(UUID id, String roleId, ZonedDateTime roomTime) {
        List<String> sendList = List.of(
                id.toString(),
                roleId,
                Integer.toString(TimeControl.getMilisecondsPassed(roomTime))
        );
        String parsedList = gson.toJson(sendList);
        try {
            HttpResponse<String> response = answerQuestionHttp(parsedList);

            if (response.statusCode() != 200) {
                JSONObject json = new JSONObject(response.body());
                ErrorDisplay.open("Status code: " + response.statusCode(),
                        json.get("message").toString());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }

    /**
     * Get how long a user has to wait before asking a new question (regarding slow mode).
     *
     * @param userId user ID
     * @param roomId ID of the room the user belongs to
     * @return amount of milliseconds left to wait, might be negative
     */
    public static int getTimeLeft(String userId, String roomId) {
        try {
            HttpResponse<String> response = getTimeLeftHttp(userId,roomId);
            if (response.statusCode() != 200) {
                ErrorDisplay.open(response);
                return 0;
            }
            return Integer.parseInt(response.body());
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
            return 0;
        }
    }
}
