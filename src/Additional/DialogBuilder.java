/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Additional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author ASUS
 */
public class DialogBuilder {
    public static final String COLOR_RED="#b33939";
    public static final String COLOR_DARK="#2c2c54";
    public static final String COLOR_YELLWO="#ffb142";
    
    private static StackPane stackPane;
    private static Node nodeToBlur;
    private static JFXDialog dialog;
    
    public static void init(StackPane sp,Node n){
        stackPane=sp;
        nodeToBlur=n;
        
    }
    
    public static void build(String header,List<JFXButton> controls,String color){
        
        BoxBlur blur=new BoxBlur(3,3,3);
        JFXDialogLayout dialogLayout=new JFXDialogLayout();
        JFXDialog  dialog=new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.TOP);
        controls.forEach(controlBtn->{
            controlBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                dialog.close();
            });
        });
        
        Label label=new Label(header);
        label.setStyle("-fx-text-fill:#2c2c54;-fx-font-weight:bold");
        dialogLayout.setHeading(label);
        dialogLayout.setActions(controls);
        dialogLayout.setStyle("-fx-border-color:"+color+";-fx-border-width:0 0 0 20");
        dialogLayout.setOnKeyPressed(e->{
            if(e.getCode()==KeyCode.ENTER){
                dialog.close();
            }
        });
        dialog.setOnDialogOpened(e->{
            dialogLayout.requestFocus();
        });
        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            nodeToBlur.setEffect(null);
        });
        dialog.show();
        nodeToBlur.setEffect(blur);
    }
    public static void build(String header,List<JFXButton> controls,Node body,String color){
        
        BoxBlur blur=new BoxBlur(3,3,3);
        JFXDialogLayout dialogLayout=new JFXDialogLayout();
        JFXDialog dialog=new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.TOP);
        controls.forEach(controlBtn->{
        
            controlBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                dialog.close();
            });
        });
        Label label=new Label(header);
        label.setStyle("-fx-text-fill:#2c2c54;-fx-font-weight:bold");
        dialogLayout.setHeading(label);
        dialogLayout.setBody(body);
        dialogLayout.setStyle("-fx-border-color:"+color+";-fx-border-width:0 0 0 20");
        dialogLayout.setActions(controls);
        dialog.setOnDialogOpened(e->{
            body.requestFocus();
        });
        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            nodeToBlur.setEffect(null);
        });
        body.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if(controls.get(0)!=null){
                    controls.get(0).fire();
                    dialog.close();
                }
            }   
        });
        dialog.show();
        nodeToBlur.setEffect(blur);
    }
    
    public static void build(String header,List<JFXButton> controls,Node body,Node nodeToFocus,String color){
        
        BoxBlur blur=new BoxBlur(3,3,3);
        JFXDialogLayout dialogLayout=new JFXDialogLayout();
        JFXDialog dialog=new JFXDialog(stackPane,dialogLayout, JFXDialog.DialogTransition.TOP);
        controls.forEach(controlBtn->{
        
            controlBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                dialog.close();
            });
        });
        
        Label label=new Label(header);
        label.setStyle("-fx-text-fill:#2c2c54;-fx-font-weight:bold");
        dialogLayout.setHeading(label);
        dialogLayout.setBody(body);
        dialogLayout.setStyle("-fx-border-color:"+color+";-fx-border-width:0 0 0 20");
        dialogLayout.setActions(controls);
        dialog.setOnDialogOpened(e->{
            nodeToFocus.requestFocus();
        });
        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            nodeToBlur.setEffect(null);
        });
        
        dialog.show();
        nodeToBlur.setEffect(blur);
    }
    
    

}
