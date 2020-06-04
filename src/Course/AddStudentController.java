/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Course;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Dao.StudentDao;
import Model.Course;
import Model.CourseStudent;
import Model.Student;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AddStudentController implements Initializable,IController {

    @FXML
    private TableView<Student> tvStudents;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> phoneCol;

    Course course;
    Student student;
    @FXML
    private JFXTextField tfSearch;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeTableView();
        tfSearch.setOnKeyReleased(e->{refresh();});
    }    
    
    private void initializeTableView(){
        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        refresh();
    }

    void setStudentAddMode(Course course) {
        this.course=course;
    }

    @FXML
    private void close() {
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void save(ActionEvent event) {
        if(checkValidation())
        {
            insert();
            close();
        }
    }

    private boolean checkValidation() {
        student=tvStudents.getSelectionModel().getSelectedItem();
        if(student==null){
            DialogBuilder.build("الرجاء اختيار احد الطلاب !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        if(student!=null && new CourseStudent(course.getId(),student.getId()).isExist()){
            DialogBuilder.build("الطالب موجود في الدورة !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        return true;
    }

    private void insert() {
        CourseStudent courseStudent= new CourseStudent();
        courseStudent.setCourseId(course.getId());
        courseStudent.setStudentId(student.getId());
        courseStudent.setDeservedAmount(0);
        courseStudent.setPaidAmount(0);
        courseStudent.setStatus("نشط");
        courseStudent.setDeservedAmount(course.getCost());
        courseStudent.insert();
    }

    
    @FXML
    private void addNewStudent(ActionEvent event) {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("/Student/NewStudent.fxml"));
            SceneAdapter.addNewScene(root,this);
        } catch (IOException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refresh() {
        tvStudents.getItems().clear();
        tvStudents.getItems().addAll(StudentDao.getStudents(tfSearch.getText().trim()));
    }
    
}
