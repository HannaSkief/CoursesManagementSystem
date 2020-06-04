/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Additional.DialogBuilder;
import Dao.SubjectDao;
import Subject.SubjectController;
import java.util.Arrays;

/**
 *
 * @author ASUS
 */
public class Subject {
    
    private long id;
    private String name;
    private long semesterId;

    public Subject() {
    }

    public Subject(long id, String name, long semesterId) {
        this.id = id;
        this.name = name;
        this.semesterId = semesterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }
    
    public boolean isExist(){
        return SubjectDao.isExist(this);
    }
    public boolean insert(){
        
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }else{
            this.id=SubjectDao.insert(this);
            return true;
        }
        
    }
    
    public boolean update(){
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }else{
            SubjectDao.update(this);
            return true;
        }
        
        
    }

    public boolean canDelete() {
        return SubjectDao.canDelete(this);
    }

    public void delete(SubjectController aThis) {
        SubjectDao.delete(this);
    }
    
    
    
}
