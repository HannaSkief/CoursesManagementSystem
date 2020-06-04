/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Additional.DialogBuilder;
import Dao.CourseDao;
import com.jfoenix.controls.JFXCheckBox;
import java.util.Arrays;
import javafx.geometry.NodeOrientation;

/**
 *
 * @author ASUS
 */
public class Course {
    
    private long id;
    private long subjectId;
    private String subjectName;
    private String status; // open or close
    private String date;
    private double cost;
    private int numberOfStudent;
    private double deservedAmount;
    private double recivedAmount;
    private JFXCheckBox cbSelect;

    public Course() {
    }
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(int numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public double getDeservedAmount() {
        return deservedAmount;
    }

    public void setDeservedAmount(double deservedAmount) {
        this.deservedAmount = deservedAmount;
    }

    public double getRecivedAmount() {
        return recivedAmount;
    }

    public void setRecivedAmount(double recivedAmount) {
        this.recivedAmount = recivedAmount;
    }

    public JFXCheckBox getCbSelect() {
        return cbSelect;
    }

    public void setCbSelect() {
        cbSelect=new JFXCheckBox();
        cbSelect.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }
   
    
    public  boolean isSelected(){
        return cbSelect.isSelected();
    }
    
    
    public boolean insert(){
        if(canAddCourse()){
            CourseDao.insert(this);
            return true;
        }else{
            DialogBuilder.build("يجب اغلاق آخر دورة اولا ", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
    }
    public void update(){
        CourseDao.update(this);
    }
    
    private boolean canAddCourse(){
        return CourseDao.canAddCourse(this);
    }
    
    public boolean canRetrieveCourse(){
        if(!(this.status.equals("منتهية")))
            return false;
        if(!CourseDao.isLastCourse(this)){
            DialogBuilder.build("لا يمكن استرجاع الدورة", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        
        return true;
    } 
    
    public void retrieveCourse(){
        this.status="غير منتهية";
        update();
    }
    
    
    public boolean canCloseCourse(){
        return (this.status.equals("غير منتهية"))&&CourseDao.isLastCourse(this);
    }

    public void closeCourse() {
        this.status="منتهية";
        update();
    }

    public void delete() {
        CourseDao.delete(this);
       

    }
    
    public boolean canDelete(){
        return CourseDao.canDelete(this);
    }
    
    
    
}
