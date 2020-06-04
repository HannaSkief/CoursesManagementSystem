/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Additional.SceneAdapter;
import Dao.CourseDao;
import Dao.SubjectDao;
import Model.Course;
import Model.Subject;
import PDF.PdfCreater;
import PDF.ReportPdf;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class ReportController implements Initializable {

    
    @FXML
    private JFXRadioButton rbAllStatus;
    @FXML
    private JFXRadioButton rbfinished;
    @FXML
    private JFXRadioButton rbNotFinished;
    @FXML
    private JFXRadioButton rbAllSubject;
    @FXML
    private JFXRadioButton rbSubject;
    @FXML
    private JFXTextField tfSearch;
    @FXML
    private JFXListView<Subject> lvSubject;
    @FXML
    private TableView<Course> tvCourses;
    @FXML
    private TableColumn<Course, Long> idCol;
    @FXML
    private TableColumn<Course, String> subjectNameCol;
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
   
    @FXML
    private TableView<Course> tvSummary;
    @FXML
    private TableColumn<Course, Integer> sumNumberOfStudentCol;
    @FXML
    private TableColumn<Course, Double> sumDeservedAmountCol;
    @FXML
    private TableColumn<Course, Double> sumRrecivedAmountCol;
    
    
    private ToggleGroup statusTG;
    private ToggleGroup subjectTG;
    private ToggleGroup cashTG;
    @FXML
    private JFXRadioButton rbAllCash;
    @FXML
    private JFXRadioButton rbfinishedCash;
    @FXML
    private JFXRadioButton rbNotFinishedCash;
    @FXML
    private VBox vbSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initRaddioButtons();
        initTableView();
        initListView();
        vbSearch.setDisable(true);
    }    
    
    private void initTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
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
                    showDetails(row.getItem());
                }
            });
            return row;
        });
    
        sumNumberOfStudentCol.setCellValueFactory(new PropertyValueFactory<>("numberOfStudent"));
        sumDeservedAmountCol.setCellValueFactory(new PropertyValueFactory<>("deservedAmount"));
        sumRrecivedAmountCol.setCellValueFactory(new PropertyValueFactory<>("recivedAmount"));
    
        getReports();
    }
    private void showDetails(Course course){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("ReportDetails.fxml"));
            Parent root=loader.load();
            ReportDetailsController controller=loader.getController();
            controller.setCourse(course);
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @FXML
    public void getReports(){
        tvCourses.getItems().clear();
        tvCourses.getItems().addAll(CourseDao.getAllCoursesByCondition(getCondition()));
      
        tvSummary.getItems().clear();
        tvSummary.getItems().add(CourseDao.getSummaryByCondition(getCondition()));
    }
    
    private void initRaddioButtons(){
        //status
        statusTG=new ToggleGroup();
        rbAllStatus.setToggleGroup(statusTG);
        rbfinished.setToggleGroup(statusTG);
        rbNotFinished.setToggleGroup(statusTG);
        rbAllStatus.setSelected(true);
        
        //subjects
        subjectTG=new ToggleGroup();
        
        rbAllSubject.setToggleGroup(subjectTG);
        rbSubject.setToggleGroup(subjectTG);
        rbAllSubject.setSelected(true);
        
        //cash
        
        cashTG=new ToggleGroup();
        rbAllCash.setToggleGroup(cashTG);
        rbfinishedCash.setToggleGroup(cashTG);
        rbNotFinishedCash.setToggleGroup(cashTG);
        rbAllCash.setSelected(true);
        
        
    }
    
    private void initListView(){
                lvSubject.setDepth(3);
        lvSubject.setCellFactory((ListView<Subject> param) -> {
             JFXListCell<Subject> cell = new JFXListCell<Subject>(){
                @Override
                protected void updateItem(Subject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                    } else {
                        //setDisable(false);
                       setText(item.getName());
                    }
                    
                }
            };

             cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                 @Override
                 public void handle(MouseEvent event) {
                      if(event.getClickCount()==2){
                          
                          getReports();
                      }
                     
                 }
             });
             
             cell.setPadding(new Insets(10,0,10,10));
             
            return cell;
        });
        

        
        tfSearch.setOnKeyReleased(e->{
            lvSubject.getItems().clear();
            lvSubject.getItems().addAll(SubjectDao.getAllSubjectBuName(
                    tfSearch.getText().trim().isEmpty()?"":tfSearch.getText().trim()));
            
        });
        
    }
    
    
    private String getCondition(){
        String cond="";
        
        if(rbAllStatus.isSelected()){
            cond+=" 1 ";
        }else if(rbfinished.isSelected()){
            cond+="courses.status='منتهية'";
        }else{
            cond+="courses.status='غير منتهية'";
            
        }
        
        cond+=" and ";
        
        if(rbAllSubject.isSelected()){
            cond+=" 1";
        }
        else{
            cond+=lvSubject.getSelectionModel().getSelectedItem()==null?
                    " 1 ":"courses.subject_id="+lvSubject.getSelectionModel().getSelectedItem().getId()+" ";
        }
        
        cond+=" and ";
        
        if(rbAllCash.isSelected()){
            cond+=" 1 ";
        }
        else if(rbfinishedCash.isSelected()){
            cond+=" course_info.deserved_amount=course_info.recived_amount ";
        }else{
            cond+=" course_info.deserved_amount!=course_info.recived_amount "; 
        }
        
        return cond;
        
    }
    
    
    @FXML
    public void enableDisableSearch(){
        vbSearch.setDisable(!vbSearch.isDisable());
        getReports();
    }

    @FXML
    private void showCashReport(ActionEvent event) {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("CashReport.fxml"));
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void createPdf(ActionEvent event) {
        if(tvCourses.getItems().isEmpty())
            return ;
        
        PdfCreater.print(new ReportPdf(tvCourses.getItems()));
    }
}
