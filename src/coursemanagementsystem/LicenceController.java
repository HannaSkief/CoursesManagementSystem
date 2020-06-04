/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursemanagementsystem;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Additional.Encryptor;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class LicenceController implements Initializable {

    @FXML
    private JFXListView<String> macList;
    @FXML
    private JFXTextField licenceTextField;
    private ArrayList<String> encrypMac=new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getMacAddress();
    }    
    private void getMacAddress(){
     
       
        try {
            final Enumeration<NetworkInterface> e= NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                final byte [] mac = e.nextElement().getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    macList.getItems().add(sb.toString());
                    
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(LicenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
           
        macList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                   StringSelection stringSelection = new StringSelection(macList.getSelectionModel().getSelectedItem());
                    java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                   clipboard.setContents(stringSelection, null);
               // }
            }
        });
        
    }

    @FXML
    private void enter(ActionEvent event) {
        
        String fileName="law.active";
        
        if(licenceTextField.getText().isEmpty())
        return;
        if(!check(licenceTextField.getText().trim()))
            System.out.println("false");
        else
        {
            try {
                FileWriter fw=new FileWriter(fileName);
                fw.write(licenceTextField.getText());
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(LicenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Stage stage=(Stage)macList.getScene().getWindow();
            stage.close();
        }
        
    }

    
    private boolean check(String s){
        String key = "hannadiabranihdr"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        encrypMac.clear();
        
        for(String mac:macList.getItems()){
            encrypMac.add(Encryptor.encrypt(key, initVector, mac));
        }
        
        if(encrypMac.contains(s))
            return true;
        
        return false;
    }
    
    @FXML
    private void close(ActionEvent event) {
        Platform.exit();
    }
}
