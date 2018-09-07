/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ugleo;

import java.util.Map;

/**
 *
 * @author anne
 */
public class Normalization {

    private Map normal;
    
    public Normalization (Map normal){
        this.normal = normal;
    }
        
    //normalization process
    String doNormalization(String text) {
        //get the normal text when text is in normal list
        //System.out.println("Norm: "+text);
        if (isNormalExist(text) == true){
            text = normal.get(text.toUpperCase()).toString();
            //System.out.println("Norm2: "+text);
        } 
        return text;
    }
    
    //check if text must have normalization process
    boolean isNormalExist(String text){
        return normal.containsKey(text.toUpperCase());
    }
    
}
