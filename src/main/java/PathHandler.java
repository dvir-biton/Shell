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
        if (!path.contains("/") && !path.contains("\\\\")) {
            return null;
        }
        path = path.replace('\\', '/');

        StringBuilder pathBuilder = new StringBuilder();
        final String[] pathItems = path.split("/");
        for (final String pathItem : pathItems) {
            final RelativePath relativePath = RelativePath.getPathBySign(pathItem);

            switch (relativePath) {
                case CURRENT_DIRECTORY -> {
                    pathBuilder.setLength(0);
                    pathBuilder.append(currentPath);
                }
                case PARENT_DIRECTORY -> {
                    pathBuilder.setLength(0);
                    currentPath = getParentDirectory(currentPath);
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

    enum RelativePath {
        CURRENT_DIRECTORY("."),
        PARENT_DIRECTORY("..");

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
