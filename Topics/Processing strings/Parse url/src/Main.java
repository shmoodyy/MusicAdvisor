import java.util.*;
class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String inputURL = scanner.next();
        String trimmedURL = inputURL.replace("https://target.com/index.html?", "");
        String[] formattedURL = trimmedURL.split("&");
        String parsedKeys, key, value, keyPass;
        keyPass = "";

        for (String param : formattedURL) {
            parsedKeys = Arrays.toString(param.split("="));
            int comma = parsedKeys.indexOf(",");
            if (parsedKeys.contains(",")) {
                key = parsedKeys.substring(1, comma);
                value = parsedKeys.substring(comma + 2, parsedKeys.length() - 1);
                System.out.println(key + " : " + value);
                if (parsedKeys.contains("pass")) {
                    keyPass = value;
                }
            } else {
                key = parsedKeys.substring(1, parsedKeys.length() - 1);
                System.out.println(key + " : not found");
            }
        }

        if (keyPass.length() > 0) {
            System.out.println("password : " + keyPass);
        }
    }
}