package hercerm.iindices.core;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Cleanser for text (.txt) files.
 *
 * @version 26.05.19
 * @author Hernán J. Cervera Manzanilla and Itzel C. Bermúdez Burgos
 */
public class TXTCleanser extends FileContentCleanser {

    TXTCleanser(String path) {
        super(path);
    }

    /**
     * Refer to {@link hercerm.iindices.core.FileContentCleanser#cleanseContent(String)}
     *
     * @param path location of the path to cleanse.
     * @return list of relevant words (no stop words).
     */
    @Override
    public List<String> cleanseContent(String path) {
        List<String> relevantWords = new LinkedList<>();

        try(Scanner sc = new Scanner(Paths.get(path))) {
            while(sc.hasNext()){
                final String redundantOuter = "[-_.,:;]";
                final String redundantInner = "[,:;]";

                String word = sc.next().replaceAll(String.format("^%s+|%s+$",
                        redundantOuter, redundantOuter), "");
                String[] words = word.split(redundantInner);

                for(String s : words)
                    if(!stopWords.contains(s.toLowerCase()))
                        relevantWords.add(s);
            }
        }
        catch(NoSuchFileException e) {
            System.out.println("File path " + e.getMessage() + " not found.");
        }
        catch(AccessDeniedException e) {
            System.out.println("File path " +
                    (e.getMessage().equals("") ? "" :
                            (e.getMessage() + " ")) + "not accessible.");
        }
        catch (IOException e) {
            System.out.println("Undefined I/O error.");
            System.exit(1);
        }

        return relevantWords;
    }
}
