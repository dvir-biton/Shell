public class CommandHandler {
    public static Command parseCommand(String command) {
        try {
            return Command.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public enum Command {
        ECHO,
        TYPE,
        PWD,
        EXIT
    }
}
