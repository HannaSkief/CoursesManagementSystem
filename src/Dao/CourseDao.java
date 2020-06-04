/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.Course;
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
public class CourseDao {
    
    
    public static void insert(Course course){
        
        try {
            String sql="insert into courses (subject_id,status,c_date,cost) values(?,?,julianday('now','localtime'),?)";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setLong(1, course.getSubjectId());
            stat.setString(2, course.getStatus().trim());
            stat.setDouble(3,course.getCost());
            
            stat.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    
    
    public static void update(Course course){
        try {
            String sql="update courses set status=?,cost=? where id=?";
            Connection con =Connect.getConnection();
            PreparedStatement stat=con.prepareStatement(sql);
            
            stat.setString(1, course.getStatus());
            stat.setDouble(2, course.getCost());
            stat.setLong(3, course.getId());
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    public static void delete(Course course){
        
        String sql="delete from courses where id="+course.getId()+"";
        try {
            Connection con=Connect.getConnection();
            Statement stat=con.createStatement();
            stat.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    public static ObservableList<Course> getOpenCourses(String name){
        ObservableList<Course> courses=FXCollections.observableArrayList();
        
        String n="%"+name+"%";
        
        String sql="select c.*,date(c.c_date) as date,s.name as subject_name"
                + " from courses c"
                + " left join subjects s"
                + " on c.subject_id=s.id"
                + " where c.status ='غير منتهية' and subject_name like ?";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement p = con.prepareStatement(sql);
            p.setString(1, n);
            
           
            ResultSet result=p.executeQuery();
            while(result.next()){
                Course course=new Course();
                course.setId(result.getInt("id"));
                course.setSubjectId(result.getInt("subject_id"));
                course.setStatus(result.getString("status"));
                course.setDate(result.getString("date"));
                course.setCost(result.getDouble("cost"));
                course.setSubjectName(result.getString("subject_name"));
                
                courses.add(course);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return courses;
    }
    
    public static ObservableList<Course> getOpenCourses(long studentId){
        ObservableList<Course> courses=FXCollections.observableArrayList();
        
        String n="%"+""+"%";
        
        String sql="select c.*,date(c.c_date) as date,s.name as subject_name"
                + " from courses c"
                + " left join subjects s"
                + " on c.subject_id=s.id"
                + " where c.status ='غير منتهية' and subject_name like ? "
                + " and c.id not in(select course_id from course_student where student_id="+studentId+" )";
        Connection con=Connect.getConnection();
        try {
            PreparedStatement p = con.prepareStatement(sql);
            p.setString(1, n);
            
           
            ResultSet result=p.executeQuery();
            while(result.next()){
                Course course=new Course();
                course.setId(result.getInt("id"));
                course.setSubjectId(result.getInt("subject_id"));
                course.setStatus(result.getString("status"));
                course.setDate(result.getString("date"));
                course.setCost(result.getDouble("cost"));
                course.setSubjectName(result.getString("subject_name"));
                course.setCbSelect();
                courses.add(course);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        return courses;
    }
    
    public static ObservableList<Course> getAllCourses(long subjectId){
        ObservableList<Course> courseList=FXCollections.observableArrayList();
        String sql="select *,date(c_date) as date ,"
                + " course_info.number_of_student as num_of_student,"
                + " course_info.deserved_amount as des_amount,"
                + " course_info.recived_amount as rec_amount from courses "
                + " INNER JOIN course_info on courses.id=course_info.course_id "
                + " where subject_id="+subjectId+"  order by id desc";
        
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                Course course=new Course();
                course.setId(result.getLong("id"));
                course.setSubjectId(subjectId);
                course.setStatus(result.getString("status"));
                course.setDate(result.getString("date"));
                course.setCost(result.getDouble("cost"));
                course.setNumberOfStudent(result.getInt("num_of_student"));
                course.setDeservedAmount(result.getDouble("des_amount"));
                course.setRecivedAmount(result.getDouble("rec_amount"));
                courseList.add(course);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return courseList;
    }
    
     public static ObservableList<Course> getAllCoursesByCondition(String condition){
        ObservableList<Course> courseList=FXCollections.observableArrayList();
        String sql="select courses.*,date(c_date) as date ,"
                + " course_info.number_of_student as num_of_student,"
                + " course_info.deserved_amount as des_amount,"
                + " course_info.recived_amount as rec_amount, subjects.name as subjectName from courses "
                + " INNER JOIN course_info on courses.id=course_info.course_id "
                + " INNER JOIN subjects on courses.subject_id=subjects.id 	 "
                + " where "+condition+"";
        
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                Course course=new Course();
                course.setId(result.getLong("id"));
                course.setStatus(result.getString("status"));
                course.setDate(result.getString("date"));
                course.setCost(result.getDouble("cost"));
                course.setNumberOfStudent(result.getInt("num_of_student"));
                course.setDeservedAmount(result.getDouble("des_amount"));
                course.setRecivedAmount(result.getDouble("rec_amount"));
                course.setSubjectName(result.getString("subjectName"));
                courseList.add(course);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return courseList;
    }
     public static Course getSummaryByCondition(String condition){
        Course course=new Course();
        String sql="select sum(course_info.number_of_student) as num_of_student,"
                + " sum( course_info.deserved_amount )as des_amount,"
                + " sum( course_info.recived_amount )as rec_amount  from courses "
                + " INNER JOIN course_info on courses.id=course_info.course_id "
                + " where "+condition+"";
        
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                course.setNumberOfStudent(result.getInt("num_of_student"));
                course.setDeservedAmount(result.getDouble("des_amount"));
                course.setRecivedAmount(result.getDouble("rec_amount"));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return course;
    }

    public static boolean canAddCourse(Course course) {

        String sql="select status from courses where subject_id="+course.getSubjectId()+" order by id desc limit 1";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                if(result.getString("status").equals("غير منتهية"))
                    return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return true;
    }

    public static boolean isLastCourse(Course course) {

        String sql="select * from courses where subject_id="+course.getSubjectId()+" and id >"+course.getId()+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    public static boolean canDelete(Course course) {
        String sql="select * from course_student where course_id="+course.getId()+" ";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next())
                return false;
            
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
}
