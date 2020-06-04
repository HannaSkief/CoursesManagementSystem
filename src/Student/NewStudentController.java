/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Additional.DialogBuilder;
import Additional.SceneAdapter;
import Model.Student;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class NewStudentController implements Initializable {

    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextField tfPhone;
    @FXML
    private JFXTextField tfInfo;

    private Student student;
    private boolean isNew;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        isNew=true;
        Platform.runLater(()->{tfName.requestFocus();});
    }    
    
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void save() {
        if(checkValidation())
        {
            if(isNew)
                insert();
            else
                update();

            close();
        }
    }
    
    private void insert(){
        student=new Student();
        student.setName(tfName.getText().trim());
        student.setInfo(tfInfo.getText().trim());
        student.setPhone(tfPhone.getText().trim());
        
        student.insert();
        
        
    }
    private void update(){
        student.setName(tfName.getText().trim());
        student.setInfo(tfInfo.getText().trim());
        student.setPhone(tfPhone.getText().trim());
        
        student.update();
        
        
    }
    
    private boolean checkValidation(){
        
        if(tfName.getText().trim().isEmpty()){
            DialogBuilder.build("الرجاء ادخال الاسم !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.requestFocus();
            return false;
        }
        
        if(student==null && new Student(0,tfName.getText().trim()).isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.selectAll();
            return false;
        }
        
        if(student!=null && new Student(student.getId(),tfName.getText().trim()).isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.selectAll();
            return false;
        }
        
        return true;
    }
    
    public void setUpdateMode(Student student){
        isNew=false;
        this.student=student;
        
        tfName.setText(student.getName());
        tfPhone.setText(student.getPhone());
        tfInfo.setText(student.getInfo());
        
    }
    
}
