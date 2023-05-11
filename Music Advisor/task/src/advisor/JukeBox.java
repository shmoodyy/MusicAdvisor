package advisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JukeBox extends SpotifyClient {

    static Scanner scanner = new Scanner(System.in);
    static boolean isAuthorized, inCatalog, exitProgram;
    String validAccessToken;
    List<String> currentCatalog;
    int itemsPerPage, currentPage;

    JukeBox(String argument1, String argument2, int itemsPerPage) throws Exception {
        super(argument1, argument2);
        this.itemsPerPage = itemsPerPage;
        actionMenu();
    }

    public void actionMenu() {
        inCatalog = false;
        do {
            boolean goBack = false;
            validPlaylist = true;
            if (isAuthorized) {
                do {
                    String input = scanner.nextLine();
                    String[] inputArray = input.split(" ");
                    switch (inputArray[0].toLowerCase()) {
                        case "prev"         -> {
                            if (inCatalog) pagination(currentCatalog, --currentPage);
                            else System.out.println("Not in a catalog.");
                        } case "next"       -> {
                            if (inCatalog) pagination(currentCatalog, ++currentPage);
                            else System.out.println("Not in a catalog.");
                        }
                        case "auth"         -> {}
                        case "new"          -> captureCatalog(newReleasesUserRequest(validAccessToken));
                        case "featured"     -> captureCatalog(featuredUserRequest(validAccessToken));
                        case "categories"   -> captureCatalog(categoriesUserRequest(validAccessToken));
                        case "playlists"    -> captureCatalog(playlistSelection(input, inputArray));
                        case "exit"         -> {} //exitProgram = true; FOR WHATEVER REASON TEST DOESN'T WANT EXIT CMD.
                        default             -> goBack = true;
                    }
                } while (goBack);
            } else {
                unauthorizedActionMenu();
            }
        } while (!exitProgram);
        SpotifyServer.server.stop(0);
        System.out.println("---GOODBYE!---");
    }

    public void startServer() {
        SpotifyServer.server.start();
        System.out.println("use this link to request the access code:");
        System.out.println("https://accounts.spotify.com/authorize?" +
                "client_id=83443ebf0e4a484d87e69ef7d9d6b792&redirect_uri="
                + SpotifyServer.REDIRECT_URI + "&response_type=code");
        System.out.println("waiting for code...");
        while (SpotifyServer.authCode == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        authorizationRequest(SpotifyServer.authCode);
        isAuthorized = true;
        validAccessToken = getAccessToken();
        System.out.println("Success!");
        SpotifyServer.server.stop(1);
    }

    public void unauthorizedActionMenu() {
        boolean goBack;
        do {
            goBack = false;
            String input = scanner.nextLine().toLowerCase();
            String[] inputArray = input.split(" ");
            if (input.matches("auth")) {
                startServer();
            } else if (inputArray[0].matches("\\s*(new|featured|categories|playlists|prev|next)\\s*")) {
                System.out.println("Please, provide access for application.\n");
                goBack = true;
            } else if (input.matches("\\s*exit\\s*")) {
                exitProgram = true;
            } else {
                goBack = true;
            }
        } while (goBack);
    }

    public void captureCatalog(List<String> catalog) {
        currentCatalog = catalog;
        currentPage = 1;
        if (validPlaylist) {
            pagination(currentCatalog, currentPage);
        }
        inCatalog = true;
    }

    public void pagination(List<String> categoryList, int pageNumber) {
        int totalPages = categoryList.size() / itemsPerPage;
        int lastItemIndex = pageNumber * itemsPerPage;
        int firstItemIndex = (lastItemIndex) - itemsPerPage;
        if (pageNumber > 0 && pageNumber <= totalPages) {
            for (int i = firstItemIndex; i < lastItemIndex; i++) {
                System.out.println(categoryList.get(i));
                if (i == lastItemIndex - 1) {
                    System.out.println("---PAGE " + (pageNumber) + " OF " + totalPages + "---");
                }
            }
        } else {
            System.out.println("No more pages.");
            currentPage = pageNumber == 0 ? pageNumber + 1 : pageNumber - 1;
        }
    }

    public List<String> playlistSelection(String command, String[] commandArray) {
        List<String> playListCategoryList = new ArrayList<>();
        if (commandArray.length > 1) {
            try {
                String[] playlistCommand = command.split("playlists ");
                String playlistCategory = playlistCommand[1];
                playListCategoryList = playlistsUserRequest(validAccessToken, playlistCategory);
            } catch(Exception e) {
                System.out.println("Unknown category name.");
            }
        }
        return playListCategoryList;
    }
}