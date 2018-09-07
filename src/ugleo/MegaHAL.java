package ugleo;

import java.util.*;
import java.io.IOException;
import java.sql.*;

/**
 * Main class implementing the main MegaHAL engine.
 * Provides methods to train the brain, and to generate text
 * responses from it.
 *
 * @author Trejkaz
 */
public class MegaHAL {

    // Fixed word sets.
    
    private Map<String, String> rootPair;
    
    /* A mapping of words which will be swapped for other words.
                     Map<word, swap>
    */
    private final Map<String, String> swapWords;
    
    //normalization
    //Map <not norm word, word>
    private final Map<String, String> normWords;
    
    // Hidden Markov first_attempt.Model
    private final Model model;

    // Parsing utilities
    private final Splitter splitter;
    
    //Maintaining knowledge base 
    private final maintenanceKB kb;

    // Random Number Generator
    private final Random rng = new Random();
    
    private final Stemming stem;
    
    public List<String> idx = new ArrayList<String>();
        
    
    /**
     * Constructs the engine, reading the configuration from the data directory.
     *
     * @throws IOException if an error occurs reading the configuration.
     */
    public MegaHAL() throws IOException {
        /*
         * 0. Initialise. Add the special "<BEGIN>" and "<END>" symbols to the
         * dictionary. Ex: 0:"<BEGIN>", 1:"<END>"
         *
         * NOTE: Currently debating the need for a dictionary.
         */
        //dictionary.add("<BEGIN>");
        //dictionary.add("<END>");

        //swapWords = Utils.readSymbolMapFromFile("C:/Users/anne/Documents/NetBeansProjects/ugleo/src/data/megahal.swp");
        //Set<Symbol> banWords = Utils.readSymbolSetFromFile("C:/Users/anne/Documents/NetBeansProjects/ugleo/src/data/megahal.ban");
        //Set<Symbol> auxWords = Utils.readSymbolSetFromFile("C:/Users/anne/Documents/NetBeansProjects/ugleo/src/data/megahal.aux");
        //swapWords = Utils.readSymbolMapFromFile("swap");
        
        rootPair = new HashMap<String,String>();
        
        swapWords = Utils.readStringMapFromFile("tb_swap");
        System.out.println("swap : "+swapWords.size());
        
        normWords = Utils.readStringMapFromFile("tb_norm");
        System.out.println("norm : "+normWords.size());
        
        //ban is stopword
        Set<String> banWords = Utils.readSymbolSetFromFile("ban");
        System.out.println("ban : "+banWords.size());
        
        //aux is word except stopword and non-word
        Set<String> auxWords = Utils.readSymbolSetFromFile("aux");
        System.out.println("aux : "+auxWords.size());
        
        // TODO: Implement first message to user (formulateGreeting()?)
        //Set<Symbol> greetWords = Utils.readSymbolSetFromFile("greet");
        stem = new Stemming();
        SymbolFactory symbolFactory = new SymbolFactory(new SimpleKeywordChecker(banWords, auxWords, normWords, stem));
        splitter = new WordNonwordSplitter(symbolFactory, swapWords); 
        
        kb = new maintenanceKB();
        model = new Model();
        

        /*BufferedReader reader = new BufferedReader(new FileReader("C:/Users/anne/Documents/NetBeansProjects/ugleo/src/data/megahal.trn"));
        String line;
        int trainCount = 0;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            trainOnly(line);
            trainCount++;
        }
        reader.close();
        System.out.println("Trained with " + trainCount + " sentences.");
        */
        
        String keyword = null, knowledge = null;
        int trainCount = 0, i = 0;
        try{  
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  

        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select * from tb_kb");
        
        //boolean hasKeyword = false;

        while(rs.next()){            
            //id_kb
            idx.add(rs.getString(1));
            //System.out.println("id train kb: "+idx.get(i));            
            
            //keyword = rs.getString(2);
            knowledge = rs.getString(3);
            System.out.println("knowledge: "+knowledge);

            trainOnly(knowledge.toUpperCase(), Integer.parseInt(idx.get(i)));
            /*
            //if there is no keyword exist in database (tb_kb)
            if (keyword == null) {
                //continue;
                System.out.println("Knowledge");
                hasKeyword = false;
                trainOnly(knowledge.toUpperCase(), Integer.parseInt(idx.get(i)), hasKeyword);
            } else {
                System.out.println("KEYWORD");
                hasKeyword = true;
                trainOnly(keyword.toUpperCase(), Integer.parseInt(idx.get(i)), hasKeyword);
            }
            */
            
            trainCount++;
            i++;
        }
        con.close();  
        System.out.println("Dilatih dengan " + trainCount + " kalimat.");

    }catch(Exception e){ System.out.println("Error in train KB connection : "+e);}
    }

