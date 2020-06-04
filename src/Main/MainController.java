/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Additional.DialogBuilder;
import Additional.FlexSol;
import Additional.IController;
import Additional.SceneAdapter;
import Model.User;
import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class MainController implements Initializable {

    @FXML
    private StackPane containerStackPane;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private BorderPane borderBane;
    @FXML
    private Label lUsername;
    @FXML
    private Label lRole;
    @FXML
    private JFXButton btnStudents;
    @FXML
    private JFXButton btnCourses;
    @FXML
    private JFXButton btnSubjects;
    @FXML
    private JFXButton btnReports;
    @FXML
    private JFXButton btnUsers;
    
    private String currentScene;
    
    double disableOpacity=0.8;
    @FXML
    private Label lFlexSol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentScene="";
        
        SceneAdapter.setContainers(containerStackPane,mainStackPane);
        DialogBuilder.init(mainStackPane, borderBane);
        lFlexSol.setStyle("-fx-font-family:'Vermin Verile'");
        
        lockScreen();

    }   
    
    public void flexSol(){
        FlexSol.visitUs();
    }
    
    
    
    @FXML
    public void lockScreen(){
       User.setPreviousUser(User.getCurrentUser());
       SceneAdapter.lockScreen(new IController() {
           @Override
           public void refresh() {
                openLockScreenCallBack();
           }
       });
        
    }
    private void openLockScreenCallBack(){
        setPrivilages(User.getCurrentUser());
        lUsername.setText(User.getCurrentUser().getName());
        lRole.setText(User.getCurrentUser().getRole());
        if(User.getPreviousUser()==null || User.getCurrentUser().getId()!=User.getPreviousUser().getId()){
                openCourses();
        }
        
    }

    @FXML
    private void openCourses() {
        clearBtnStyle();
        changeBtnStyle(btnCourses);
        openScene("/Course/Course.fxml");
        
    }

    @FXML
    private void openStudent(ActionEvent event) {
        clearBtnStyle();
        changeBtnStyle(btnStudents);
        openScene("/Student/Student.fxml");
        
    }
    @FXML
    public void openSubjects(){
        clearBtnStyle();
        changeBtnStyle(btnSubjects);
        openScene("/Subject/Subject.fxml");
    }
    
    
    private void openScene(String path){
        if(currentScene.equals(path))
            return;
        
         try {
            Parent root=FXMLLoader.load(getClass().getResource(path));
            SceneAdapter.openScene(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         currentScene=path;
    }

    @FXML
    private void openSetting(ActionEvent event) {
        clearBtnStyle();
        changeBtnStyle(btnUsers);
        openScene("/Setting/Users.fxml");
    }

    @FXML
    private void openReport(ActionEvent event) {
        clearBtnStyle();
        changeBtnStyle(btnReports);
        openScene("/Report/Report.fxml");
    }
    
    public void changeBtnStyle(JFXButton btn){
        btn.getStyleClass().add("selected-button");
        btn.setOpacity(1);
        
    }
    public void clearBtnStyle(){
        btnCourses.getStyleClass().remove("selected-button");
        btnCourses.setOpacity(0.8);
        btnStudents.getStyleClass().remove("selected-button");
        btnStudents.setOpacity(0.8);
        btnSubjects.getStyleClass().remove("selected-button");
        btnSubjects.setOpacity(0.8);
        btnReports.getStyleClass().remove("selected-button");
        btnReports.setOpacity(disableOpacity);
        btnUsers.getStyleClass().remove("selected-button");
        btnUsers.setOpacity(disableOpacity);
        
    }
    
    private void setPrivilages(User user){
        if(user.getRole().equalsIgnoreCase("admin")){
            btnReports.setDisable(true);
            btnUsers.setDisable(true);
            disableOpacity=0.3;
        }else{
            btnReports.setDisable(false);
            btnUsers.setDisable(false);
            disableOpacity=0.8;
        }
    }
    
    
}
