package code.challenge.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA_TYPE;
import static io.micronaut.http.MediaType.TEXT_PLAIN_TYPE;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class AudioControllerTest {
    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testPingResponse() {
        HttpRequest<String> request = HttpRequest.GET("/audio");
        HttpResponse response = client.toBlocking().exchange(request);

        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Test
    public void testProcessCorrectAudioFile() {
        File file = null;
        //prepare
        try {
            file = File.createTempFile("data", ".mp3");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        HttpResponse response = uploadAudioFile(file);
        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Test
    public void testProcessWrongFileFormat() {
        File file = null;
        //prepare
        try {
            file = File.createTempFile("data", ".abc");
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            HttpResponse response = uploadAudioFile(file);
        } catch (HttpClientResponseException e) {
            // correct behavior, end test
            return;
        }
        fail("wrong file format should raise an exception");
    }

    public HttpResponse uploadAudioFile(File file) {
        MultipartBody requestBody = MultipartBody.builder()
                .addPart(
                        "file",
                        file.getName(),
                        TEXT_PLAIN_TYPE,
                        file
                ).build();

        HttpRequest request = HttpRequest.POST("/audio", requestBody)
                .contentType(MULTIPART_FORM_DATA_TYPE);
        HttpResponse<String> response =
                client.toBlocking().exchange(request);

        return response;
    }


}
