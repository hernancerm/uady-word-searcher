package hercerm.iindices.app;

import hercerm.iindices.core.IIndexGenerator;
import hercerm.iindices.core.REPL;
import hercerm.iindices.core.REPLCommands;
import hercerm.iindices.core.WordSearcher;
import hercerm.iindices.util.FileDownloader;
import hercerm.iindices.util.PathsAccessor;
import hercerm.iindices.util.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Instantiating this class starts a REPL, as well as does the following:
 * <ul>
 *     <li>If not existent, creates %USERPROFILE%\WordSearcher directory.</li>
 *     <li>If not existent, downloads
 *     https://raw.githubusercontent.com/stopwords-iso/stopwords-es/master/stopwords-es.txt</li>
 *     into %USERPROFILE%\WordSearcher\stopWords-es.txt
 *     <li>If existent, de-serializes %USERPROFILE%\WordSearcher\iIndices.ser and loads a new
 *     WordSearcher with it. Else, loads the searcher with a new index generator.</li>
 * </ul>
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class WordSearcherApplication {

    private final String BASE_DIR;
    private final String STOP_WORDS_FN;
    private final String INDICES_FN;

    public WordSearcherApplication() {
        PathsAccessor accessor = PathsAccessor.instance();

        BASE_DIR = System.getenv((String) accessor.get("root_d")) + accessor.get("app_d");
        STOP_WORDS_FN = accessor.get("stopWords_f");
        INDICES_FN = accessor.get("iIndices_f");

        filesSetup();
        new REPL(new REPLCommands(fetchSearcher())).start();
    }

    private void filesSetup() {
        try {
            PathsAccessor accessor = PathsAccessor.instance();

            // Create directory for storing generated indices
            if(Files.notExists(Paths.get(BASE_DIR)))
                Files.createDirectory(Paths.get(BASE_DIR));

            // Download stopWords-es.txt file if not existent in BASE_DIR
            if(Files.notExists(Paths.get(BASE_DIR + STOP_WORDS_FN)))
                FileDownloader.download(accessor.get("stopWords_url"),
                        BASE_DIR + STOP_WORDS_FN);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private WordSearcher fetchSearcher() {
        IIndexGenerator generator;

        // Deserialize or create new IIndexGenerator
        if(Files.exists(Paths.get(BASE_DIR + INDICES_FN))) {
            Serializer<IIndexGenerator> serializer =
                    new Serializer<>(BASE_DIR + INDICES_FN);
            generator = serializer.deserialize();
        }
        else
            generator = new IIndexGenerator();

        return new WordSearcher(generator);
    }
}
