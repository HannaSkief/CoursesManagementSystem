/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Setting;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Dao.UserDao;
import Model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class UsersController implements Initializable,IController {

    @FXML
    private JFXTextField tfSearch;
    @FXML
    private TableView<User> tvUsers;
    @FXML
    private TableColumn<User, Long> idCol;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> roleCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initializeTableView();
        tfSearch.setOnKeyReleased(e->{refresh();});
    }    
    
    private void initializeTableView(){
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        refresh();
    }
    
    
    @FXML
    private void addNewUser(ActionEvent event) {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("NewUser.fxml"));
            SceneAdapter.addNewScene(root, this);
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateUser(ActionEvent event) {
        User user=tvUsers.getSelectionModel().getSelectedItem();
        if(user!=null){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("NewUser.fxml"));
            try {
                Parent root=loader.load();
                NewUserController controller=loader.getController();
                controller.setUpdateMode(user);
                
                SceneAdapter.addNewScene(root, this);
            } catch (IOException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        User user=tvUsers.getSelectionModel().getSelectedItem();
        if(user==null)
            return;
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_RED+" ; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
           user.delete();
           tvUsers.getItems().remove(user);
        });
        if(user.canDelete()){
            DialogBuilder.build("حذف المستخدم "+user.getName(), Arrays.asList(btn), DialogBuilder.COLOR_RED);
        }else{
            DialogBuilder.build("لا يمكن حذف المستخدم", Arrays.asList(), DialogBuilder.COLOR_YELLWO);
        }
        
        
    }

    @FXML
    private void archiveUser(ActionEvent event) {
        if(tvUsers.getSelectionModel().getSelectedItem()==null){
            return;
        }
        JFXButton button=new JFXButton("تأكيد");
        button.setStyle("-fx-background-color:#2c2c54 ; -fx-text-fill:white");
        button.setOnMouseClicked(e->{
            
            if(User.getCurrentUser().getId()==tvUsers.getSelectionModel().getSelectedItem().getId())
                {
                    DialogBuilder.build("لا يمكنك القيام بأرشفة الحساب الذي تستخدمه الآن.", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
                    return;
                }
            
            if(tvUsers.getSelectionModel().getSelectedItem().getId()==1)
                {
                    DialogBuilder.build("لا يمكن أرشفة المستخدم الرئيسي !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
                    return;
                }
            
            UserDao.archiveUser(tvUsers.getSelectionModel().getSelectedItem());
            refresh();
            
        });
        
        DialogBuilder.build("تأكيد عملية أرشفة المستخدم(عند أرشفة المستخدم سوف يبقى في البرنامج لكن لا تستطيع تسجيل الدخول باسمه)."
                , Arrays.asList(button),DialogBuilder.COLOR_YELLWO);
    }

    @Override
    public void refresh() {
        tvUsers.getItems().clear();
        tvUsers.getItems().addAll(UserDao.getUser(tfSearch.getText().trim()));
    }
    
}
