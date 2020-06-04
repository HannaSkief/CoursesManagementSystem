/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ASUS
 */
public class StudentDao {
    
    public static void insert(Student student){
        String sql="insert into students (name,phone,info,created_at) values (?,?,?,julianday('now','localtime'))";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, student.getName());
            stat.setString(2, student.getPhone());
            stat.setString(3, student.getInfo());
            
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void update(Student student){
        String sql="update students set name=?,phone=?,info=? where id=?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, student.getName());
            stat.setString(2, student.getPhone());
            stat.setString(3, student.getInfo());
            stat.setLong(4, student.getId());
            
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static boolean isExist(Student student){
        
        String sql="select * from students where name=? and id!=?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, student.getName());
            stat.setLong(2, student.getId());
            
            ResultSet result=stat.executeQuery();
            if(result.next())
                return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return false;
    }
    
    
    public static ObservableList<Student> getStudents(String name){
        
        String n="%"+name+"%";
        
        ObservableList<Student> studentList=FXCollections.observableArrayList();
        String sql="select *,date(created_at) as date from students where name like ?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, n);
            
            ResultSet result=stat.executeQuery();
            while(result.next()){
                Student s=new Student();
                s.setId(result.getLong("id"));
                s.setName(result.getString("name"));
                s.setPhone(result.getString("phone"));
                s.setInfo(result.getString("info"));
                s.setCreatedAt(result.getString("date"));
                
                studentList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return studentList;
    }

    public static boolean canDelete(Student student) {
        String sql="select * from course_student where student_id="+student.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    public static void delete(Student student) {
        String sql="delete from students where id="+student.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    
}
