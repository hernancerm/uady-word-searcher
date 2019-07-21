package hercerm.iindices.core;

import hercerm.iindices.app.WordSearcherApplication;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Generates inverted indices for any input text file.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class IIndexGenerator implements Serializable {

    private Map<String, List<IIValue>> iIndices;
    private FileContentCleanser cleanser;
    private Set<String> registeredPaths;

    public IIndexGenerator() {
        this.iIndices = new HashMap<>();
        this.registeredPaths = new HashSet<>();

        Properties p = new Properties();
        try {
            p.load(WordSearcherApplication.class.getClassLoader()
                    .getResourceAsStream("paths.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        final String BASE_DIR = System.getenv((String) p.get("root_d")) + p.get("app_d");
        final String STOP_WORDS_FN = p.getProperty("stopWords_f");

        // Set default startup cleanser
        this.cleanser = new TXTCleanser(BASE_DIR + STOP_WORDS_FN);
    }

    /**
     * If the file has already been added, no operation is performed. Otherwise, stop words
     * are removed and entries for each word are created.
     *
     * @param path text file path.
     */
    void addFile(String path){

        // Duplicate paths not allowed
        if(registeredPaths.contains(path))
            return;
        registeredPaths.add(path);

        // TODO: 23.05.19 Autodetect file type and set cleanser type accordingly
        List<String> words = cleanser.cleanseContent(path);

        processingLoop:
        for (String word : words)
            if (iIndices.containsKey(word)) {
                for (IIValue value : iIndices.get(word))
                    if (value.getPath().equals(path)) {
                        value.setMatches(value.getMatches() + 1);
                        continue processingLoop;
                    }
                iIndices.get(word).add(new IIValue(path));
            } else {
                iIndices.put(word, new LinkedList<>());
                iIndices.get(word).add(new IIValue(path));
            }

        // Keep files sorted by number of matches
        for(List<IIValue> lists : iIndices.values())
            Collections.sort(lists);
    }

    Map<String, List<IIValue>> getIIndices() {
        return iIndices;
    }
}
