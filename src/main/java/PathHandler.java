import java.nio.file.Files;
import java.nio.file.Path;

public class PathHandler {
    public static String findPath(final String command) {
        final String PATH = getEntryPath();

        final String[] paths = PATH.split(":");
        for (final String path : paths) {
            Path commandPath = Path.of(path, command);
            if (Files.isRegularFile(commandPath)) {
                return commandPath.toString();
            }
        }

        return null;
    }

    public static String parsePath(String path, String currentPath) {
        path = path.replace('\\', '/');

        StringBuilder pathBuilder = new StringBuilder();
        final String[] pathItems = path.split("/");
        for (final String pathItem : pathItems) {
            final RelativePath relativePath = RelativePath.getPathBySign(pathItem);

            if (relativePath != null) {
                pathBuilder.setLength(0);
            }
            switch (relativePath) {
                case CURRENT -> pathBuilder.append(currentPath);
                case PARENT -> {
                    currentPath = getParentDirectory(currentPath);
                    pathBuilder.append(currentPath);
                }
                case HOME -> {
                    currentPath = getHomePath();
                    pathBuilder.append(currentPath);
                }
                // not a relative path
                case null -> {
                    if (!pathItem.isBlank()) {
                        pathBuilder.append('/');
                        pathBuilder.append(pathItem);
                    }
                }
            }
        }

        return pathBuilder.toString();
    }

    private static String getParentDirectory(final String currentDirectory) {
        return currentDirectory.substring(0, currentDirectory.lastIndexOf('/'));
    }

    private static String getEntryPath() {
        return System.getenv("PATH");
    }

    private static String getHomePath() {
        return System.getenv("HOME");
    }

    enum RelativePath {
        CURRENT("."),
        PARENT(".."),
        HOME("~");

        public final String sign;

        RelativePath(final String sign) {
            this.sign = sign;
        }

        public static RelativePath getPathBySign(String sign) {
            for (final RelativePath path : values()) {
                if (path.sign.equals(sign)) {
                    return path;
                }
            }

            return null;
        }
    }
}
