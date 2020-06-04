/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report;

import Additional.SceneAdapter;
import Dao.CoursePaymentDao;
import Model.CoursePayment;
import PDF.CashReportPdf;
import PDF.PdfCreater;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class CashReportController implements Initializable {

    @FXML
    private JFXDatePicker fromDate;
    @FXML
    private JFXDatePicker toDate;
    @FXML
    private TableView<CoursePayment> tvPayments;
    @FXML
    private TableColumn<CoursePayment, Long> idCol;
    @FXML
    private TableColumn<CoursePayment, String> typeCol;
    @FXML
    private TableColumn<CoursePayment, Double> amountCol;
    @FXML
    private TableColumn<CoursePayment, String> dateCol;
    @FXML
    private TableColumn<CoursePayment, String> usernameCol;
    @FXML
    private TableColumn<CoursePayment, String> noteCol;
    @FXML
    private TableColumn<CoursePayment, String> studentCol;
    @FXML
    private TableColumn<CoursePayment, String> subjectCol;
    
    
    @FXML
    private TableView<CoursePayment> tvSummary;
    @FXML
    private TableColumn<CoursePayment, Double> sumAmountCol;
    @FXML
    private TableColumn<CoursePayment, String> sumTypeCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
          
        initTableView();
        getReport();
    }    

    private void initTableView() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        
       
        sumAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        sumTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

    }
    
    
    @FXML
    public  void getReport(){
        tvPayments.getItems().clear();
        tvPayments.getItems().addAll(CoursePaymentDao.getPayments(fromDate.getValue().toString(), toDate.getValue().toString()));
        
        tvSummary.getItems().clear();
        tvSummary.getItems().add(CoursePaymentDao.getSummary(fromDate.getValue().toString(), toDate.getValue().toString()));
    }
    
    @FXML
    public void close(){
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void createPdf(ActionEvent event) {
        if(tvPayments.getItems().isEmpty())
            return;
        PdfCreater.print(new CashReportPdf(tvPayments.getItems(), fromDate.getValue().toString(), toDate.getValue().toString()));
    }
    
}
