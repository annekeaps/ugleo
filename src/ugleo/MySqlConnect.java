/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ugleo;
import java.sql.*;  

/**
 *
 * @author anne
 */
public class MySqlConnect {            
    //public static void main(String args[]){  
    public void Connect(){
    try{  
        Class.forName("com.mysql.jdbc.Driver");  

        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/ugleo","root","12345");  

        /*
        Statement stmt=con.createStatement();  

        ResultSet rs=stmt.executeQuery("select * from ban");  

        while(rs.next())  
        System.out.println(rs.getString(2));

        con.close();  
        */
    }catch(Exception e){ System.out.println(e);}  

    }
}
