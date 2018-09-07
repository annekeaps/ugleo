package ugleo;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A splitter which splits on word boundaries, splitting the string into a list of 'word'
 * fragments and 'non-word' fragments.
 *
 * @author Trejkaz
 */
public class WordNonwordSplitter implements Splitter {

    /**
     * The regex pattern which defines word boundaries.
     */
    //private static Pattern boundaryPattern = Pattern.compile("([\\w']+|[^\\w']+)");    
    private static Pattern boundaryPattern = Pattern.compile("([\\w]+|[^\\w]+)");
    private List <String> exceptJoin = new ArrayList<String>();
    private Map<String,String> swapWords = new HashMap<String,String>();    

   

    /**
     * Symbol factory for creating symbols.
     */
    private final SymbolFactory symbolFactory;    
    

    /**
     * Creates the splitter.
     *
     * @param symbolFactory symbol factory for creating symbols.
     * @param swapWords
     */
    public WordNonwordSplitter(SymbolFactory symbolFactory, Map swapWords) {
        this.symbolFactory = symbolFactory;
        this.swapWords = swapWords;
    }

    public List<Symbol> split(String text) {
        //System.out.println("TEXT: "+text);
        String str;
        List<Symbol> symbolList = new ArrayList<Symbol>();        
        symbolList.add(Symbol.START);

        Matcher m = boundaryPattern.matcher(text);
        while (m.find()) {
            //System.out.println("Match: "+m.group());
            str = m.group().toUpperCase().intern();
            System.out.println("Word: "+str); 
            System.out.println("-------------------------");
            //System.out.println("Match: "+swapWords.containsKey(str));            
            
            if (swapWords.containsKey(str)){
                String [] temp = swapWords.get(str).split(" ");
                
                for (int i=0; i<temp.length; i++){
                    System.out.println("SWAP:\t "+temp[i]);

                    Symbol s = symbolFactory.createSymbol(temp[i]);
                    //System.out.println("Symbol: "+s.toString());
                    //Symbol s = symbolFactory.createSymbol(word);

                    symbolList.add(s);
                }
            } else {
                Symbol s = symbolFactory.createSymbol(str);
                symbolList.add(s);
            }
            System.out.println("=========================");
        }
        symbolList.add(Symbol.END);
        return symbolList;
    }

    public String join(List<Symbol> symbols, Map <String, String> rootPair) {
        exceptJoin.add("SIAPA"); exceptJoin.add("APA"); exceptJoin.add("DIMANA"); 
        exceptJoin.add("KAPAN"); exceptJoin.add("KENAPA"); exceptJoin.add("BAGAIMANA");
        
        // Chop off the <START> and <END>
        symbols = symbols.subList(1, symbols.size() - 1);

        String symbolStr;
        // Build up the rejoined list.
        StringBuffer result = new StringBuffer();
        for (Symbol symbol : symbols) {
            symbolStr = symbol.toString();             
            
            if (rootPair.containsKey(symbolStr)){
                symbolStr = rootPair.get(symbolStr);                
            }
            
            System.out.println("STR: "+symbolStr);
            
            if (!exceptJoin.contains(symbolStr)){
                //System.out.println("PASS JOIN");
                result.append(symbolStr.toLowerCase());
            } 
        }        
        return result.toString();
    }
}

