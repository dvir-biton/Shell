import java.util.Scanner;

public class Parser {
    public String parseInput(final String input) {
        final Scanner parserScanner = new Scanner(input);

        final String command = parserScanner.next();
        return switch (command) {
            case "echo" -> {
                if (!parserScanner.hasNext()) {
                    yield "";
                }

                // to eliminate the space
                yield parserScanner.nextLine().substring(1);
            }
            case "exit" -> input;
            default -> command + ": command not found";
        };
    }
}
