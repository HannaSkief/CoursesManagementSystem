/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursemanagementsystem;

import Additional.DialogBuilder;
import Additional.Encryptor;
import Additional.FlexSol;
import Additional.SceneAdapter;
import Additional.WindowStyle;
import Dao.UserDao;
import Main.MainController;
import Model.User;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ASUS
 */
public class loginController implements Initializable,Runnable {

    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    
    
    private ArrayList<String> encrypMac=new ArrayList<>();
    @FXML
    private Label lFlexSol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        new Thread(this).start();
        
        Platform.runLater(()->{
            if(User.getCurrentUser()!=null){
                userName.setText(User.getCurrentUser().getName());
                password.requestFocus();
            }else{
                userName.requestFocus();
            }
        });
        
        // load new font 
        Font.loadFont(getClass().getResourceAsStream("/fonts/VerminVerile.ttf"), 30);
        lFlexSol.setStyle("-fx-font-family:'Vermin Verile'");
        
    }    

    @FXML
    private void login() {
        UserDao.login(userName.getText().trim(), password.getText().trim(),new  UserDao.LoginResult() {
            @Override
            public void done(User user) {
                User.setCurrentUser(user);
                SceneAdapter.openLockScreen();
            }

            @Override
            public void wrongUsername(String s) {
                DialogBuilder.build(s,Arrays.asList(),DialogBuilder.COLOR_RED);
                
            }

            @Override
            public void wrongPassword(String s) {
                DialogBuilder.build(s,Arrays.asList(),DialogBuilder.COLOR_RED);
                
            }
        });
    }
    
    
    private void checkLecince(){
        
        getMacAddress();
        
        String fileName="law.active";
        
        try {
            FileReader fr=new FileReader(fileName);
            BufferedReader br=new BufferedReader(fr);
            String content=new String();
            String line=null;
            while((line=br.readLine())!=null){
                content+=line;
            }
            if(!LecinceTrue(content)){
                Platform.runLater(()->{showLicenceKeyDialog();});
            }

        } catch (FileNotFoundException ex) {
            Platform.runLater(()->{showLicenceKeyDialog();});
            
        } catch (IOException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    private void showLicenceKeyDialog(){
        
        try {
            Parent root=FXMLLoader.load(getClass().getResource("Licence.fxml"));
            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(userName.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            WindowStyle.allowDrag(root, stage);
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    private boolean LecinceTrue(String s){
        
         if(encrypMac.contains(s))
            return true;
        return false;
    }
    
    private void getMacAddress(){
        String key = "hannadiabranihdr"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        encrypMac.clear();
        
        try {
            final Enumeration<NetworkInterface> e= NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                final byte [] mac = e.nextElement().getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    encrypMac.add(Encryptor.encrypt(key, initVector, sb.toString()));
                    
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void run() {
        checkLecince();
    }
    
    
    @FXML
    public void flexSol(){
        FlexSol.visitUs();
    }
    
}
