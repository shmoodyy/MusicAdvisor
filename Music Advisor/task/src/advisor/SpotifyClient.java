package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class SpotifyClient extends SpotifyServer {

    HttpClient client = HttpClient.newHttpClient();
    String apiServerPath;
    private String accessToken;
    boolean validPlaylist = true;
    Map<String, String> categoryMap = new HashMap<>();

    SpotifyClient(String authorizationServerPath, String apiServerPath) throws Exception {
        super(authorizationServerPath);
        this. apiServerPath = apiServerPath;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void authorizationRequest(String code) {
        System.out.println("making http request for access_token...");
        String encodedData = String.format("grant_type=authorization_code" +
                "&code=%s&redirect_uri=%s", code, REDIRECT_URI);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic "
                        + Base64.getEncoder().encodeToString(("83443ebf0e4a484d87e69ef7d9d6b792"
                        + ":c88558a43c584210b5a224127755327e").getBytes()))
                .uri(URI.create(authorizationServerPath)) //local: SPOTIFY_ACCESS_POINT; jetbrains: authorizationServerPath
                .POST(HttpRequest.BodyPublishers.ofString(encodedData))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            setAccessToken(JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString());
        } catch (Exception e) {
            System.out.println("We cannot send data. Please, try later.");
        }
    }

    public List<String> newReleasesUserRequest(String accessToken) {
        List<String> newReleaseList = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/new-releases"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject albumsObj = jo.getAsJsonObject("albums");
            for (JsonElement album : albumsObj.getAsJsonArray("items")) {
                JsonObject albumObject = album.getAsJsonObject();

                List<String> artistItems = new ArrayList<>();
                for (JsonElement artist : albumObject.getAsJsonArray("artists")) {
                    JsonObject artistObject = artist.getAsJsonObject();
                    artistItems.add(artistObject.get("name").getAsString());
                }
                newReleaseList.add(String.format("%s%n%s%n%s%n%n", albumObject.get("name").getAsString()
                        , artistItems
                        , albumObject.getAsJsonObject("external_urls").get("spotify").getAsString()));
            }
        } catch (Exception e) {
            System.out.println("We cannot send data. Please, try later.");
        }
        return newReleaseList;
    }

    public List<String> featuredUserRequest(String accessToken) {
        List<String> featuredList = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/featured-playlists"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject playlistsObj = jo.getAsJsonObject("playlists");

            for (JsonElement playlist : playlistsObj.getAsJsonArray("items")) {
                JsonObject playlistObject = playlist.getAsJsonObject();
                featuredList.add(String.format("%s%n%s%n%n", playlistObject.get("name").getAsString()
                        , playlistObject.getAsJsonObject("external_urls").get("spotify").getAsString()));
            }
        } catch (Exception e) {
            System.out.println("We cannot send data. Please, try later.");
        }
        return featuredList;
    }

    public List<String> categoriesUserRequest(String accessToken) {
        List<String> categoryList = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/categories"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categoriesObj = jo.getAsJsonObject("categories");

            for (JsonElement category : categoriesObj.getAsJsonArray("items")) {
                JsonObject categoryObject = category.getAsJsonObject();
                String categoryName = categoryObject.get("name").getAsString();
                categoryList.add(categoryName);
            }

        } catch (Exception e) {
            System.out.println("We cannot send data. Please, try later.");
        }
        return categoryList;

    }

    public List<String> playlistsUserRequest(String accessToken, String categoryArgument) {
        List<String> playlistList = new ArrayList<>();
        HttpRequest requestCategories = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/categories"))
                .GET()
                .build();

        try {
            HttpResponse<String> response1 = client.send(requestCategories, HttpResponse.BodyHandlers.ofString());
            JsonObject jo1 = JsonParser.parseString(response1.body()).getAsJsonObject();
            JsonObject categoriesObj = jo1.getAsJsonObject("categories");

            for (JsonElement category : categoriesObj.getAsJsonArray("items")) {
                JsonObject categoryObject = category.getAsJsonObject();
                String categoryName = categoryObject.get("name").getAsString();
                categoryMap.put(categoryName, categoryObject.get("id").getAsString());
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .uri(URI.create(apiServerPath + "/v1/browse/categories/"
                            + categoryMap.get(categoryArgument) + "/playlists"))
                    .GET()
                    .build();

            try {
                HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jo2 = JsonParser.parseString(response2.body()).getAsJsonObject();
                JsonObject playlistsObj = jo2.getAsJsonObject("playlists");
                for (JsonElement playlist : playlistsObj.getAsJsonArray("items")) {
                    JsonObject playlistObject = playlist.getAsJsonObject();
                    playlistList.add(String.format("%s%n%s%n%n", playlistObject.get("name").getAsString()
                            , playlistObject.getAsJsonObject("external_urls")
                                    .get("spotify").getAsString()));
                }
            } catch (Exception e) {
                validPlaylist = false;
                System.out.println("Test unpredictable error message");
            }
        } catch (Exception e) {
            System.out.println("We cannot send data. Please, try later.");
        }
        return playlistList;
    }
}