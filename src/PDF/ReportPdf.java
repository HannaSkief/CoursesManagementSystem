/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Model.Course;
import Model.CoursePayment;
import static PDF.PdfCreater.dest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author ASUS
 */
public class ReportPdf extends PdfCreater{

    ObservableList<Course> courseList;
    private double totalNumberOfStudent,totalDeservedAmount,totalRecivedAmount;

    public ReportPdf(ObservableList<Course> courseList) {
        this.courseList = courseList;
        totalDeservedAmount=0;
        totalNumberOfStudent=0;
        totalRecivedAmount=0;
    }
    
    
    @Override
    public void create() {
        dest+="التقارير"+" .pdf";
         try {
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setPageEvent(new Header());
            document.open();
            
            PdfPTable table1 = new PdfPTable(1);
            table1.setTotalWidth(PageSize.A4.getWidth()-20);
            table1.setLockedWidth(true);
            table1.setHorizontalAlignment(2);
            table1.addCell(getInfo("التقارير",INFO_FONT));
            table1.addCell(getInfo(LocalDate.now().toString(),CELL_FONT));
            
            document.add(table1);
            
            float w=PageSize.A4.getWidth()-20;
            PdfPTable table2=new PdfPTable(8);
            table2.setSpacingBefore(20);
            table2.setTotalWidth(w);
            table2.setLockedWidth(true);
            
            table2.addCell(getHeader("المبلغ المقبوض", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المستحق", HEADER_FONT));
            table2.addCell(getHeader("عدد الطلاب", HEADER_FONT));
            table2.addCell(getHeader("تاريخ الانشاء", HEADER_FONT));
            table2.addCell(getHeader("التكلفة", HEADER_FONT));
            table2.addCell(getHeader("الحالة", HEADER_FONT));
            table2.addCell(getHeader("المادة", HEADER_FONT));
            table2.addCell(getHeader("رقم الدورة", HEADER_FONT));
            for(Course c:courseList){
                table2.addCell(getCell(String.format("%.0f",c.getRecivedAmount()), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",c.getDeservedAmount()), CELL_FONT));
                table2.addCell(getCell(String.valueOf(c.getNumberOfStudent()), CELL_FONT));
                table2.addCell(getCell(c.getDate(), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",c.getCost()), CELL_FONT));
                table2.addCell(getCell(c.getStatus(), CELL_FONT));
                table2.addCell(getCell(c.getSubjectName(), CELL_FONT));
                table2.addCell(getCell(String.valueOf(c.getId()), CELL_FONT));
                
                totalNumberOfStudent+=c.getNumberOfStudent();
                totalDeservedAmount+=c.getDeservedAmount();
                totalRecivedAmount+=c.getRecivedAmount();
            }
            
            // add the summary row
            
            table2.addCell(getHeader(String.format("%.0f",totalRecivedAmount), HEADER_FONT));
            table2.addCell(getHeader(String.format("%.0f",totalDeservedAmount), HEADER_FONT));
            table2.addCell(getHeader(String.valueOf(totalNumberOfStudent), HEADER_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            
            document.add(table2);
            

            document.close();
            
            openFile(dest);
            
        } catch (DocumentException ex) {
            Logger.getLogger(PdfCreater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        

    }
    
}
