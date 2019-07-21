package hercerm.iindices.core;

import java.util.LinkedList;
import java.util.List;

/**
 * Performs searches upon indices provided by an inverted index generator:
 * {@link hercerm.iindices.core.IIndexGenerator}.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class WordSearcher {
    private IIndexGenerator generator;

    /**
     * Build searcher with such inverted indices.
     *
     * @param generator inverted indices on which to perform searches.
     */
    public WordSearcher(IIndexGenerator generator) {
        this.generator = generator;
    }

    /**
     * Performs a search upon the loaded generator. Refer to
     * {@link #WordSearcher(IIndexGenerator)}.
     *
     * @param word search word.
     * @return list of files which contain matching strings. For each matching
     * file the number of matches is specified. The list is sorted in descending
     * order as to number of matches.
     */
    List<String> prettySearch(String word) {
        List<String> matches = new LinkedList<>();

        if(generator.getIIndices().containsKey(word))
            for(IIValue v: generator.getIIndices().get(word))
                matches.add(String.format("%s has %d matches.",
                        v.getPath(), v.getMatches()));

        return matches;
    }

    IIndexGenerator getGenerator() {
        return generator;
    }
    void setGenerator(IIndexGenerator generator) {
        this.generator = generator;
    }
}
