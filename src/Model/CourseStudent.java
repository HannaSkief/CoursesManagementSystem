/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.CourseStudentDao;

/**
 *
 * @author ASUS
 */
public class CourseStudent {
    
    private long courseId;
    private String subjectName;
    private long studentId;
    private String studentName;
    private String date;
    private double deservedAmount;
    private double paidAmount;
    private double remainAmount;
    private String status;

    public CourseStudent(long courseId, long studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public CourseStudent() {
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDeservedAmount() {
        return deservedAmount;
    }

    public void setDeservedAmount(double deservedAmount) {
        this.deservedAmount = deservedAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(double remainAmount) {
        this.remainAmount = remainAmount;
    }
    

    public void insert() {
        CourseStudentDao.insert(this);
    }

    public boolean isExist() {
        return CourseStudentDao.isExist(this);
    }

    public void update() {
        CourseStudentDao.update(this);
    }

    public void delete() {
        CourseStudentDao.delete(this);
    }

    public boolean canDelete() {
        return CourseStudentDao.canDelete(this);
    }
    
    
}
