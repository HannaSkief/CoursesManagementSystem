/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Course;

import Additional.IController;
import Additional.SceneAdapter;
import Dao.CourseDao;
import Model.Course;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class CourseController implements Initializable,IController {

    @FXML
    private JFXTextField tfSearch;
    @FXML
    private TableView<Course> tvCourses;
    @FXML
    private TableColumn<Course, Long> idCol;
    @FXML
    private TableColumn<Course, String> subjectCol;
    @FXML
    private TableColumn<Course, String> statusCol;
    @FXML
    private TableColumn<Course, Double> costCol;
    @FXML
    private TableColumn<Course, String> dateCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        initializeCoursesTable();
        tfSearch.setOnKeyReleased(e->{refresh();});
        Platform.runLater(()->{tfSearch.requestFocus();});
    }    
    
    
    public void initializeCoursesTable(){
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tvCourses.setRowFactory((TableView<Course> param) -> {
            TableRow<Course> row=new TableRow<>();
            row.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    showCourse();
                }
            });
            
            return row;
        });
        
        refresh();
    }

    @FXML
    private void showCourse() {
        
        Course course=tvCourses.getSelectionModel().getSelectedItem();
        if(course!=null){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("CourseDetail.fxml"));
            try {
                Parent root=loader.load();
                CourseDetailController controller=loader.getController();
                controller.setShowDetailMode(course);
                
                SceneAdapter.addNewScene(root, this);
            } catch (IOException ex) {
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    @Override
    public void refresh() {
        tvCourses.getItems().clear();
        tvCourses.getItems().addAll(CourseDao.getOpenCourses(tfSearch.getText().trim()));
    }
    
}