    /**
     * Trains on a single line of text.
     *
     * @param userText the line of text.
     * @param id
     * @param hasKeyword
     */
    public void trainOnly(String userText, int id) {                
        // Split the user's line into symbols.
        List<Symbol> userWords = splitter.split(userText.toUpperCase());        
        /*
        //only keyword which can be trained into model
        List<Symbol> userKeywords = new ArrayList<Symbol>(userWords.size());        
        
        for (Symbol s : userWords) {
            if (s.isKeyword()) {
                userKeywords.add(s);
            }
        }          
        */
        
        this.rootPair = stem.getRootPair();
        // Train the brain from the user's list of symbols.
        model.train(userWords);
        //model.train(userKeywords);
        
        //save userText keyword in KB
        //kb.addKeyword(splitter.join(userKeywords), id);
        //kb.addKeyword(splitter.join(userWords, rootPair), id);
        //System.out.println("result: "+splitter.join(userKeywords));
        //System.out.println("result: "+splitter.join(userWords, rootPair));
    }

    /**
     * Formulates a line back to the user, and also trains from the user's text.
     *
     * @param userText the line of text.
     * @return the reply.
     */
    public String formulateReply(String userText) {
        //save user input into kb 
        //int id = kb.addKnowledge(userText);
        
        //boolean hasKeyword = false;
        
        // Split the user's line into symbols.
        List<Symbol> userWords = splitter.split(userText.toUpperCase());
        /*
        List<Symbol> userKeywords = new ArrayList<Symbol>(userWords.size());                
        
        // Find keywords in the user's input.        
        for (Symbol s : userWords) {
            if (s.isKeyword()) {
                userKeywords.add(s);
            }
        }                
        */
        
        this.rootPair = stem.getRootPair();
        
        //Save keyword into knowledge base
        //kb.addKeyword(splitter.join(userWords, rootPair), id);
        //kb.addKeyword(splitter.join(userKeywords), id);
        
        /*
        //swap userInput if needed
        for (Symbol s : userWords) {            
        //for (Symbol s : userKeywords) {
            boolean exist = swapWords.containsKey(s);
            if (exist == true){
                String [] swapArray = swapWords.get(s).split(" ");
                
                //swap userWords into the other words based on swapWords list
                for (int i = 0; i<swapArray.length; i++){
                    userWords.add(userWords.indexOf(s)+i, new Symbol(swapArray[i].toUpperCase(), false));
                    //userKeywords.add(userKeywords.indexOf(s)+i, new Symbol(swapArray[i].toUpperCase(), false));
                }
            }
        }
        */

        // Train the brain from the user's list of symbols.
        model.train(userWords);
        //model.train(userKeywords);
        //System.out.println("userWords: "+splitter.join(userKeywords));
        //System.out.println("userWords swap: "+splitter.join(userWords, rootPair));
        
        List<Symbol> userKeywords = new ArrayList<Symbol>(userWords.size());                
        
        // Find keywords in the user's input.        
        for (Symbol s : userWords) {
            if (s.isKeyword()) {
                userKeywords.add(s);
            }
        }                
        
        
        // Generate candidate replies.
        int candidateCount = 0;
        double bestInfoContent = 0.0;
        List<Symbol> bestReply = null;
        int timeToTake = 1000 * 5; // 5 seconds.
        long t0 = System.currentTimeMillis();
        while (System.currentTimeMillis() - t0 < timeToTake) {
            //System.out.print("Generating... ");
            List<Symbol> candidateReply = model.generateRandomSymbols(rng, userKeywords);
            candidateCount++;
            System.out.println("Kandidat\t: " + candidateReply);

            double infoContent = model.calculateInformation(candidateReply, userKeywords);
            System.out.println("Nilai Informasi\t: "+infoContent+"\n");
            
            if (infoContent > bestInfoContent && !Utils.equals(candidateReply, userWords)) {
                bestInfoContent = infoContent;
                bestReply = candidateReply;
            }
        }
        System.out.println("Menghasilkan "+candidateCount+" kandidat jawaban.");
        System.out.println("Nilai Informasi Tertinggi: " + bestInfoContent + "\n");

        // Return the generated string, tacked back together.
        
        String reply = null;
        if (bestReply != null) {
            //model.train(bestReply);
            reply=splitter.join(bestReply, rootPair);
            System.out.println("reply: "+reply);
            /*
            //get sentence from KB
            maintenanceKB kb = new maintenanceKB();
            reply = kb.getKB(reply);
            */
        }       
        
        return reply;
    }
}
