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
                    yield argument + ": not found";
                }
            }
            case CommandHandler.Command.EXIT -> null;
        };
    }


}
