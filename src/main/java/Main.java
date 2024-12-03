import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final Parser parser = new Parser();
        String input;

        do {
            System.out.print("$ ");
            input = scanner.nextLine();
            final String output = parser.parseInput(input);
            if (output != null) {
                System.out.println(output);
            }
        } while (!input.equals("exit 0"));
    }
}
