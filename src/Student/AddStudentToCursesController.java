/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Additional.SceneAdapter;
import Dao.CourseDao;
import Model.Course;
import Model.CourseStudent;
import Model.Student;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AddStudentToCursesController implements Initializable {

    @FXML
    private TableView<Course> tvCourses;
    @FXML
    private TableColumn<Course, Long> idCol;
    @FXML
    private TableColumn<Course, String> subjectCol;
    @FXML
    private TableColumn<Course, Double> costCol;
    @FXML
    private TableColumn<Course, String> dateCol;
    @FXML
    private TableColumn<Course, JFXCheckBox> selectCol;

    private Student student;
    @FXML
    private JFXTextField tfsearch;
    ObservableList<Course> courseList;
    @FXML
    private Label numberOfCourses;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTableView();
        numberOfCourses.setVisible(false);
    }    
    
    private void initTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        costCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        selectCol.setCellValueFactory(new PropertyValueFactory<>("cbSelect"));
        
        selectCol.setCellFactory((TableColumn<Course, JFXCheckBox> param) -> {
            TableCell<Course, JFXCheckBox> cell =new TableCell<Course, JFXCheckBox>(){
                @Override
                protected void updateItem(JFXCheckBox item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    if(item!=null && !empty){
                        setGraphic(item);
                        item.setOnMouseClicked(e->{
                            if(item.isSelected())
                                addCourse();
                            else
                                removeCourse();
                        });
                    }
                    else
                        setGraphic(null);
                }
                
            };
               
            
            return cell;
        });
    }

    public void setStudent(Student student){
        this.student=student;
        courseList=FXCollections.observableArrayList(CourseDao.getOpenCourses( student.getId()));
        getCourses();
    }
    
    @FXML
    public void getCourses(){
        tvCourses.getItems().clear();
        tvCourses.getItems().addAll(courseList.filtered(new Predicate<Course>() {
            @Override
            public boolean test(Course t) {
                  return t.getSubjectName().contains(tfsearch.getText());
            }
        }));
    }
    
    @FXML
    private void save(ActionEvent event) {
        
        for(Course course:courseList){
            if(course.isSelected()){
                CourseStudent courseStudent= new CourseStudent();
                courseStudent.setCourseId(course.getId());
                courseStudent.setStudentId(this.student.getId());
                courseStudent.setDeservedAmount(0);
                courseStudent.setPaidAmount(0);
                courseStudent.setStatus("نشط");
                courseStudent.setDeservedAmount(course.getCost());
                if(!courseStudent.isExist())
                    courseStudent.insert();
            }
        }
        close();
    }
    
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }
    
    private void addCourse(){
        int num=Integer.parseInt(numberOfCourses.getText())+1;
        if(num==1){
            numberOfCourses.setVisible(true);
        }
        numberOfCourses.setText(String.valueOf(num));
    }
    private void removeCourse(){
        int num=Integer.parseInt(numberOfCourses.getText())-1;
        if(num==0){
            numberOfCourses.setVisible(false);
        }
        numberOfCourses.setText(String.valueOf(num));
    }
    
}
