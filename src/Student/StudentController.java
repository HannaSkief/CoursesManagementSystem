/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Dao.StudentDao;
import Model.Student;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class StudentController implements Initializable,IController {

    @FXML
    private JFXTextField tfSearch;
    @FXML
    private TableView<Student> tvStudents;
    @FXML
    private TableColumn<Student, Long> idCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> phoneCol;
    @FXML
    private TableColumn<Student, String> infoCol;
    @FXML
    private TableColumn<Student, String> createdAtCol;

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
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        
        tvStudents.setRowFactory((TableView<Student> param) -> {
            TableRow<Student> row=new TableRow<>();
            
            row.setOnMouseClicked(e->{
                if(e.getClickCount()==2)
                    openStudentMovement(row.getItem());
            });
            return row;
        });
        
        refresh();
    }
    
    private void openStudentMovement(Student student){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("StudentMovement.fxml"));
            Parent root=loader.load();
            StudentMovementController controller=loader.getController();
            controller.setStudent(student);
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void addNewStudent(ActionEvent event) {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("NewStudent.fxml"));
            SceneAdapter.addNewScene(root, this);
        } catch (IOException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refresh() {

        tvStudents.getItems().clear();
        tvStudents.getItems().addAll(StudentDao.getStudents(tfSearch.getText().trim()));
    }

    @FXML
    private void updateStudent(ActionEvent event) {
        Student student=tvStudents.getSelectionModel().getSelectedItem();
        if(student!=null){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("NewStudent.fxml"));
            try {
                Parent root=loader.load();
                NewStudentController controller=loader.getController();
                controller.setUpdateMode(student);
                
                SceneAdapter.addNewScene(root, this);
            } catch (IOException ex) {
                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        Student student=tvStudents.getSelectionModel().getSelectedItem();
        if(student==null)
            return;
        
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_RED+"; -fx-text-fill:#ffffff ");
        btn.setOnAction(e->{
            student.delete();
            tvStudents.getItems().remove(student);
        });
        
        if(student.canDelete()){
            DialogBuilder.build("هل تريد حذف الطالب "+student.getName()+" ؟",Arrays.asList(btn), DialogBuilder.COLOR_RED);
        }else{
            DialogBuilder.build("لا يمكن حذف الطالب " +student.getName(), Arrays.asList(),DialogBuilder.COLOR_YELLWO);
        }
        
    }
    
}
