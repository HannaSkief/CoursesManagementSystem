/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.UserDao;

/**
 *
 * @author ASUS
 */
public class User {
    
    private long id;
    private String name;
    private String password;
    private String role;
    private static User currentUser;
    private static User previousUser;

    public User() {
    }

    public User(long id, String name) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public static User getPreviousUser() {
        return previousUser;
    }

    public static void setPreviousUser(User previousUser) {
        User.previousUser = previousUser;
    }
    

    public void insert() {
        UserDao.insert(this);
    }

    public void update() {
        UserDao.update(this);
    }

    public boolean isExist() {
        return UserDao.checkIfExist(this);
    }

    public boolean canDelete() {
        return (this.id!=1 && UserDao.canDelete(this) );
    }

    public void delete() {
        UserDao.delete(this);
    }

    
}
