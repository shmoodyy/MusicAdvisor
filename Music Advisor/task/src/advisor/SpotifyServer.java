package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class SpotifyServer {

    static String authCode;
    static final String SPOTIFY_ACCESS_POINT = "https://accounts.spotify.com/api/token";
    static String portAsString = "8080";
    static String REDIRECT_URI;
    static HttpServer server;
    String authorizationServerPath;

    SpotifyServer(String authorizationServerPath) throws Exception {
        this.authorizationServerPath = authorizationServerPath;
        server = HttpServer.create();
        createServer(generate()); //local-testing: hardcode "8080"; jetbrains: use generate()
    }

    public static int generate() throws Exception {
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        portAsString = String.valueOf(port);
        REDIRECT_URI = "http://localhost:" + portAsString;
        socket.close();
        return port;
    }

    public static void createServer(int port) throws Exception {
//        REDIRECT_URI = "http://localhost:" + portAsString; //comment this out when using jetbrains testing.
        server.bind(new InetSocketAddress(port), 0);

        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        String msg;
                        if (query == null) {
                            msg = "Authorization code not found. Try again.";
                        } else {
                            if (query.contains("code=")) {
                                System.out.println("code received");
                                msg = "Got the code. Return back to your program.";
                                authCode = query.substring(query.indexOf('=') + 1);
                            } else {
                                msg = "Authorization code not found. Try again.";
                            }

                        }
                        exchange.sendResponseHeaders(200, msg.length());
                        exchange.getResponseBody().write(msg.getBytes());
                        exchange.getResponseBody().close();
                    }
                }
        );
    }
}