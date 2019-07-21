package hercerm.iindices.core;

import java.io.Serializable;

class IIValue implements Comparable<IIValue>, Serializable {
    private final String path;
    private int matches;

    IIValue(String path) {
        this.path = path;
        matches = 1;
    }

    String getPath() {
        return path;
    }

    int getMatches() {
        return matches;
    }

    void setMatches(int matches) {
        this.matches = matches;
    }

    @Override
    public int compareTo(IIValue o) {
        return Integer.compare(o.matches, matches);
    }
}
