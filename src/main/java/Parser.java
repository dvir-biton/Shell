import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Parser {
    public String parseInput(final String input) {
        final Scanner parserScanner = new Scanner(input);

        final String commandStr = parserScanner.next();
        final CommandHandler.Command command = CommandHandler.parseCommand(commandStr);
        if (command == null) {
            return commandStr + ": command not found";
        }

        return switch (command) {
            case CommandHandler.Command.ECHO -> {
                if (!parserScanner.hasNext()) {
                    yield "";
                }

                // to eliminate the space
                yield parserScanner.nextLine().substring(1);
            }
            case TYPE -> {
                final String argument = parserScanner.next();
                if (CommandHandler.parseCommand(argument) != null) {
                    yield argument + " is a shell builtin";
                } else {
                    final String commandPath = findCommandPath(argument);
                    if (commandPath == null) {
                        yield argument + ": not found";
                    }

                    yield argument + " is " + commandPath;
                }
            }
            case CommandHandler.Command.EXIT -> null;
        };
    }

    private String findCommandPath(final String commaand) {
        final String PATH = System.getenv("PATH");
        if (PATH == null) {
            return null;
        }

        final String[] paths = PATH.split(":");
        for (final String path : paths) {
            Path commandPath = Path.of(path, commaand);
            if (Files.isRegularFile(commandPath)) {
                return commandPath.toString();
            }
        }

        return null;
    }
}
