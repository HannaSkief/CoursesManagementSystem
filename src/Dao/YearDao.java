/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
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
public class YearDao {
    
    public static long insert(Year year){
     long id=1;
        String sql="insert into years (name) values(?)";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, year.getName());
            stat.executeUpdate();
            
            sql="select id from years order by id desc limit 1";
            Statement stat2=con.createStatement();
            ResultSet result=stat2.executeQuery(sql);
            if(result.next()){
                id=result.getLong(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return id;
    }
    
    public static void update(Year year){
        String sql="update years set name=? where id =?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setString(1, year.getName());
            stat.setLong(2, year.getId());
            stat.executeUpdate();            
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static boolean isExist(Year year){
        String sql="select * from years where name='"+year.getName()+"' and id!="+year.getId()+"";
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

    public static ObservableList<Year> getYears() {

        ObservableList<Year> yearList=FXCollections.observableArrayList();
        String sql="select * from years ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                yearList.add(new Year(result.getLong("id"), result.getString("name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return yearList;
    }

    public static boolean canDelete(Year year) {
        String sql="select * from semesters where year_id="+year.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    public static void delete(Year year) {
        String sql="delete from years where id="+year.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(YearDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
