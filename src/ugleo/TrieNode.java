package ugleo;

import java.util.List;

/**
 * Represents a node in the language model.  Contains the symbol at this position, a count of the number of
 * times this context has occurred (usage), a list/map of children, and a total count of the usages of all its children
 * (count).
 *
 * @author Trejkaz
 * @see TrieNodeMap
 */
public class TrieNode {
    /**
     * The symbol which occurs at this node.
     */
    Symbol symbol;

    /**
     * The number of times this context occurs.
     */
    int usage;

    /**
     * The total of the children's usages.
     */
    int count;

    /**
     * The mapping of child symbols to TrieNode objects.
     */
    private TrieNodeMap children;

    /**
     * Constructs a root trie node.
     */
    public TrieNode() {
        this(null);
    }

    public TrieNode(Symbol symbol) {
        this.symbol = symbol;
        this.count = 0;
        this.usage = 0;
        this.children = new TrieNodeMap();
    }

    /**
     * Gets the child trie for the given symbol.  Optionally, grows the tree if this child node did
     * not already exist.
     *
     * @param symbol the symbol we are searching for.
     * @param createIfNull if true, grows the tree if the child node did not exist.
     * @return the trie node for this symbol, newly created if necessary.
     */
    public TrieNode getChild(Symbol symbol, boolean createIfNull) {
        
        TrieNode child = children.get(symbol);
        
        if (child == null && createIfNull) {
            children.put(symbol, child = new TrieNode(symbol));
        }

        return child;
    }

    public List getChildList() {
        return children.getList();
    }
    
    public String getSymbol(){
        //System.out.println("xxx : "+this.symbol);
        return this.symbol.toString();
    }
    
    public int getCount(){
        return this.count;
    }
    
    public int getUsage(){
        return this.usage;
    }
}
