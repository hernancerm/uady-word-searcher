package hercerm.iindices.core;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Specification for file cleansers.
 *
 * <h2>Purpose of file cleansers</h2>
 * <p>They do not modify files, instead they read their content and remove
 * unimportant words (stop words) for searches. Each cleanser requires the
 * path of a stop words file, which expected type is TXT.</p>
 *
 * @version 26.05.19
 * @author Hernán J. Cervera Manzanilla and Itzel C. Bermúdez Burgos
 */
public abstract class FileContentCleanser implements Serializable {

    final Set<String> stopWords;

    /**
     * Constructs a cleanser with the provided stop words.
     * Stop words are used to cleanse the file, i.e. these
     * words are never returned by {@link #cleanseContent(String)}
     *
     * @param path stop words text file location.
     */
    FileContentCleanser(String path) {
        stopWords = new HashSet<>();

        try(Scanner input = new Scanner(Paths.get(path))) {

            while(input.hasNext())
                stopWords.add(input.next());

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads the alphanumeric contents of a file, removes the specified
     * stop words and returns a list of the remaining words.
     *
     * @param path location of the path to cleanse.
     * @return list of relevant words (no stop words).
     */
    public abstract List<String> cleanseContent(String path);
}
