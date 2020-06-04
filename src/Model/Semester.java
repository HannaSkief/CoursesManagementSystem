/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Additional.DialogBuilder;
import Dao.SemesterDao;
import java.util.Arrays;

/**
 *
 * @author ASUS
 */
public class Semester {
    
    private long id;
    private String name;
    private long yearId;

    public Semester() {
    }

    public Semester(long id, String name, long yearId) {
        this.id = id;
        this.name = name;
        this.yearId = yearId;
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

    public long getYearId() {
        return yearId;
    }

    public void setYearId(long yearId) {
        this.yearId = yearId;
    }
    
    public boolean isExist(){
        return SemesterDao.isExist(this);
    }
    public boolean insert(){
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO); 
            return false;
        }else{
            this.id=SemesterDao.insert(this);
            return true;
        }
    }
    public boolean update(){
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        else{
            SemesterDao.update(this);
            return true;
        }
        
    }

    public boolean canDelete() {
        return SemesterDao.canDelete(this);
    }

    public void delete() {
        SemesterDao.delete(this);
    }
    
    
    
    
}
