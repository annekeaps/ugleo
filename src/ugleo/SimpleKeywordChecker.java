package ugleo;

import java.util.Map;
import java.util.Set;

/**
 * A simple keyword checker, that returns true if the word is not in the ban list.
 * An auxilliary list is also provided, however this method does not presently use it.
 *
 * @author Trejkaz
 */
public class SimpleKeywordChecker extends KeywordChecker {  
    
    private Normalization normal;
    private Stemming stem;    
    
    private Set banWords;
    private Set auxWords;

    public SimpleKeywordChecker(Set banWords, Set auxWords, Map normWords, Stemming stem) {
        this.banWords = banWords;
        this.auxWords = auxWords;
        normal = new Normalization(normWords);        
        this.stem = stem;
    }
    
    public Stemming getStem(){
        return this.stem;
    }
/*
    //for finding keyword from input
    public boolean isKeywordInput(String symbol) { 
        //do normalization
        if (isWord(symbol) == true){
            symbol = normal.doNormalization(symbol); }
        
        //stemming
        symbol = stem.run(symbol);
        
        //return true if the word is not in the ban list and is in aux list
        return !isStopword(symbol) && auxWords.contains(symbol);
    }*/

    //for finding keyword of knowledge base sentences
    public boolean isKeyword(String symbol){
        return (isStopword(symbol) == false) & (isWord(symbol) == true);
    }
    
    public String transformWord(String symbol){          
        //do normalization
        if (isWord(symbol) == true){
            //System.out.println("\nNormalization\n");
            symbol = normal.doNormalization(symbol); 
            System.out.println("Normal\t: "+symbol.toString());
        }
        
        //stemming
        symbol = stem.run(symbol);
        System.out.println("Stem\t: "+symbol);
        
        //return true if the word is in the ban list
        return symbol;
    }
    
    //return true if the word is in the ban list
    public boolean isStopword(String symbol){            
        //System.out.println("\t"+symbol + "\t"+ banWords.contains(symbol));
        return banWords.contains(symbol);
    }
}
