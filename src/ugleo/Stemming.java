/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ugleo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author anne
 */
public class Stemming {
     //Map<root, word>
     public Map<String,String> rootPair = new HashMap<String,String>();
    
     public List<String> vokal = new ArrayList<String>();
     public List<String> secondPrefix = new ArrayList<String>();
     public List<String> firstPrefix = new ArrayList<String>();
     public List<String> suffix = new ArrayList<String>();
     public List<String> pronoun = new ArrayList<String>();
     public List<String> particle = new ArrayList<String>();
     
     public Stemming(){          
        particle.add("KAH");
        particle.add("LAH");
        particle.add("PUN");
        
        pronoun.add("NYA");
        pronoun.add("KU");
        pronoun.add("MU");
                
        vokal.add("A");
        vokal.add("I");
        vokal.add("U");
        vokal.add("E");
        vokal.add("O");
        
        suffix.add("KAN");
        suffix.add("AN");
        suffix.add("I");
        
        secondPrefix.add("BER");
        secondPrefix.add("BEL");
        secondPrefix.add("BE");
        secondPrefix.add("PER");
        secondPrefix.add("PEL");
        secondPrefix.add("PE");
                                
        firstPrefix.add("MENG");
        firstPrefix.add("MENY");
        firstPrefix.add("MEN");
        firstPrefix.add("MEM");
        firstPrefix.add("ME");
        firstPrefix.add("PENG");
        firstPrefix.add("PENY");
        firstPrefix.add("PEN");
        firstPrefix.add("PEM");
        firstPrefix.add("DI");
        firstPrefix.add("TER");
        firstPrefix.add("KE");        
     }
     
     public Map<String, String> getRootPair(){
         return this.rootPair;
     }
 
     public String removeParticle(String input){
        //System.out.println("REMOVE PARTICLE");
        String subStr;
        int length = input.length();                                
        
        if (length > 3){
            subStr = input.substring(length - 3);
            int idx = pronoun.indexOf(subStr);
            
            if (idx != -1){
                subStr = input.substring(0, length - 3);
            } else { subStr = input; }
        } else {
            subStr = input;
        }
        //System.out.println("Remove particle: "+subStr);        
        subStr = removePronoun(subStr);
        
        return subStr;
    }
    
    public String removePronoun(String input){
        //System.out.println("REMOVE PRONOUN");
        String subStr;
        String output;
        int length = input.length();
        int index = 0;
        
         for (int i=2; i<4; i++){            
            if (i<length){
                subStr = input.substring(length-i);               
                if (pronoun.contains(subStr)){
                    index = i;
                } 
            } else { /*System.out.println("Jumlah huruf dalam satu kata minimal dua huruf.");*/ break; }
        }         

        if (index == 0){            
            output = input;            
        } else {
            subStr = input.substring(length-index);
            int idx = pronoun.indexOf(subStr);
            
            //nya ku mu
            if (idx != -1){
                output = input.substring(0, length-index);                        
            } else {
                output = input;
            }
        }
        //System.out.println("Remove pronoun: "+ output);
        output = funcFirstPrefix(output);
        
        return output;
    }
     
    public String removeSuffix(String input, String afiks, boolean boo){
        //System.out.println("REMOVE SUFFIX");
        String output;
        String subStr;
        int index = 0;
        int length = input.length();
        
        /*  kan except(ke peng)
            an except(di meng ter)
            i except(ber ke peng)
        */
        
        for (int i=2; i<4; i++){            
            if (i<length){
                subStr = input.substring(length-i);                
                if (suffix.contains(subStr)){
                    index = i;                    
                } 
            } else { /*System.out.println("Jumlah huruf dalam satu kata minimal dua huruf.");*/ break; }
        }         

        if (index == 0){            
            output = input;            
        } else {       
            subStr = input.substring(length-index);
            int idx = suffix.indexOf(subStr);
            //System.out.println(subStr);
            
            //System.out.println("remove suffix: "+input+"\t afiks: "+afiks);
            if ((afiks.equalsIgnoreCase("ke")) || (afiks.equalsIgnoreCase("peng"))){
                if (idx == 1){
                    output = input.substring(0, length-index);                
                } else {                
                    output = input;
                }
            } else if (afiks.equalsIgnoreCase("ber")){                    
                if (idx == 0){
                    output = input.substring(0, length-index);
                }            
                else if (idx == 1){
                    output = input.substring(0, length-index);
                } else {                
                    output = input;
                }
            } else if ((afiks.equalsIgnoreCase("di")) || (afiks.equalsIgnoreCase("meng"))
                    || (afiks.equalsIgnoreCase("ter"))){            
                if (idx == 0){
                    output = input.substring(0, length-index);
                } else if (idx == 2){
                    output = input.substring(0, length-index);
                } else {
                    output = input;
                }
            } else {
                if (idx != -1){
                    output = input.substring(0, length-index);
                } else {
                    output = input;
                }
            }
        }
        //System.out.println("output suffix: "+output);
        if (boo == true) {
            output = funcSecondPrefix(output, true);
        }

        return output;
    }
    
