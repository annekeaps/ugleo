/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ugleo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author anne
 */
public class maintenanceKB {
    
    private Connection con;
    
    public maintenanceKB (){
        
    }
    
    public void connect(){
        try{
        Class.forName("com.mysql.jdbc.Driver");  

        con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  
                
        }
        catch(Exception e){ System.out.println("gagal connect : "+e);}   
    }
    
    //add KB sentence into train file
    public int addKnowledge(String text){
        int id = 0;
        connect();
        try{
            Statement stmt=con.createStatement();

            stmt.executeUpdate("INSERT INTO tb_kb VALUES (NULL, NULL, '"+ text +"')", Statement.RETURN_GENERATED_KEYS);                        
                           
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                id = rs.getInt(1);
            }
            System.out.println("rs: "+id);
           
            con.close();
        }
        catch(Exception e){ System.out.println("Gagal di add knowledge : "+e);}
        return id;
    }
    
    //add KB keyword into train file
    public void addKeyword(String keyword, int id){
        connect();
        try{
            Statement stmt=con.createStatement();  

            stmt.executeUpdate("UPDATE tb_kb SET keyword = '"+ keyword +"' WHERE id_kb = "+id);

            con.close();
        }            
        catch(Exception e){ System.out.println("Gagal di add keyword : "+e);}
    }
    
    //get sentence in train file
    public String getKB(String keyword){
        String reply = null;
        System.out.println("keyword : "+ keyword);
        connect();
        
        try{
            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("SELECT knowledge FROM tb_kb WHERE keyword = '"+keyword+"'");
            while (rs.next()){
            reply = rs.getString(1);
            System.out.println("reply : "+ reply);
            }
            con.close();
        }            
        catch(Exception e){ System.out.println("Gagal get KB : "+e);}
        
        return reply;
    }
}
