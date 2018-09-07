package ugleo;

/**
 * Factory which creates symbols.
 *
 * @author Trejkaz
 */
public class SymbolFactory {
    /**
     * Checks whether symbols are keywords.
     */
    private final KeywordChecker checker;

    /**
     * Constructs the factory.
     *
     * @param checker checks whether symbols are keywords.
     */
    public SymbolFactory(KeywordChecker checker) {
        this.checker = checker;
    }

    /**
     * Creates a symbol.
     *
     * @param symbol the string value.
     * @param hasKeyword
     * @return the symbol.
     */
    public Symbol createSymbol(String symbol) {
        //if (hasKeyword == false) {
            symbol = checker.transformWord(symbol); 
        //}
            boolean checkKey = checker.isKeyword(symbol);
            System.out.println("Keyword: "+checkKey);
            
        return new Symbol(symbol, checkKey);
    }


}
