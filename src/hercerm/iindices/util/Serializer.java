package hercerm.iindices.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Provides a high level API for serializing and de-serializing objects of
 * the same type into one path. Only one serialization file is present.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 *
 * @param <T> Serializable target class.
 */
public class Serializer<T extends Serializable> {

    private final String PATH;

    /**
     * Creates a serializer bound to this path.
     *
     * @param PATH path to store serialized object.
     */
    public Serializer(String PATH) {
        this.PATH = PATH;
    }

    /**
     * Serializes the target object into the specified path.
     *
     * @param target object to serialize.
     */
    public void serialize(T target) {
        try(ObjectOutputStream output =
                    new ObjectOutputStream(
                            Files.newOutputStream(Paths.get(PATH)));) {
            output.writeObject(target);
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * De-serializes the serialization file defined by path. {@link #Serializer(String)}
     *
     * @return de-serialized object.
     */
    @SuppressWarnings("unchecked")
    public T deserialize() {
        T retrieved = null;

        try(ObjectInputStream input =
                    new ObjectInputStream(
                            Files.newInputStream(Paths.get(PATH)));) {
            retrieved = (T) input.readObject();
        }
        catch(ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return retrieved;
    }
}
