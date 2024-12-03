import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final Parser parser = new Parser();
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            System.out.println(parser.parseInput(input));

        } while (!input.equals("exit 0"));
    }
}
