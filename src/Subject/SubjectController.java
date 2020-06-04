/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Subject;

import Additional.DialogBuilder;
import Additional.SceneAdapter;
import Dao.SemesterDao;
import Dao.SubjectDao;
import Dao.YearDao;
import Model.Semester;
import Model.Subject;
import Model.Year;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class SubjectController implements Initializable {

    @FXML
    private JFXListView<Year> lvYear;
    @FXML
    private JFXListView<Semester> lvSemester;
    @FXML
    private JFXListView<Subject> lvSubject;
    @FXML
    private VBox vbSearch;
    @FXML
    private JFXTextField tfSerachSubject;
    @FXML
    private JFXListView<Subject> lvSearchSubject;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initYearListView();
        initSemesterListView();
        initSubjectListView();
        initSubjectSearch();
        getYears();
    }    
    
    private void initYearListView() {
        lvYear.setDepth(3);
        lvYear.setCellFactory((ListView<Year> param) -> {
             JFXListCell<Year> cell = new JFXListCell<Year>(){
                @Override
                protected void updateItem(Year item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                    } else {
                        //setDisable(false);
                       setText(item.getName());
                    }
                    
                }
            };
            
             cell.setPadding(new Insets(10,0,10,10));
             
            return cell;
        });
        
        lvYear.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Year>() {
            @Override
            public void changed(ObservableValue<? extends Year> observable, Year oldValue, Year newValue) {
                if(newValue!=null){
                   getSemesters(newValue);
               }
                
            }

            
        });
        
        

    }
    
    private void initSemesterListView() {
        lvSemester.setDepth(3);
        lvSemester.setCellFactory((ListView<Semester> param) -> {
             JFXListCell<Semester> cell = new JFXListCell<Semester>(){
                @Override
                protected void updateItem(Semester item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                    } else {
                        //setDisable(false);
                       setText(item.getName());
                    }
                    
                }
            };

             
             cell.setPadding(new Insets(10,0,10,10));
             
            return cell;
        });
        
        lvSemester.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Semester>() {
            @Override
            public void changed(ObservableValue<? extends Semester> observable, Semester oldValue, Semester newValue) {
               if(newValue!=null){
                   getSubjects(newValue);
               }
            }
        });
    }
    
    private void initSubjectListView(){
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
                          
                          openSubjectCoursess(cell.getItem());
                      }
                     
                 }
             });
             
             cell.setPadding(new Insets(10,0,10,10));
             
            return cell;
        });
        

        
    }
    
    private void initSubjectSearch(){
        vbSearch.setVisible(false);
         
        tfSerachSubject.setOnKeyReleased(e->{
            lvSearchSubject.getItems().clear();
            if(!tfSerachSubject.getText().trim().isEmpty())
                lvSearchSubject.getItems().addAll(SubjectDao.getAllSubjectBuName(tfSerachSubject.getText().trim()));
        });
                
        lvSearchSubject.setDepth(3);
        lvSearchSubject.setCellFactory((ListView<Subject> param) -> {
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
                            openSubjectCoursess(cell.getItem());
                        }
                 }
             });
             
             cell.setPadding(new Insets(10,0,10,10));
             
            return cell;
        });
        
        
        
    }

    

    private void getYears() {
        lvYear.getItems().addAll(YearDao.getYears());

    }
    private void getSemesters(Year year) {
        lvSemester.getItems().clear();
        lvSubject.getItems().clear();
        
        lvSemester.getItems().addAll(SemesterDao.getSemesterByYear(year));
    }
    
     private void getSubjects(Semester semester) {
         lvSubject.getItems().clear();
         lvSubject.getItems().addAll(SubjectDao.getAllSubject(semester));
     }
     @FXML
    private void addYear() {
        
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #2c2c54;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
            
            Year year=new Year();
            year.setId(0);
            year.setName(text.getText().trim());
            if(year.insert())
                lvYear.getItems().add(year);    

        });
        DialogBuilder.build("اسم السنة", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
    }

    @FXML
    private void addSemester(ActionEvent event) {
        Year year=lvYear.getSelectionModel().getSelectedItem();
        if(year==null)
            return;
        
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #2c2c54;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
            
            Semester semester=new Semester();
            semester.setId(0);
            semester.setName(text.getText().trim());
            semester.setYearId(year.getId());
            if(semester.insert())
                lvSemester.getItems().add(semester);    

        });
        DialogBuilder.build("اسم الفصل", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
        
    }

    @FXML
    private void addSubject(ActionEvent event) {
       Semester semester=lvSemester.getSelectionModel().getSelectedItem();
       if(semester==null)
           return;
      
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #2c2c54;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
            
            Subject subject=new Subject();
            subject.setId(0);
            subject.setName(text.getText().trim());
            subject.setSemesterId(semester.getId());
            if(subject.insert())
                lvSubject.getItems().add(subject);    

        });
        DialogBuilder.build("اسم الفصل", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
       
        
    }

    @FXML
    private void search(ActionEvent event) {
        vbSearch.setVisible(!vbSearch.isVisible());
        tfSerachSubject.setText("");
        tfSerachSubject.requestFocus();
    }

    @FXML
    private void edit(ActionEvent event) {
        if(lvSubject.getSelectionModel().getSelectedItem()!=null){
            editSubject(lvSubject.getSelectionModel().getSelectedItem());
        }
        else if(lvSemester.getSelectionModel().getSelectedItem()!=null){
            editSemester(lvSemester.getSelectionModel().getSelectedItem());
        }
        else if (lvYear.getSelectionModel().getSelectedItem()!=null){
            editYear(lvYear.getSelectionModel().getSelectedItem());
        }
       
    }

    @FXML
    private void delete(ActionEvent event) {
        if(lvSubject.getSelectionModel().getSelectedItem()!=null){
            deleteSubject(lvSubject.getSelectionModel().getSelectedItem());
        }
        else if(lvSemester.getSelectionModel().getSelectedItem()!=null){
            deleteSemester(lvSemester.getSelectionModel().getSelectedItem());
        }
        else if (lvYear.getSelectionModel().getSelectedItem()!=null){
            deleteYear(lvYear.getSelectionModel().getSelectedItem());
        }
    }
    
    private void editSemester(Semester semester){
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        text.setText(semester.getName());
        text.selectAll();
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #706fd3;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
         
            semester.setName(text.getText().trim());
            if(semester.update())
                lvSemester.refresh();

        });
        DialogBuilder.build("اسم السنة", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
        
    }
    private void editYear(Year year){
        
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        text.setText(year.getName());
        text.selectAll();
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #706fd3;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
         
            year.setName(text.getText().trim());
            if(year.update())
                lvYear.refresh();

        });
        DialogBuilder.build("اسم السنة", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
        
        
    }

    private void editSubject(Subject subject) {

         
        JFXTextField text=new JFXTextField();
        text.setStyle("-fx-font-size:18;-fx-font-weight:bold");
        text.setText(subject.getName());
        text.selectAll();
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color: #706fd3;"
                + " -fx-text-fill:white;");
        btn.setOnAction(e->{
            if(text.getText().isEmpty()){
                return;
            }
         
            subject.setName(text.getText().trim());
            if(subject.update())
                lvSubject.refresh();

        });
        DialogBuilder.build("اسم السنة", Arrays.asList(btn), text,DialogBuilder.COLOR_DARK);
    }
    
    private void deleteYear(Year year){
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color: "+DialogBuilder.COLOR_RED+"; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            year.delete();
            lvYear.getItems().remove(year);
        });
        
        if(year.canDelete()){
            DialogBuilder.build("هل تريد حذف :"+year.getName(), Arrays.asList(btn),DialogBuilder.COLOR_RED);
        }else{
            DialogBuilder.build("لا يمكن حذف السنة لأنها تحوي فصول !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
        }
        
    }
    private void deleteSemester(Semester semester){
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color: "+DialogBuilder.COLOR_RED+"; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            semester.delete();
            lvSemester.getItems().remove(semester);
        });
        
        if(semester.canDelete()){
            DialogBuilder.build("هل تريد حذف :"+semester.getName(), Arrays.asList(btn),DialogBuilder.COLOR_DARK);
        }else{
            DialogBuilder.build("لا يمكن حذف الفصل لانة يحوي مواد !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
        }
        
        
    }
    private void deleteSubject(Subject subject){
        JFXButton btn=new JFXButton("حذف");
        btn.setStyle("-fx-background-color: "+DialogBuilder.COLOR_RED+"; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            subject.delete(this);
            lvSubject.getItems().remove(subject);
            
        });
        
        if(subject.canDelete()){
            DialogBuilder.build("هل تريد حذف :"+subject.getName(), Arrays.asList(btn),DialogBuilder.COLOR_RED);
        }
        else{
            DialogBuilder.build(" لا يمكن حذف المادة لانها تحوي كورسات !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
        }
    }
    
    
    private void openSubjectCoursess(Subject subject){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("SubjectCourses.fxml"));
            Parent root=loader.load();
            SubjectCoursesController controller=loader.getController();
            controller.setSubject(subject);
            SceneAdapter.addNewScene(root, null);
        } catch (IOException ex) {
            Logger.getLogger(SubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
