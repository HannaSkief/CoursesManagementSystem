/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Additional;

import Model.User;
import com.jfoenix.controls.JFXProgressBar;
import coursemanagementsystem.loginController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author ASUS
 */
public class SceneAdapter {
    
    private static StackPane container;
    private static StackPane mainStacPane;
    
    private static ObservableList<Parent> rootList;
    private static List<IController> controllerList;
    private static IController mainIController;
    
    
    public static void setContainers(StackPane stackPaneContainer,StackPane stackMain){
        container=stackPaneContainer;
        mainStacPane=stackMain;
        rootList=FXCollections.observableArrayList();
        controllerList=new ArrayList<>();
        
    }
    
    // open scene from side bar 
    public static void openScene(Parent root){
        
        root.translateXProperty().set(container.getWidth());
        container.getChildren().add(root);
        Timeline timeLine=new Timeline();
        KeyValue kv=new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
        KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
        timeLine.getKeyFrames().add(kf);
        timeLine.setOnFinished(event2->{
            if(!rootList.isEmpty()){
                container.getChildren().removeAll(rootList);
                rootList.clear();
                controllerList.clear();
            }
            rootList.add(root);
        });
        timeLine.play();
    }
    
    // close all scenes 
    public static void OpenMainScene(){
        
        if(!rootList.isEmpty()){
            Timeline timeLine=new Timeline();
            KeyValue kv=new KeyValue(rootList.get(rootList.size()-1).translateXProperty(),container.getWidth(),Interpolator.EASE_IN);
            KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
            timeLine.getKeyFrames().add(kf);
            timeLine.setOnFinished(event2->{
                if(!rootList.isEmpty()){
                    container.getChildren().removeAll(rootList);
                    rootList.clear();
                    controllerList.clear();
                }
            });
            timeLine.play();
            
        }
    }
    
    // open scene above other scene  and add parent controller to controllerList
    public static void addNewScene(Parent root,IController controller){
        BoxBlur blur=new BoxBlur(3, 3, 3);
        if(!rootList.isEmpty())
            rootList.get(rootList.size()-1).setEffect(blur);
        
        rootList.add(root);
        controllerList.add(controller);
        
        root.translateXProperty().set(container.getWidth());
        container.getChildren().add(root);
        Timeline timeLine=new Timeline();
        KeyValue kv=new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
        KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
        timeLine.getKeyFrames().add(kf);
        timeLine.play();
    }
    
    //close last scene in rootList and fire last refresh function in controllerList 
    public static void closeCurrentScene(){
        
        if(!rootList.isEmpty()){
            Timeline timeLine=new Timeline();
            KeyValue kv=new KeyValue(rootList.get(rootList.size()-1).translateXProperty(),container.getWidth(),Interpolator.EASE_IN);
            KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
            timeLine.getKeyFrames().add(kf);
            timeLine.setOnFinished(event2->{
                if(!rootList.isEmpty()){
                    container.getChildren().remove(rootList.get(rootList.size()-1));
                    rootList.remove(rootList.size()-1);
                    if(!rootList.isEmpty())
                        rootList.get(rootList.size()-1).setEffect(null);
                    if(!controllerList.isEmpty()){
                        if(controllerList.get(controllerList.size()-1)!=null)
                            controllerList.get(controllerList.size()-1).refresh();
                     
                        controllerList.remove(controllerList.size()-1);
                    }
                }
            });
            timeLine.play();
            
        }
        
    }
    
    
    public static void lockScreen(IController iController){
        try {
            mainIController=iController;
            Parent root =FXMLLoader.load(loginController.class.getResource("/coursemanagementsystem/login.fxml"));
            root.translateXProperty().set(container.getWidth());
            mainStacPane.getChildren().add(root);
            Timeline timeLine=new Timeline();
            KeyValue kv=new KeyValue(root.translateXProperty(),0,Interpolator.EASE_IN);
            KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
            timeLine.getKeyFrames().add(kf);
            timeLine.play();
        } catch (IOException ex) {
            Logger.getLogger(SceneAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void openLockScreen(){
           
            mainIController.refresh();
            Timeline timeLine=new Timeline();
            KeyValue kv=new KeyValue(mainStacPane.getChildren().get(mainStacPane.getChildren().size()-1).translateXProperty(),mainStacPane.getWidth(),Interpolator.EASE_IN);
            KeyFrame kf=new KeyFrame(Duration.seconds(0.3), kv);
            timeLine.getKeyFrames().add(kf);
            timeLine.setOnFinished(event2->{
                mainStacPane.getChildren().remove(mainStacPane.getChildren().size()-1);
            });
            timeLine.play();
    }
    
    
    public static StackPane getMainStackPane(){
        return mainStacPane;
    }
    
    
    
    
}
