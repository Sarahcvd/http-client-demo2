package no.kristiania.httpclient2;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    /* FÅ VEKK DENNE TESTEN */
    private HttpClient HttpClient(String requestTarget) throws IOException {
        return new HttpClient("urlecho.appspot.com", 80, requestTarget);
    }

    @Test
    void shouldReadSuccessStatusCode() throws IOException {
        HttpClient client = new HttpClient ("urlecho.appspot.com", 80, "/echo");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReadFailureStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?status=404");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReadHeaders() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=Kristiania");
        assertEquals("10", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReadResponseBody() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=Kristiania");
        assertEquals("Kristiania", client.getReponseBody());
    }

}