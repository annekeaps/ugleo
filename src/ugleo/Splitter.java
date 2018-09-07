package ugleo;

import java.util.List;
import java.util.Map;

/**
 * Interface defining a method to split a string into a list of symbols.
 *
 * @author Trejkaz
 */
public interface Splitter {
    /**
     * Split a string into a list of symbols.
     *
     * @param text the string to split.
     * @param hasKeyword
     * @return a list of Symbol objects.
     */
    public List<Symbol> split(String text);

    /**
     * Join a list of symbols into a string.
     *
     * @param symbols the symbols to join.
     * @return the joined string.
     */
    public String join(List<Symbol> symbols, Map<String, String> rootPair);
}
