package no.kristiania.httpclient2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public HttpServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                handleRequest(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void handleRequest(Socket clientSocket) throws IOException {


        // The first line of the incoming request is called the request line
        String requestLine = HttpClient.readLine(clientSocket);
        System.out.println(requestLine);

        // The requestLine consists of a verb (GET, POST), a request target and HTTP version
        String requestTarget = requestLine.split(" ")[1];
        String statusCode = "200";
        String body = null;
        // The request target can have a query string separated by ?
        // For example /echo?status=404
        int questionPos = requestTarget.indexOf("?");
        if(questionPos != -1){
            QueryString queryString = new QueryString(requestTarget.substring(questionPos+1));
            statusCode = queryString.getParameter("status");
            if(statusCode == null) {
                statusCode = "200";
            }
            body = queryString.getParameter("body");
        }
        if (body == null) {
            body = "Hello <strong>World</strong>!";
        }

        String response = "HTTP/1.1 " + statusCode + " OK\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes());
    }

    public static void main(String[] args) throws IOException {
        new HttpServer(8080);
    }
}
