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
                // check for process if not a builtin command
                final String[] commandArguments = input.split(" ");
                final String fullFilePath = PathHandler.findPath(commandArguments[0]);
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
                    final String commandPath = PathHandler.findPath(argument);
                    if (commandPath == null) {
                        yield argument + ": not found";
                    }

                    yield argument + " is " + commandPath;
                }
            }
            case CD -> {
                final String argument = parserScanner.next();
                final String parsedPath = PathHandler.parsePath(argument, currentDirectory);
                if (Files.isDirectory(Path.of(parsedPath))) {
                    currentDirectory = parsedPath;
                } else {
                    yield "cd: " + argument + ": No such file or directory";
                }

                yield null;
            }
            case PWD -> currentDirectory;
            case CommandHandler.Command.EXIT -> null;
        };
    }
}
