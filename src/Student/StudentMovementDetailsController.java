/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Student;

import Additional.SceneAdapter;
import Dao.CoursePaymentDao;
import Model.CoursePayment;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class StudentMovementDetailsController implements Initializable {

    @FXML
    private TableView<CoursePayment> tvPayments;
    @FXML
    private TableColumn<CoursePayment, Long> idCol;
    @FXML
    private TableColumn<CoursePayment, Double> amountCol;
    @FXML
    private TableColumn<CoursePayment, String> noteCol;
    @FXML
    private TableColumn<CoursePayment, String> dateCol;
    @FXML
    private TableColumn<CoursePayment, String> userNameCol;
    @FXML
    private TableColumn<CoursePayment,String> typeCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTableView();
    }    
    
    private void initTableView(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        
        
    }
    
    public void setCourseStudent(long courseId,long studentId){
        tvPayments.getItems().addAll(CoursePaymentDao.getPayments(courseId,studentId));
    }
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }
    
}
