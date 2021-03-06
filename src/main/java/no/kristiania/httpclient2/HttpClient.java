package no.kristiania.httpclient2;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private final int responseCode;
    private Map<String, String> responseHeaders = new HashMap<>();
    private String responseBody;

    public HttpClient(String hostname, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(hostname, port);

        // The HTTP request consists of a request line and zero or more header lines, terminated by a blank line
        // The request line ocnsists of a verb (GET, POST) a request target and the HTTP version
        // For example "GET /index.html HTTP1.1"
        String request = "GET " + requestTarget + " HTTP/1.1\r\n" +
                // A request header consists of name: value
                // The host header is the same as the web browser shows in the menu bar
                "Host: " + hostname + "\r\n\r\n";
        socket.getOutputStream().write(request.getBytes());

        // The first line in the reponse is called the reponse line
        String responseLine = readLine(socket);
        // The response line consists of the HTTP version, a reponse code and a description
        // For example "HTTP/1.1 404 Not Found"
        String[] responseLineParts = responseLine.split(" ");
        // The status line is the second word in the reponse line
        responseCode = Integer.parseInt(responseLineParts[1]);

        // After the reponse line, the reponse has zero or more reponse headers
        String headerLine;
        while (!(headerLine = readLine(socket)).isEmpty()){
            // Each header consists of name: value
            System.out.println(headerLine);
            int colonPos = headerLine.indexOf(":");
            String headerName = headerLine.substring(0, colonPos);
            // Spaces at the beginning and end of the header value should be ignored
            String headerValue = headerLine.substring(colonPos + 1).trim();
            responseHeaders.put(headerName, headerValue);
        }

        // The Content-Length header tells us how many bytes in the response that follow the header
        int contentLength = Integer.parseInt(getResponseHeader("Content-Length"));
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            body.append((char)socket.getInputStream().read());
        }
        // The next content-length bytes are called the reponse body
        this.responseBody = body.toString();
    }

    public static String readLine(Socket socket) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != -1){
            // Each line is terminated by CRLF (carriage return, line feed)
            // or \r\n
            if(c == '\r'){
                socket.getInputStream().read(); // Read the \n after \r
                break;
            }
            line.append((char)c);
        }
        return line.toString();
    }

    public static void main(String[] args) throws IOException {
        String hostname = "urlecho.appspot.com";
        int port = 80;
        String requestTarget = "/echo?status=200&body=Hello%20world!";
        new HttpClient(hostname, port, requestTarget);
    }

    public int getStatusCode() {
        return responseCode;
    }

    public String getResponseHeader(String headerName) {
        return responseHeaders.get(headerName);
    }

    public String getReponseBody() {
        return responseBody;
    }
}
