/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.Course;
import Model.CoursePayment;
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
public class CoursePaymentDao {
    
    public static void insert(CoursePayment coursePayment){
        String sql="insert into course_payment (course_id,student_id,amount,note,p_date,user_id)values"
                + "(?,?,?,?,julianday('now','localtime'),?)";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setLong(1, coursePayment.getCourseId());
            stat.setLong(2, coursePayment.getStudentId());
            stat.setDouble(3, coursePayment.getAmount());
            stat.setString(4, coursePayment.getNote());
            stat.setLong(5, coursePayment.getUserId());
            stat.executeUpdate();
        
        } catch (SQLException ex) {
            Logger.getLogger(CoursePaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    public static ObservableList<CoursePayment> getPayments(long courseId,long studentId){
        ObservableList<CoursePayment> paymentList=FXCollections.observableArrayList();
        
        String sql="select course_payment.id as id ,abs(course_payment.amount) as amount ,"
                + " course_payment.note as note ,date(course_payment.p_date) as date, "
                + " users.name as userName ,case  when amount>0 then 'قبض' else 'ترجيع' end as type"
                + " FROM course_payment "
                + " INNER JOIN users on course_payment.user_id=users.id "
                + " where course_payment.course_id="+courseId+" and course_payment.student_id="+studentId+" ";
        
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                CoursePayment cp=new CoursePayment();
                cp.setId(result.getLong("id"));
                cp.setAmount(result.getDouble("amount"));
                cp.setNote(result.getString("note"));
                cp.setDate(result.getString("date"));
                cp.setUserName(result.getString("userName"));
                cp.setType(result.getString("type"));
                paymentList.add(cp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoursePaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return paymentList;
    }
    public static ObservableList<CoursePayment> getPayments(String fromDate,String toDate){
        ObservableList<CoursePayment> paymentList=FXCollections.observableArrayList();
        
        String sql="select course_payment.id as id ,abs(course_payment.amount) as amount ,"
                + " course_payment.note as note ,date(course_payment.p_date) as date, "
                + " users.name as userName ,case  when amount>0 then 'قبض' else 'ترجيع' end as type ,"
                + " students.name as studentName ,subjects.name as subjectName FROM course_payment "
                + " INNER JOIN users on course_payment.user_id=users.id "
                + " Inner join students on course_payment.student_id=students.id "
                + " Inner join courses on course_payment.course_id=courses.id "
                + " Inner join subjects on courses.subject_id=subjects.id "
                + " where date(course_payment.p_date) between '"+fromDate+"' and '"+toDate+"' ";
        
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                CoursePayment cp=new CoursePayment();
                cp.setId(result.getLong("id"));
                cp.setAmount(result.getDouble("amount"));
                cp.setNote(result.getString("note"));
                cp.setDate(result.getString("date"));
                cp.setUserName(result.getString("userName"));
                cp.setType(result.getString("type"));
                cp.setStudentName(result.getString("studentName"));
                cp.setSubjectName(result.getString("subjectName"));
                paymentList.add(cp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoursePaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return paymentList;
    }
    
    
    public static CoursePayment getSummary(String fromDate,String toDate){
        CoursePayment cp=new CoursePayment();
        cp.setType("المجموع");
        
        
        String sql="select sum(amount) as amount from course_payment where date(p_date) between '"+fromDate+"' and '"+toDate+"'";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                cp.setAmount(result.getDouble("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoursePaymentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cp;
    }
    
    
    
}