    public String funcFirstPrefix(String input){
        //System.out.println("FIRST PREFIX");
        String subStr;
        String output;
        int index = 0;
        boolean exist = true;
        int length = input.length();                
                
        for (int i=2; i<5; i++){            
            if (i<length){
                subStr = input.substring(0, i);                
                if (firstPrefix.contains(subStr)){
                    index = i;                    
                } 
            } else { /*System.out.println("Jumlah huruf dalam satu kata minimal dua huruf.");*/ break; }
        }         

        if (index == 0){
            exist = false;
            output = input;
            output = funcSecondPrefix(output, exist);
        } else {        
            subStr = input.substring(0, index);
            int idx = firstPrefix.indexOf(subStr);
            //System.out.println("prefix: "+subStr+" \t idx: "+idx);

            //"meny" and "peny"
            if (((idx == 1) || (idx == 6)) && (vokal.contains(String.valueOf(input.charAt(4))))){
                output = input.substring(index);
                output = "s" + output;
            }//"meng" and "peng"
            else if (((idx == 0)||(idx == 5)) && (vokal.contains(String.valueOf(input.charAt(4))))){
                output = input.substring(index);
                output = "k" + output;
            }//"men" and "pen"
            else if (((idx == 2) || (idx == 7)) && (vokal.contains(String.valueOf(input.charAt(3))))){
                output = input.substring(index);
                output = "t" + output;                
            }//"mem" and "pem"
            else if (((idx == 3) && (vokal.contains(String.valueOf(input.charAt(3)))))
                    || ((idx == 8) && (vokal.contains(String.valueOf(input.charAt(3)))))){
                output = input.substring(index);
                output = "p" + output;                
            }//me, di, ter, ke
            else if (idx != -1){
                output = input.substring(index);
            }//kata depan "di" dan "ke"
            else {
                output = input;
            }
            //System.out.println("first prefix: "+output);
            output = removeSuffix(output, subStr, exist);                    
        }
        return output;
    }
      
    public String funcSecondPrefix(String input, boolean rootWord){
        //System.out.println("SECOND PREFIX");
        String subStr = null;
        String output;
        int index = 0;
        int length = input.length();
        
        for (int i=2; i<4; i++){
            if (i<length){
                subStr = input.substring(0, i);                
                if (secondPrefix.contains(subStr)){
                    index = i;
                    //System.out.println("subStr: "+subStr);
                } 
            } else {/*System.out.println("Jumlah huruf dalam satu kata minimal dua huruf.");*/ break; }
        }
        
        if (index == 0){
            output = input;            
        } else {
            //System.out.println("index: "+index);
            
            subStr = input.substring(0, index);
            int idx = secondPrefix.indexOf(subStr);
            //System.out.println("second prefix: "+subStr+" \t idx: "+idx);
               
                //ber + vokal dan per + vokal
                if (((idx == 3) || (idx == 0)) /*&& (vokal.contains(String.valueOf(input.charAt(3))))){*/){
                    output = input.substring(index);
                } 
                //bel + ajar dan pel + ajar
                else if (((idx == 4) || (idx == 1)) && (input.substring(3, 6).equalsIgnoreCase("ajar"))){
                    output = input.substring(index);
                }//be dan pe
                else if ((idx == 2) || (idx == 5)){
                    output = input.substring(index);
                }//kata benda "bel" dan "pel"
                else {
                    output = input;
                }
        }
        
        //System.out.println("second output: "+output);
        if (rootWord == false){
            output = removeSuffix(output, subStr, false);
        }
        return output;
    }
    
    public String run(String input){        
        String output = removeParticle(input);
        
        if (output != null) {
            //root, word
            rootPair.put(output, input);
            //System.out.println("rootPair: "+output+ " "+rootPair.get(output));
        }
        return output;
    }
     
}
