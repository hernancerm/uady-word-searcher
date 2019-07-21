package hercerm.iindices.core;

import hercerm.iindices.util.PathsAccessor;
import hercerm.iindices.util.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the evaluation and printing side of the REPL.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class REPLCommands {

    private WordSearcher searcher;

    private final String BASE_DIR;
    private final String INDICES_FN;

    /**
     * The REPL supports searching, this is done through a
     * {@link hercerm.iindices.core.WordSearcher}.
     *
     * @param searcher used for performing searches and registering new files.
     */
    public REPLCommands(WordSearcher searcher) {
        PathsAccessor accessor = PathsAccessor.instance();

        BASE_DIR = System.getenv((String) accessor.get("root_d")) + accessor.get("app_d");
        INDICES_FN = accessor.get("iIndices_f");

        this.searcher = searcher;
    }

    /**
     * Generates inverted indices for a specified file.
     *
     * @param command command keyword and argument (expected: file path).
     */
    void add(String command) {
        try {
            searcher.getGenerator().addFile(
                    command.replaceAll("^add\\s+", ""));
        }
        catch(IndexOutOfBoundsException e) {
            unsupported();
        }
    }

    /**
     * Searches a word in a registered file. Files are registered via
     * {@link #add(String)}.
     *
     * @param command command keyword and argument (expected: search word).
     */
    void search(String command) {
        try {
            List<String> matches = searcher.prettySearch(command.split(" ")[1]);

            for(String match : matches)
                System.out.println("  " + match);

        } catch (IndexOutOfBoundsException e) {
            unsupported();
        }
    }

    /**
     * Sets new index generator for the searcher and deletes serialization file.
     */
    void clear() {
        try {
            Files.deleteIfExists(Paths.get(BASE_DIR + INDICES_FN));
            searcher.setGenerator(new IIndexGenerator());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Deletes serialization file and creates a new one in its place. The purpose of this
     * file is to persist the generated indices during the session. Closing the app by
     * any different means prevents the indices from being persisted.
     */
    void exit() {
        try {
            Serializer<IIndexGenerator> serializer = new Serializer<>(BASE_DIR + INDICES_FN);
            Files.deleteIfExists(Paths.get(BASE_DIR + INDICES_FN));
            serializer.serialize(searcher.getGenerator());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Prints help.txt, a help file containing the syntax and description for each command.
     */
    @SuppressWarnings("ConstantConditions")
    void help() {
        Scanner helpFile = new Scanner(REPLCommands.class
                .getClassLoader().getResourceAsStream("help.txt"));

        while(helpFile.hasNextLine())
            System.out.println(helpFile.nextLine());

        helpFile.close();
    }

    /**
     * Message for unsupported commands. A command is unsupported if the command name is
     * unrecognized of the arguments don't match number or identity expected.
     */
    void unsupported() {
        System.out.println("Command with such arguments not recognized.");
    }
}
