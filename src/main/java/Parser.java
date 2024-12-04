import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Parser {
    String currentDirectory = System.getProperty("user.dir");

    public String parseInput(final String input) {
        final Scanner parserScanner = new Scanner(input);

        final String commandStr = parserScanner.next();
        final CommandHandler.Command command = CommandHandler.parseCommand(commandStr);
        if (command == null) {
            try {
                final String[] commandArguments = input.split(" ");
                final String fullFilePath = findCommandPath(commandArguments[0]);
                if (fullFilePath == null) {
                    return commandStr + ": command not found";
                }
                commandArguments[0] = fullFilePath;
                Process process = new ProcessBuilder(commandArguments).start();
                process.getInputStream().transferTo(System.out);
                return null;
            } catch (IOException e) {
                return commandStr + ": command not found";
            }
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
            case CD -> {
                currentDirectory = parserScanner.next();
                yield null;
            }
            case PWD -> currentDirectory;
            case CommandHandler.Command.EXIT -> null;
        };
    }

    private String findCommandPath(final String command) {
        final String PATH = getPath();

        final String[] paths = PATH.split(":");
        for (final String path : paths) {
            Path commandPath = Path.of(path, command);
            if (Files.isRegularFile(commandPath)) {
                return commandPath.toString();
            }
        }

        return null;
    }

    private String getPath() {
        return System.getenv("PATH");
    }
}
