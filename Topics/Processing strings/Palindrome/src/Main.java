import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] inputCharArray = scanner.next().toCharArray();
        boolean isPalindrome = false;
        int i = 0;
        for (int j = inputCharArray.length - 1; j > 0; j--) {
            isPalindrome = inputCharArray[j] == inputCharArray[i++];
            if (!isPalindrome) break;
        }
        System.out.println(isPalindrome || inputCharArray.length == 1 ? "yes" : "no");
    }
}