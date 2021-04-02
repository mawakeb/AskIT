package nl.tudelft.oopp.askit.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;


public class ServerCommunicationTest {

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    // request made by tests
    @Mock
    private HttpRequest mockRequest;


    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.initMocks(this);

        // clear request that might be left from earlier tests

        // supply response mock for calls to client.send(request, bodyHandler)
        // also stores the corresponding request for access during tests
        when(client.send(any(HttpRequest.class), ArgumentMatchers.any()))
                .thenAnswer((InvocationOnMock invocation) -> response);

        ServerCommunication.setHttpClient(client);
    }

    @Test
    void getStringHttpResponse() throws IOException, InterruptedException {
        // send an empty request
        HttpResponse<String> result = ServerCommunication.getStringHttpResponse(mockRequest);

        // the httpClient responds without errors by default in @BeforeEach
        // so the method should simple return the mocked response
        assertEquals(response, result);
    }

    @Test
    void getStringHttpResponseException() {
        // test the exception handling, getStringHttpResponse should not fail but just return null
        HttpResponse<String> result = null;
        try {
            when(client.send(any(HttpRequest.class), ArgumentMatchers.any()))
                    .thenThrow(IOException.class);
            result = ServerCommunication.getStringHttpResponse(mockRequest);
        } catch (Exception e) {
            // no exception should be thrown when initializing mock behaviour
        }

        assertNull(result);
    }
}
