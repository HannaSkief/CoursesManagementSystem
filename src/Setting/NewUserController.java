/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Setting;

import Additional.DialogBuilder;
import Additional.SceneAdapter;
import Model.User;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class NewUserController implements Initializable {

    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextField tfPassword;
    @FXML
    private JFXComboBox<String> cbRole;
    private boolean isNew;
    
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        isNew=true;
        initRoles();
        Platform.runLater(()->{tfName.requestFocus();});
    }


    private void initRoles() {
        cbRole.getItems().clear();
        cbRole.getItems().add("Admin");
        cbRole.getItems().add("Manager");
        cbRole.getSelectionModel().selectFirst();
        
    }    

    @FXML
    private void close() {
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void save(ActionEvent event) {
        if(checkValidation())
        {
            if(isNew)
                insert();
            else
                update();

            close();
        }
    }

    void setUpdateMode(User user) {
        
        isNew=false;
        this.user=user;
        
        tfName.setText(this.user.getName());
        tfPassword.setText(this.user.getPassword());
        cbRole.setValue(this.user.getRole());
        
        if(user.getId()==1){
            cbRole.setDisable(true);
        }
    }

    private boolean checkValidation() {
        
        if(tfName.getText().trim().isEmpty()){
            DialogBuilder.build("الرجاء ادخال الاسم !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.requestFocus();
            return false;
        }
        
        if(user==null && new User(0,tfName.getText().trim()).isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.selectAll();
            return false;
        }
        
        if(user!=null && new User(user.getId(),tfName.getText().trim()).isExist()){
            DialogBuilder.build("الاسم مستخدم بالفعل !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfName.selectAll();
            return false;
        }
        
        return true;
    }

    private void insert() {
        
        User user=new User();
        user.setName(tfName.getText().trim());
        user.setPassword(tfPassword.getText().trim());
        user.setRole(cbRole.getSelectionModel().getSelectedItem());
        
        user.insert();
        
    }

    private void update() {
        
        user.setName(tfName.getText().trim());
        user.setPassword(tfPassword.getText().trim());
        user.setRole(cbRole.getSelectionModel().getSelectedItem());
        
        user.update();
    }
    
}
