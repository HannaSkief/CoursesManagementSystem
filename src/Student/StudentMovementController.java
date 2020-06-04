/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Additional.DialogBuilder;
import Additional.IController;
import Additional.SceneAdapter;
import Dao.CoursePaymentDao;
import Dao.CourseStudentDao;
import Dao.StudentMovementDao;
import Model.CoursePayment;
import Model.CourseStudent;
import Model.Student;
import Model.StudentMovement;
import Model.User;
import PDF.PdfCreater;
import PDF.StudentPdf;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class StudentMovementController implements Initializable,IController {

    @FXML
    private Label lStudentName;
    private Student student;
    @FXML
    private TableView<StudentMovement> tvStudentMovement;
    @FXML
    private TableColumn<StudentMovement, Long> courseIdCol;
    @FXML
    private TableColumn<StudentMovement, String> subjectNameCol;
    @FXML
    private TableColumn<StudentMovement, String> courseDateCol;
    @FXML
    private TableColumn<StudentMovement, String> courseStatusCol;
    @FXML
    private TableColumn<StudentMovement, Double> deservedAmountCol;
    @FXML
    private TableColumn<StudentMovement, Double> paidAmountCol;
    @FXML
    private TableColumn<StudentMovement, Double> remainAmountCol;
    @FXML
    private TableColumn<StudentMovement, String> studentStatusCol;
    @FXML
    private TableColumn<StudentMovement, String> addedAtCol;
    @FXML
    private TableView<StudentMovement> tvSummary;
    @FXML
    private TableColumn<StudentMovement, Double> deservedAmountSum;
    @FXML
    private TableColumn<StudentMovement, Double> paidAmountSum;
    @FXML
    private TableColumn<StudentMovement, String> sumLabelCol;
    @FXML
    private TableColumn<StudentMovement, Double> remainAmountSum;
    @FXML
    private JFXButton btnDeleteStudent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
    } 
    
    private void initTableView(){
        courseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        courseDateCol.setCellValueFactory(new PropertyValueFactory<>("courseDate"));
        courseStatusCol.setCellValueFactory(new PropertyValueFactory<>("courseStatus"));
        deservedAmountCol.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        paidAmountCol.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        studentStatusCol.setCellValueFactory(new PropertyValueFactory<>("studentStatus"));
        addedAtCol.setCellValueFactory(new PropertyValueFactory<>("addedAt"));
        remainAmountCol.setCellValueFactory(new PropertyValueFactory<>("remainAmount"));
        
        tvStudentMovement.setRowFactory((TableView<StudentMovement> param) -> {
            TableRow<StudentMovement> row=new TableRow<StudentMovement>();
            row.setOnMouseClicked(e->{
                if(e.getClickCount()==2){
                    if(row.getItem()!=null)
                        showDetails(row.getItem());
                }
            });
            return row;
        });
        
        /// summary tableView
        sumLabelCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        deservedAmountSum.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        paidAmountSum.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        remainAmountSum.setCellValueFactory(new PropertyValueFactory<>("remainAmount"));
    }
    private void showDetails(StudentMovement sm){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("StudentMovementDetails.fxml"));
            Parent root=loader.load();
            StudentMovementDetailsController controller=loader.getController();
            controller.setCourseStudent(sm.getCourseId(), student.getId());
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(StudentMovementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }
    
    public void setStudent(Student student){
        this.student=student;
        lStudentName.setText(student.getName());
        refresh();
        
    }

    @FXML
    private void addCourses(ActionEvent event) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("AddStudentToCurses.fxml"));
            Parent root=loader.load();
            AddStudentToCursesController controller=loader.getController();
            controller.setStudent(this.student);
            SceneAdapter.addNewScene(root, this);
        } catch (IOException ex) {
            Logger.getLogger(StudentMovementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void refresh() {
        tvStudentMovement.getItems().clear();
        tvSummary.getItems().clear();
        
        tvStudentMovement.getItems().addAll(StudentMovementDao.getAll(student.getId()));
        tvSummary.getItems().add(StudentMovementDao.getSummary(student.getId()));
    }

    @FXML
    private void reciveCash(ActionEvent event) {
        
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #2c2c54;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(!text.getText().trim().matches("\\d+")){
                reciveCash(event);
                return;
            }
            double amount=Double.parseDouble(text.getText().trim());
            
            if(amount>(tvSummary.getItems().get(0).getDeservedAmount()-tvSummary.getItems().get(0).getPaidAmount())){
                reciveCash(event);
                return;
            }
            
            for(StudentMovement sm:tvStudentMovement.getItems()){
                
                if(amount<=0)
                    break;
                if((sm.getDeservedAmount()-sm.getPaidAmount())==0)
                    continue;
                
                if(amount>(sm.getDeservedAmount()-sm.getPaidAmount())){
                    
                    recive((sm.getDeservedAmount()-sm.getPaidAmount()), sm.getCourseId());
                    amount=amount-(sm.getDeservedAmount()-sm.getPaidAmount());
                    
                }else{
                    recive(amount, sm.getCourseId());
                    break;
                }
                
            }
            refresh();
        });
            
            
        DialogBuilder.build("قبض المبلغ :", Arrays.asList(btn),text, DialogBuilder.COLOR_DARK);
    }
    
    private void recive(double amount,long courseId){
        CoursePayment coursePayment=new CoursePayment();
        coursePayment.setCourseId(courseId);
        coursePayment.setStudentId(student.getId());
        coursePayment.setAmount(amount);
        coursePayment.setNote("");
        coursePayment.setUserId(User.getCurrentUser().getId());
        coursePayment.insert();
        
        CourseStudentDao.addPayment(courseId, this.student.getId(),amount);
    }

    

    @FXML
    private void createPdf() {
        if(tvStudentMovement.getItems().isEmpty())
            return;
        
        PdfCreater.print(new StudentPdf(tvStudentMovement.getItems(), student));
    }

    @FXML
    private void delete(ActionEvent event) {
        StudentMovement sm=tvStudentMovement.getSelectionModel().getSelectedItem();
        if(sm==null)
            return;
        
        CourseStudent cs=new CourseStudent(sm.getCourseId(), student.getId());
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_RED+" ; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            cs.delete();
            refresh();
        });
        
        if(cs.canDelete()){
            DialogBuilder.build("حذف الدورة : "+sm.getSubjectName()+" ؟", Arrays.asList(btn), DialogBuilder.COLOR_RED);
        }else{
            DialogBuilder.build("لا يمكن حذف الدورة", Arrays.asList(), DialogBuilder.COLOR_YELLWO);
            
        }
    }
    
}
