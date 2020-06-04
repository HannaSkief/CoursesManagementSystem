/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Subject;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Course.CourseController;
import Course.CourseDetailController;
import Dao.CourseDao;
import Model.Course;
import Model.Subject;
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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class SubjectCoursesController implements Initializable,IController {

    @FXML
    private TableView<Course> tvCourses;
    @FXML
    private TableColumn<Course, Long> idCol;
    @FXML
    private TableColumn<Course, String> statusCol;
    @FXML
    private TableColumn<Course, Double> costCol;
    @FXML
    private TableColumn<Course, String> dateCol;
    @FXML
    private TableColumn<Course, Integer> numberOfStudentCol;
    @FXML
    private TableColumn<Course, Double> deservedAmountCol;
    @FXML
    private TableColumn<Course, Double> recivedAmountCol;
    
    private Subject subject;
    @FXML
    private Label lSubjectname;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
    }   
    
    private void initTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numberOfStudentCol.setCellValueFactory(new PropertyValueFactory<>("numberOfStudent"));
        deservedAmountCol.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        recivedAmountCol.setCellValueFactory(new PropertyValueFactory<>("recivedAmount"));
        
        tvCourses.setRowFactory((TableView<Course> param) -> {
            TableRow<Course> row=new TableRow<>();
            row.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    showCourse();
                }
            });
            
            return row;
        });
        
       
    }
    
    
    public void setSubject(Subject subject){
        this.subject=subject;
        lSubjectname.setText(subject.getName());
         refresh();
    }
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void addNewCourse(ActionEvent event) {
        
        JFXTextField tfCost=new JFXTextField();
        tfCost.setPromptText("تكلفة الدورة");
        tfCost.setLabelFloat(true);
        tfCost.setPadding(new Insets(10));
        tfCost.setStyle("-fx-font-size:18 ;-fx-font-weight:bold");
        
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color:#2c2c54;-fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            if(tfCost.getText().trim().isEmpty()||!tfCost.getText().trim().matches("\\d+")){
                return; 
            }
            
            Course course=new Course();
            course.setSubjectId(this.subject.getId());
            course.setCost(Double.parseDouble(tfCost.getText().trim()));
            course.setStatus("غير منتهية");
            
            if(course.insert()){
                refresh();
            }
            
        });
        
        DialogBuilder.build("إضافة دورة جديدة", Arrays.asList(btn),tfCost,DialogBuilder.COLOR_DARK);
    }

    @FXML
    private void closeCourse(ActionEvent event) {
        Course course=tvCourses.getSelectionModel().getSelectedItem();
        if(course==null)
            return;
        
        JFXButton btn =new JFXButton("موافق");
        btn.setStyle("-fx-background-color:#2c2c54; -fx-text-fill:#ffffff");
        
        btn.setOnAction(e->{
            course.closeCourse();
            tvCourses.refresh();
        });
        
        if(course.canCloseCourse()){
            DialogBuilder.build("هل تريد إغلاق الدورة ؟", Arrays.asList(btn),DialogBuilder.COLOR_DARK);
        }
    }

    @FXML
    private void retriveCourse(ActionEvent event) {
        Course course=tvCourses.getSelectionModel().getSelectedItem();
        if(course==null)
            return;
        
        JFXButton btn =new JFXButton("موافق");
        btn.setStyle("-fx-background-color:#2c2c54; -fx-text-fill:#ffffff");
        
        btn.setOnAction(e->{
            course.retrieveCourse();
            tvCourses.refresh();
        });
        
        if(course.canRetrieveCourse()){
            DialogBuilder.build("هل تريد استرجاع الدورة ؟", Arrays.asList(btn),DialogBuilder.COLOR_DARK);
        }
    }

    @FXML
    private void showCourse() {
        Course course=tvCourses.getSelectionModel().getSelectedItem();
        if(course!=null){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/Course/CourseDetail.fxml"));
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
        tvCourses.getItems().addAll(CourseDao.getAllCourses(subject.getId()));
    }

    @FXML
    private void deleteCourse(ActionEvent event) {
        
        Course course=tvCourses.getSelectionModel().getSelectedItem();
        if(course==null)
            return;
        
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_RED+"; -fx-text-fill:#ffffff");
        btn.setOnMouseClicked(e->{
            course.delete();
            tvCourses.getItems().remove(course);
        });
        if(course.canDelete()){
            DialogBuilder.build("حذف الدورة "+course.getId()+" ؟ ", Arrays.asList(btn),DialogBuilder.COLOR_RED);
        }
        else{
            DialogBuilder.build("لا يمكن حذف الدورة !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
        }
        
    }
    
}
