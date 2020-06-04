/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Additional.Connect;
import Model.StudentMovement;
import java.sql.Connection;
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
public class StudentMovementDao {
    
    public static ObservableList<StudentMovement> getAll(long studentId){
        ObservableList<StudentMovement> movementsList=FXCollections.observableArrayList();
        String sql="select courses.id as id ,subjects.name as name,courses.status as courseStatus,date(courses.c_date) as date,"
                + " course_student.deserved_amount as deservedAmount,course_student.paid_amount as paidAmount,"
                + " course_student.status as studentStatus,date(course_student.added_at) as addedAt from courses "
                + " INNER JOIN subjects on courses.subject_id=subjects.id "
                + " INNER JOIN course_student on courses.id=course_student.course_id "
                + " where course_student.student_id="+studentId+" ORDER by course_student.added_at DESC";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            while(result.next()){
                StudentMovement sm=new StudentMovement();
                sm.setCourseId(result.getLong("id"));
                sm.setSubjectName(result.getString("name"));
                sm.setCourseStatus(result.getString("courseStatus"));
                sm.setCourseDate(result.getString("date"));
                sm.setDeservedAmount(result.getDouble("deservedAmount"));
                sm.setPaidAmount(result.getDouble("paidAmount"));
                sm.setRemainAmount(sm.getDeservedAmount()-sm.getPaidAmount());
                sm.setStudentStatus(result.getString("studentStatus"));
                sm.setAddedAt(result.getString("addedAt"));
                
                movementsList.add(sm);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentMovementDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return movementsList;
    }
    
    public static StudentMovement getSummary(long studentId){
        StudentMovement summary=new StudentMovement();
        String sql="select sum(deserved_amount) as desAmount,sum(paid_amount) as paidAmount from course_student where student_id="+studentId+"";
        Connection con=Connect.getConnection();
        try {
            Statement stat=con.createStatement();
            ResultSet result=stat.executeQuery(sql);
            if(result.next()){
                summary.setSubjectName("المجموع");
                summary.setDeservedAmount(result.getDouble("desAmount"));
                summary.setPaidAmount(result.getDouble("paidAmount"));
                summary.setRemainAmount(summary.getDeservedAmount()-summary.getPaidAmount());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentMovementDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return summary;
        
    }
    
}
