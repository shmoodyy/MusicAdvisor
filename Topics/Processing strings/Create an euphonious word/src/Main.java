import java.util.*;

public class Main {
    public static void main(String[] args) {
        // write your code here
        int vowelCount, consonantCount, discordantCount;
        vowelCount = consonantCount = discordantCount = 0;

        Scanner scanner = new Scanner(System.in);
        String inputWord = scanner.next();
        String[] letterByLetter = inputWord.split("");

        for (String str : letterByLetter) {
            if ("aeiouy".contains(str)) {
                vowelCount++;
                if (vowelCount >= 3 && vowelCount % 2 != 0) {
                    discordantCount++;
                }
                consonantCount = 0;
            } else {
                consonantCount++;
                if (consonantCount >= 3 && consonantCount % 2 != 0){
                    discordantCount++;
                }
                vowelCount = 0;
            }
        }
        System.out.println(discordantCount);
    }
}