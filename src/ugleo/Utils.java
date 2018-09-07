package ugleo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.sql.*;  
/**
 * Class for static utility methods.
 *
 * @author Trejkaz
 */
public class Utils {

    /*
    public static Map<Symbol,Symbol> readSymbolMapFromFile(String filename) throws IOException {
        Map<Symbol,Symbol> map = new HashMap<Symbol,Symbol>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            String[] words = line.split("\t");
            if (words.length != 2) {
                continue;
            }
            map.put(new Symbol(words[0], false), new Symbol(words[1], false));
        }
        reader.close();

        return map;
    }
    */
    public static Map<Symbol,Symbol> readSymbolMapFromFile(String filename) throws IOException {
        Map<Symbol,Symbol> map = new HashMap<Symbol,Symbol>();

        try{
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  
        
        Statement stmt=con.createStatement();  

        ResultSet rs=stmt.executeQuery("select * from "+filename);
        String line;
        while(rs.next()){  
            line = rs.getString(2).trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }           
            map.put(new Symbol(rs.getString(2).toUpperCase(), false), new Symbol(rs.getString(3).toUpperCase(), false));
        }
        con.close();
        }
        catch(Exception e){ System.out.println(e);}  
        return map;
    }
    
    public static Map<Symbol,String> readSymbolStringMapFromFile(String filename) throws IOException {
        Map<Symbol,String> map = new HashMap<Symbol,String>();

        try{
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  
        
        Statement stmt=con.createStatement();  

        ResultSet rs=stmt.executeQuery("SELECT tb_word.word, tb_swap.swap " +
                "FROM tb_swap INNER JOIN tb_word ON tb_word.id_word = tb_swap.id_word");
        
        String line;
        while(rs.next()){  
            line = rs.getString(1).trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }           
            map.put(new Symbol(rs.getString(1).toUpperCase(), false), rs.getString(2).toUpperCase());
        }
        con.close();
        }
        catch(Exception e){ System.out.println(e);}  
        return map;
    }
    
    public static Map<String,String> readStringMapFromFile(String filename) throws IOException {
        Map<String,String> map = new HashMap<String,String>();

        try{
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  
        
        Statement stmt=con.createStatement();  

        String sql = null;
        
        if (filename.equalsIgnoreCase("tb_norm")){
            sql = "SELECT tb_norm.not_normword, tb_word.word " +
                "FROM tb_norm INNER JOIN tb_word ON tb_word.id_word = tb_norm.id_word";            
        } else if (filename.equalsIgnoreCase("tb_swap")){
            sql = "SELECT tb_word.word, tb_swap.swap " +
                "FROM tb_swap INNER JOIN tb_word ON tb_word.id_word = tb_swap.id_word" ;
        } else {
            System.out.println("Tabel tidak ditemukan");
        }
        ResultSet rs=stmt.executeQuery(sql);
        
        String line;
        while(rs.next()){  
            line = rs.getString(1).trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }           
            map.put(rs.getString(1).toUpperCase(), rs.getString(2).toUpperCase());
        }
        con.close();
        }
        catch(Exception e){ System.out.println(e);}  
        return map;
    }    
    
    public static Set<String> readSymbolSetFromFile(String type) throws IOException {
        HashSet<String> set = new HashSet<String>();

        try{  
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  
        
        Statement stmt=con.createStatement();  
        ResultSet rs = null;
        
        if (type.equalsIgnoreCase("ban")){
            rs=stmt.executeQuery("SELECT * FROM tb_word WHERE id_typeword = 1 ");
        } else {
            rs=stmt.executeQuery("SELECT * FROM tb_word WHERE id_typeword = 2 ");
        }
        
        String line;
        while(rs.next()){  
        line = rs.getString(2).trim().toUpperCase();        
        
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            //set.add(new Symbol(line, false));
            set.add(line);
        }
        con.close();          

        }catch(Exception e){ System.out.println(e);}  
        
        return set;        
    }
    /*
    public static Set<Symbol> readSymbolSetFromFile(String filename) throws IOException {
        HashSet<Symbol> set = new HashSet<Symbol>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            set.add(new Symbol(line, false));
        }
        reader.close();

        return set;
    }
    */
    public static boolean equals(List l1, List l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        Iterator il1 = l1.iterator();
        Iterator il2 = l2.iterator();
        while (il1.hasNext()) {
            if (!il1.next().equals(il2.next())) {
                return false;
            }
        }
        return true;
    }
}
