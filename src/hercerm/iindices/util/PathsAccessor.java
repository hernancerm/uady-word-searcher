package hercerm.iindices.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Wraps a Properties object loaded with the properties file
 * containing all paths utilized by the application.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class PathsAccessor {

    private static PathsAccessor pathsAccessor;
    private static Properties properties;

    private PathsAccessor() {}

    public static PathsAccessor instance() {
        if(pathsAccessor == null) {
            properties = new Properties();
            try {
                properties.load(PathsAccessor.class.getClassLoader()
                        .getResourceAsStream("paths.properties"));
                pathsAccessor = new PathsAccessor();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathsAccessor;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
