/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.CourseStudent;
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
public class CourseStudentDao {
    
    public static void insert(CourseStudent courseStudent){
        
        try {
            String sql="insert into course_student (course_id,student_id,deserved_amount,paid_amount,status,added_at) values(?,?,?,?,?,julianday('now','localtime'))";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setLong(1, courseStudent.getCourseId());
            stat.setLong(2, courseStudent.getStudentId());
            stat.setDouble(3, courseStudent.getDeservedAmount());
            stat.setDouble(4,courseStudent.getPaidAmount());
            stat.setString(5,courseStudent.getStatus());
            
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    
    
    public static void update(CourseStudent courseStudent){
        try {
            String sql="update course_student set course_id=?,student_id=?,deserved_amount=?,paid_amount=?,status=? where course_id=? and student_id=?";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
             stat.setLong(1, courseStudent.getCourseId());
            stat.setLong(2, courseStudent.getStudentId());
            stat.setDouble(3, courseStudent.getDeservedAmount());
            stat.setDouble(4,courseStudent.getPaidAmount());
            stat.setString(5,courseStudent.getStatus());
            stat.setLong(6, courseStudent.getCourseId());
            stat.setLong(7, courseStudent.getStudentId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void addPayment(long courseId,long studentId,double amount){
        try {
            String sql="update course_student set paid_amount=paid_amount+? where course_id=? and student_id=?";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setDouble(1, amount);
            stat.setLong(2, courseId);
            stat.setLong(3, studentId);
            
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void delete(CourseStudent courseStudent){
        
        String sql="delete from course_student where course_id="+courseStudent.getCourseId()+" and student_id="+courseStudent.getStudentId()+"";
        try {
            Connection con=Connect.getConnection();
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    public static ObservableList<CourseStudent> getCourseStudents(String name,long id){
        ObservableList<CourseStudent> courseStudents=FXCollections.observableArrayList();
        
        String n="%"+name+"%";
        String sql="select c.*,s.name as subject_name,cs.student_id,cs.deserved_amount as deserved_amount,"
                + " cs.paid_amount as paid_amount,cs.status as student_status,date(cs.added_at) as added_at,stu.name as student_name"
                + " from courses c"
                + " left join subjects s"
                + " on c.subject_id=s.id"
                + " left join course_student cs"
                + " on c.id=cs.course_id"
                + " left join students stu"
                + " on cs.student_id=stu.id"
                + " where c.id="+id+" and student_name like ?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement p = con.prepareStatement(sql);
            p.setString(1, n);            
            ResultSet result=p.executeQuery();
            while(result.next()){
                CourseStudent courseStudent=new CourseStudent();
                courseStudent.setCourseId(result.getInt("id"));
                courseStudent.setSubjectName(result.getString("subject_name"));
                courseStudent.setStudentId(result.getInt("student_id"));
                courseStudent.setStudentName(result.getString("student_name"));
                courseStudent.setStatus(result.getString("student_status"));
                courseStudent.setDate(result.getString("added_at"));
                courseStudent.setDeservedAmount(result.getDouble("deserved_amount"));
                courseStudent.setPaidAmount(result.getDouble("paid_amount"));
                courseStudent.setRemainAmount(courseStudent.getDeservedAmount()-courseStudent.getPaidAmount());
                courseStudents.add(courseStudent);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return courseStudents;
    }
    
    
    public static boolean isExist(CourseStudent courseStudent){
        
        String sql="select * from course_student where course_id=? and student_id=?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement stat=con.prepareStatement(sql);
            stat.setLong(1, courseStudent.getCourseId());
            stat.setLong(2, courseStudent.getStudentId());
            
            ResultSet result=stat.executeQuery();
            if(result.next())
                return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return false;
    }

    public static boolean canDelete(CourseStudent cs) {
        String sql="select * from course_payment where course_id="+cs.getCourseId()+" and student_id="+cs.getStudentId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(CourseStudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
