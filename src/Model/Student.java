/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.StudentDao;

/**
 *
 * @author ASUS
 */
public class Student {
    private long id;
    private String name;
    private String phone;
    private String info;
    private String createdAt;

    public Student() {
    }

    public Student(long id, String name) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void insert() {
        StudentDao.insert(this);
    }

    public void update() {
        StudentDao.update(this);
    }
    
    public boolean isExist(){
        return StudentDao.isExist(this);
    }

    public void delete() {
        StudentDao.delete(this);
    }

    public boolean canDelete() {
        return StudentDao.canDelete(this);
    }
    
    
}
