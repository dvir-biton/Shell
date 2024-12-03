import java.util.Scanner;
import static java.util.Objects.equals;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = "";

        do {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            input = scanner.next();

            if (input.equals("exit")) {
                if (scanner.hasNextInt()) {
                    final int statusCode = scanner.nextInt();
                }
                break;
            }

            System.out.println(input + ": command not found");
        } while (true);
    }
}
