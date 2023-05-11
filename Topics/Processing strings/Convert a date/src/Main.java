import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        String inputDate = scanner.nextLine();

        String[] outputDate = inputDate.split("-");

        System.out.printf("%s/%s/%s", outputDate[1], outputDate[2], outputDate[0]);

    }
}