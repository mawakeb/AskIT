package nl.tudelft.oopp.askit.communicationlogic;

import static nl.tudelft.oopp.askit.communication.ServerCommunication.answerQuestionHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getAnsweredHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.getQuestionsHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.sendQuestionHttp;
import static nl.tudelft.oopp.askit.communication.ServerCommunication.upvoteQuestionHttp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import nl.tudelft.oopp.askit.data.Question;
import nl.tudelft.oopp.askit.methods.TimeControl;
import nl.tudelft.oopp.askit.views.ErrorDisplay;

public class QuestionLogic {
    private static final Gson gson = new Gson();

    /**
     * Sends a question to server.
     *
     * @param text   String that represents the question
     * @param roomId id of the room that it's being sent to
     * @return boolean of whether the user is banned
     */
    public static boolean sendQuestion(String text, UUID roomId, UUID userId,
                                       ZonedDateTime roomTime) {

        Question userQuestion = new Question(text, 0, roomId, userId,
                TimeControl.getMilisecondsPassed(roomTime));

        String parsedQuestion = gson.toJson(userQuestion);
        try {
            HttpResponse<String> response = sendQuestionHttp(parsedQuestion);
            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }

            // handle responses where the request was received successfully,
            // but logic on the server rejects storing the question for different reasons
            if (!response.body().equals("SUCCESS")) {
                return true;
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
        return false;
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
     * Upvotes a question.
     *
     * @param id of the upvoted question
     */
    public static void upvoteQuestion(UUID id) {
        try {
            HttpResponse<String> response = upvoteQuestionHttp(id.toString());

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
     */
    public static void answerQuestion(UUID id) {
        try {
            HttpResponse<String> response = answerQuestionHttp(id.toString());

            if (response.statusCode() != 200) {
                ErrorDisplay.open("Status code: " + response.statusCode(), response.body());
            }
        } catch (Exception e) {
            ErrorDisplay.open(e.getClass().getCanonicalName(), e.getMessage());
        }
    }
}
