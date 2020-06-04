/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Additional.SceneAdapter;
import Dao.CourseStudentDao;
import Model.Course;
import Model.CourseStudent;
import Student.StudentMovementController;
import Student.StudentMovementDetailsController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
public class ReportDetailsController implements Initializable {

     @FXML
    private TableView<CourseStudent> tvCourseStudents;
    @FXML
    private TableColumn<CourseStudent, Long> idCol;
    @FXML
    private TableColumn<CourseStudent, String> subjectCol;
    @FXML
    private TableColumn<CourseStudent, String> studentCol;
    @FXML
    private TableColumn<CourseStudent, Double> deservedAmountCol;
    @FXML
    private TableColumn<CourseStudent, Double> paidAmountCol;
    @FXML
    private TableColumn<CourseStudent, Double> remainAmountCol;
    @FXML
    private TableColumn<CourseStudent, String> studentStatusCol;
    @FXML
    private TableColumn<CourseStudent, String> addAtCol;

    private Course course;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTableView();
    }    
    
    private void initTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        deservedAmountCol.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        paidAmountCol.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        remainAmountCol.setCellValueFactory(new PropertyValueFactory<>("remainAmount"));
        studentStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        addAtCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tvCourseStudents.setRowFactory((TableView<CourseStudent> param) -> {
            TableRow<CourseStudent> row=new TableRow<>();
            row.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    showPaymentsDetails(row.getItem());
                }
            });
            return row;
        });
    }
    
     private void showPaymentsDetails(CourseStudent cs){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/Student/StudentMovementDetails.fxml"));
            Parent root=loader.load();
            StudentMovementDetailsController controller=loader.getController();
            controller.setCourseStudent(cs.getCourseId(), cs.getStudentId());
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(StudentMovementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setCourse(Course course){
        this.course=course;
        
        //get all courseStudent
        tvCourseStudents.getItems().addAll(CourseStudentDao.getCourseStudents("",course.getId()));

    }
    
    public void close(){
        SceneAdapter.closeCurrentScene();
    }
    
}
