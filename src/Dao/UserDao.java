/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

/**
 *
 * @author ASUS
 */
import Additional.Connect;
import Model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class UserDao {
    
    
    public static void insert(User user){
        
        try {
            String sql="insert into users (name,password,role,archive) values(?,?,?,?)";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setString(1, user.getName().trim());
            stat.setString(2, user.getPassword().trim());
            stat.setString(3,user.getRole());
            stat.setString(4,"no");
            
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    public static void update(User user){
        try {
            String sql="update users set name=?,password=?,role=? where id=?";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setString(1, user.getName().trim());
            stat.setString(2, user.getPassword().trim());
            stat.setString(3,user.getRole());
            stat.setLong(4, user.getId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void archiveUser(User user){
        
        String sql="update users set archive='yes' where id="+user.getId()+"";
        try {
            Connection con=Connect.getConnection();
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public static boolean checkIfExist(User user){
        
        String sql="select * from users where name='"+user.getName().trim()+"' and id!="+user.getId();
        try {
            Connection con =Connect.getConnection();
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }
    public static void delete(User user){
        
        String sql="delete from users where id="+user.getId()+"";
        try {
            Connection con=Connect.getConnection();
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    
    public static ObservableList<User> getUser(String name){
        ObservableList<User> users=FXCollections.observableArrayList();
        
        String sql="select * from users where archive='no' and name like '%"+name+"%'";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                User user=new User();
                user.setId(result.getInt(1));
                user.setName(result.getString(2));
                user.setPassword(result.getString(3));
                user.setRole(result.getString(4));
                
                users.add(user);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return users;
    }
    
    public static void login(String username,String password,LoginResult loginResult){
        
        String sql="select * from users where name='"+username+"' and archive='no' ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            User user=null;
            while(result.next()){
                user=new User();
                user.setId(result.getInt(1));
                user.setName(result.getString(2));
                user.setPassword(result.getString(3));
                user.setRole(result.getString(4));
            }
            if(user==null){
                loginResult.wrongUsername("اسم مستخدم خاطئ !");
            }
            else if(!user.getPassword().equals(password)){
                loginResult.wrongPassword("كلمة السر خاطئة !");
            }
            else
                loginResult.done(user);
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static boolean canDelete(User user) {
        String sql="select * from course_payment where user_id="+user.getId()+" ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
            
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    

    
    
    public interface LoginResult{
        
        public void done(User user);
        public void wrongUsername(String s);
        public void wrongPassword(String s);
        
    }
    
    
}
