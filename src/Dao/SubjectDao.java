/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.Semester;
import Model.Subject;
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
public class SubjectDao {

    
    public static long insert(Subject subject) {

         long id=1;
        
        String sql="insert into subjects (semester_id,name)values(?,?)";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setLong(1, subject.getSemesterId());
            stat.setString(2, subject.getName());
            stat.executeUpdate();
            
            sql="select id from subjects order by id desc limit 1";
            Statement stat2=con.createStatement();
            ResultSet result=stat2.executeQuery(sql);
            if(result.next()){
                id=result.getLong("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return id;
    }
    
    
    public static boolean isExist(Subject s) {

        String sql="select * from subjects where name='"+s.getName()+"' and semester_id="+s.getSemesterId()+" and id!="+s.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return true;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }
    
    public static ObservableList<Subject> getAllSubject(Semester semester){
        ObservableList<Subject> subjectList=FXCollections.observableArrayList();
        String sql="select * from subjects where semester_id="+semester.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                subjectList.add(new Subject(result.getLong("id"),result.getString("name"),result.getLong("semester_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return subjectList;
    }
    
    public static ObservableList<Subject> getAllSubjectBuName(String name){
        String s="%"+name+"%";
        ObservableList<Subject> subjectList=FXCollections.observableArrayList();
        String sql="select * from subjects where name like ?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, s);
            ResultSet result=stat.executeQuery();
            while(result.next()){
                subjectList.add(new Subject(result.getLong("id"),result.getString("name"),result.getLong("semester_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return subjectList;
    }

    

    public static void update(Subject subject) {
        String sql="update subjects set name=? where id=?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, subject.getName());
            stat.setLong(2, subject.getId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean canDelete(Subject subject) {
        String sql="select * from courses where subject_id="+subject.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    public static void delete(Subject subject) {
        String sql="delete from subjects where id="+subject.getId()+" ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
