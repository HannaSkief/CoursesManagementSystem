/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Additional.DialogBuilder;
import Dao.YearDao;
import java.util.Arrays;

/**
 *
 * @author ASUS
 */
public class Year {
    private long id;
    private String name;

    public Year() {
    }
    

    public Year(long id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isExist() {
        return YearDao.isExist(this);
    }

    public boolean insert() {
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO);  
            return false;
        }else{
            this.id=YearDao.insert(this);
            return true;
        }
    }
    
    public boolean update(){
        if(isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل", Arrays.asList(),DialogBuilder.COLOR_YELLWO);  
            return false;
        }else{
            YearDao.update(this);
            return true;
        }
            
    }
    

    public boolean canDelete() {
         return YearDao.canDelete(this);
    }

    public void delete() {
        YearDao.delete(this);
    }
    
    
    
}
