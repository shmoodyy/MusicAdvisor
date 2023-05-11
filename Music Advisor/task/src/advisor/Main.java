package advisor;

public class Main {
    public static void main(String[] args) throws Exception {
        String argument1 = args != null && args.length > 1 && args[0].equals("-access") ? (args[1]) + "/api/token"
                : "https://accounts.spotify.com/api/token";
        String argument2 = args != null && args.length > 2 && args[2].equals("-resource") ? args[3]
                : "https://api.spotify.com";
        int itemsPerPage = args != null && args.length > 3 && args[4].equals("-page") ? Integer.parseInt(args[5]) : 5;
        new JukeBox(argument1, argument2, itemsPerPage);
    }
}