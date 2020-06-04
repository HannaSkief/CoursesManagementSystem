/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Course;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Dao.CourseStudentDao;
import Model.Course;
import Model.CourseStudent;
import Model.User;
import PDF.CoursePdf;
import PDF.PdfCreater;
import Student.StudentMovementController;
import Student.StudentMovementDetailsController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class CourseDetailController implements Initializable,IController {

    @FXML
    private JFXTextField tfSearch;
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
    
    @FXML
    private JFXButton btnAddStudent;
    @FXML
    private JFXButton btnDeleteStudent;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeCoursesDetailTable();
        tfSearch.setOnKeyReleased(e->{refresh();});
        Platform.runLater(()->{tfSearch.requestFocus();});
        
    }    
    
    public void initializeCoursesDetailTable(){
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        deservedAmountCol.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        paidAmountCol.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        remainAmountCol.setCellValueFactory(new PropertyValueFactory<>("remainAmount"));
        studentStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        addAtCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        //change student status on double click
        studentStatusCol.setCellFactory((TableColumn<CourseStudent, String> param) -> {
            TableCell<CourseStudent, String> cell=new TableCell<CourseStudent, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); 
                    if (item == null || empty) {
                        setText("");
                    } else {
                        
                       setText(item);
                    }
                }
                
            };
            
            cell.setOnMouseClicked(e->{
                if(e.getClickCount()==2)
                    changeStudentStatus();
            });
            return cell;
        });
        
        
        // show payments details on double click on student name 
        studentCol.setCellFactory((TableColumn<CourseStudent, String> param) -> {
            TableCell<CourseStudent, String> cell=new TableCell<CourseStudent, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); 
                    if (item == null || empty) {
                        setText("");
                    } else {
                        
                       setText(item);
                    }
                }
                
            };
            
            cell.setOnMouseClicked(e->{
                if(e.getClickCount()==2)
                    showPaymentsDetails((CourseStudent)cell.getTableRow().getItem());
            });
            return cell;
        });
        
        
        if(User.getCurrentUser().getRole().equalsIgnoreCase("Manager")){
            setDeservedAmountCellFactory();
        }
        
        
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
    private void changeStudentStatus(){
    
        CourseStudent courseStudent=tvCourseStudents.getSelectionModel().getSelectedItem();
        
        JFXListView<String> listView=new JFXListView<>();
        
        listView.getItems().addAll("نشط","غير نشط");
        listView.getSelectionModel().select(courseStudent.getStatus());
        
        listView.getStylesheets().add("/Css/SubjectListViewStyle.css");
        JFXButton btn=new JFXButton("موافق");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_DARK+";-fx-text-fill:#ffffff ");
        btn.setOnAction(e->{
            courseStudent.setStatus(listView.getSelectionModel().getSelectedItem());
            courseStudent.update();
            tvCourseStudents.refresh();
        });
        VBox vb=new VBox(5);
        vb.getChildren().addAll(new Separator(),listView);
        
        
        DialogBuilder.build("اختر حالة للطالب", Arrays.asList(btn),vb, DialogBuilder.COLOR_DARK);
    
    }

    public void setShowDetailMode(Course course) {
        this.course=course;
        if(course.getStatus().equals("منتهية"))
            setFinishMode();
            
        refresh();
    }
    
    private void setDeservedAmountCellFactory(){
        
        deservedAmountCol.setCellFactory((TableColumn<CourseStudent, Double> param) -> {
            TableCell<CourseStudent, Double> cell=new TableCell<CourseStudent, Double>(){
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    if(item==null||empty)
                        setText("");
                else{
                        setText(String.valueOf(item));
                    }
                }
            };
            
            cell.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    changeDeservedAmount();
                }
            });
            
            return cell;
        });
        
    }
    
    private void changeDeservedAmount(){
        CourseStudent courseStudent=tvCourseStudents.getSelectionModel().getSelectedItem();
        
        JFXListView<Double> listView=new JFXListView<>();
        
        listView.getItems().addAll(course.getCost(),courseStudent.getPaidAmount());
        listView.getSelectionModel().select(courseStudent.getDeservedAmount());
        
        listView.getStylesheets().add("/Css/SubjectListViewStyle.css");
        JFXButton btn=new JFXButton("موافق");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_DARK+";-fx-text-fill:#ffffff ");
        btn.setOnAction(e->{
            courseStudent.setDeservedAmount(listView.getSelectionModel().getSelectedItem());
            courseStudent.setRemainAmount(courseStudent.getDeservedAmount()-courseStudent.getPaidAmount());
            courseStudent.update();
            tvCourseStudents.refresh();
        });
        VBox vb=new VBox(5);
        vb.getChildren().addAll(new Separator(),listView);
        
        
        DialogBuilder.build("المبلغ المستحق", Arrays.asList(btn),vb, DialogBuilder.COLOR_DARK);
        
        
    }

    @FXML
    private void resicveCash(ActionEvent event) {
        CourseStudent courseStudent=tvCourseStudents.getSelectionModel().getSelectedItem();
        if(courseStudent==null){
            return;
        }
//        if(courseStudent.getRemainAmount()==0){
//            DialogBuilder.build("تم قبض كامل المستحقات المالية من "+courseStudent.getStudentName(), Arrays.asList(), DialogBuilder.COLOR_YELLWO);
//            return;
//        }
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Recieve.fxml"));
        try {
            Parent root=loader.load();
            RecieveController controller=loader.getController();
            controller.setPayMode(courseStudent);

            SceneAdapter.addNewScene(root, this);
        } catch (IOException ex) {
            Logger.getLogger(CourseDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addStudent(ActionEvent event) {
        
            FXMLLoader loader=new FXMLLoader(getClass().getResource("AddStudent.fxml"));
            try {
                Parent root=loader.load();
                AddStudentController controller=loader.getController();
                controller.setStudentAddMode(course);
                
                SceneAdapter.addNewScene(root, this);
            } catch (IOException ex) {
                Logger.getLogger(CourseDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }   
    }
    

    @FXML
    private void deleteStudent(ActionEvent event) {
        CourseStudent courseStudent=tvCourseStudents.getSelectionModel().getSelectedItem();
        if(courseStudent==null)
            return;
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_RED+" ; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            courseStudent.delete();
            tvCourseStudents.getItems().remove(courseStudent);
        });
        
        if(courseStudent.canDelete()){
            DialogBuilder.build("حذف الطالب "+courseStudent.getStudentName()+" ؟", Arrays.asList(btn), DialogBuilder.COLOR_RED);
        }else{
            DialogBuilder.build("لا يمكن حذف الطالب ", Arrays.asList(), DialogBuilder.COLOR_YELLWO);
        }
        
        
    }

    @Override
    public void refresh() {
        tvCourseStudents.getItems().clear();
        tvCourseStudents.getItems().addAll(CourseStudentDao.getCourseStudents(tfSearch.getText().trim(),course.getId()));
    }
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }

    private void setFinishMode() {
        btnAddStudent.setVisible(false);
        btnAddStudent.setMaxSize(0, 0);
        btnAddStudent.setMinSize(0, 0);
        btnDeleteStudent.setVisible(false);
        btnDeleteStudent.setMaxSize(0, 0);
        btnDeleteStudent.setMinSize(0, 0);
    }

   public void createPdf(){
       if(tvCourseStudents.getItems().isEmpty())
           return;
       
       PdfCreater.print(new CoursePdf(tvCourseStudents.getItems()));
   }

    
}
