/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Course;

import Additional.DialogBuilder;
import Additional.SceneAdapter;
import Model.CoursePayment;
import Model.CourseStudent;
import Model.Student;
import Model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class RecieveController implements Initializable {

    @FXML
    private JFXTextField tfStudent;
    @FXML
    private JFXTextField tfCash;

    CourseStudent courseStudent;
    @FXML
    private JFXTextArea taNote;
    @FXML
    private JFXRadioButton rbRecive;
    @FXML
    private JFXRadioButton rbPay;
    
    ToggleGroup typeTG;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        typeTG=new ToggleGroup();
        rbRecive.setToggleGroup(typeTG);
        rbPay.setToggleGroup(typeTG);
        rbRecive.setSelected(true);
        
        Platform.runLater(()->{tfCash.requestFocus();});
        
    }    

    @FXML
    private void close() {
        SceneAdapter.closeCurrentScene();
    }

    @FXML
    private void save(ActionEvent event) {
        
        JFXButton btn=new JFXButton("حفظ");
        btn.setStyle("-fx-background-color:"+DialogBuilder.COLOR_DARK+" ; -fx-text-fill:#ffffff");
        btn.setOnAction(e->{
            insert();
            updatePaidAmount();
            close();
        });
        if(checkValidation())
        {
            String title=(rbRecive.isSelected()?" قبض":"ترجيع ")+" "+tfCash.getText().trim() ;
           DialogBuilder.build("حفظ العملية: "+title+" ؟", Arrays.asList(btn), DialogBuilder.COLOR_DARK);
        }
    }

    void setPayMode(CourseStudent courseStudent) {
        this.courseStudent=courseStudent;
        tfStudent.setText(this.courseStudent.getStudentName());
    }

    private void insert() {
    
        double amount=Double.parseDouble(tfCash.getText().trim());
        CoursePayment coursePayment=new CoursePayment();
        coursePayment.setCourseId(courseStudent.getCourseId());
        coursePayment.setStudentId(courseStudent.getStudentId());
        coursePayment.setAmount(rbRecive.isSelected()?amount:(-1*amount));
        coursePayment.setNote(taNote.getText().trim().isEmpty()?" ":taNote.getText().trim());
        coursePayment.setUserId(User.getCurrentUser().getId());
        coursePayment.insert();
        

    }

    private boolean checkValidation() {
        if(tfCash.getText().trim().isEmpty()){
            DialogBuilder.build("الرجاء ادخال المبلغ !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            tfCash.requestFocus();
            return false;
        }
        if(!tfCash.getText().trim().matches("\\d+\\.?\\d*")){
            DialogBuilder.build("الرجاء إدخال المبلغ بالشكل الصحيح", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        if(rbRecive.isSelected()&&Double.parseDouble(tfCash.getText().trim())>(this.courseStudent.getDeservedAmount()-courseStudent.getPaidAmount())){
            DialogBuilder.build("المبلغ المدفوع اكبر من المبلغ المستحق !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        if(rbPay.isSelected()&&Double.parseDouble(tfCash.getText().trim())>this.courseStudent.getPaidAmount()){
             DialogBuilder.build("المرتجع اكبر من المبلغ المدفوع !", Arrays.asList(),DialogBuilder.COLOR_YELLWO);
            return false;
        }
        
        return true;
    }

    private void updatePaidAmount() {
        double amount=Double.parseDouble(tfCash.getText().toString());
        if(rbPay.isSelected())
            amount*=-1;
        courseStudent.setPaidAmount(courseStudent.getPaidAmount()+amount);
        courseStudent.update();
    }
    
}
