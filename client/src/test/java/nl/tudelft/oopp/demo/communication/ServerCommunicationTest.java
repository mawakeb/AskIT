package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ServerCommunicationTest {

    private ServerCommunication sc;
    private static Gson gson = new Gson();

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        // supply response mock for calls to client.send(request, bodyHandler)
        when(client.send(any(HttpRequest.class),any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        sc = new ServerCommunication(client);
    }

    @Test
    public void testGetQuestions(){
        List<String> expected = List.of("Question 1","Question 2","Question 3");
        String json = gson.toJson(expected);

        // set response content
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);

        List<String> actual = sc.getQuestions();
        assertEquals(expected, actual);
    }

    @Test
    public void testSendQuestion() {
        sc.sendQuestion("Unit test question");
    }
}
