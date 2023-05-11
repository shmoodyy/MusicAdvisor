import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int varA = scanner.nextInt();
        int varB = scanner.nextInt();
        int varC = scanner.nextInt();
        int varN = scanner.nextInt();

        Random randomNum = new Random();
        final int letterBound = 26;
        final int digitBound = 10;
        StringBuilder printString = new StringBuilder();
        for (int i = 0; i < varN; i++) {
            if (i < varA || i >= varC + varB + varA) {
                char placeholderA = (char) ('A' + randomNum.nextInt(letterBound));
                while (i > 0 && printString.charAt(i - 1) == placeholderA) {
                    placeholderA = (char) ('A' + randomNum.nextInt(letterBound));
                }
                printString.append(placeholderA);
            } else if (i < varB + varA) {
                char placeholderB = (char) ('a' + randomNum.nextInt(letterBound));
                while (i > 0 && printString.charAt(i - 1) == placeholderB) {
                    placeholderB = (char) ('a' + randomNum.nextInt(letterBound));
                }
                printString.append(placeholderB);
            } else {
                char placeholderC = (char) ('0' + randomNum.nextInt(digitBound));
                while (i > 0 && printString.charAt(i - 1) == placeholderC) {
                    placeholderC = (char) ('0' + randomNum.nextInt(digitBound));
                }
                printString.append(placeholderC);
            }
        }
        System.out.print(printString);
    }
}