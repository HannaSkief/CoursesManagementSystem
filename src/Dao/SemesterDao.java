/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.Semester;
import Model.Year;
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
public class SemesterDao {

    public static long insert(Semester semester) {

        long id=1;
        
        String sql="insert into semesters (year_id,name)values(?,?)";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setLong(1, semester.getYearId());
            stat.setString(2, semester.getName());
            stat.executeUpdate();
            
            sql="select id from semesters order by id desc limit 1";
            Statement stat2=con.createStatement();
            ResultSet result=stat2.executeQuery(sql);
            if(result.next()){
                id=result.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SemesterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;


    }
    public static void update(Semester semester){
        String sql="update semesters set name =? where id=?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, semester.getName());
            stat.setLong(2, semester.getId());
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SemesterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static ObservableList<Semester> getSemesterByYear(Year year){
        ObservableList<Semester> semesterList=FXCollections.observableArrayList();
        String sql="select * from semesters where year_id="+year.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                semesterList.add(new Semester(result.getLong("id"),result.getString("name"),result.getLong("year_id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SemesterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return semesterList;
    }
   

    public static boolean isExist(Semester semester) {

        String sql="select * from semesters where name='"+semester.getName()+"' and year_id="+semester.getYearId()+" and id!="+semester.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        

    }

    public static boolean canDelete(Semester semester) {
        String sql="select * from subjects where semester_id="+semester.getId()+" ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SemesterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static void delete(Semester semester) {
        String sql="delete from semesters where id ="+semester.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(SemesterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    
}
