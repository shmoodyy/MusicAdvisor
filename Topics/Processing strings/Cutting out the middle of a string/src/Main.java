import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        StringBuilder sB = new StringBuilder(input);
        int strLen = sB.length();
        int midPoint = strLen / 2;

        if (strLen % 2 == 0) {
            sB.deleteCharAt(midPoint - 1);
            sB.deleteCharAt(midPoint - 1);
            System.out.println(sB);
        } else {
            sB.deleteCharAt(midPoint);
            System.out.println(sB);
        }
    }
}