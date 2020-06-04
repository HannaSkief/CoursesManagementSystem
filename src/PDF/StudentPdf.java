/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Model.CourseStudent;
import Model.Student;
import Model.StudentMovement;
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
public class StudentPdf extends PdfCreater{

    ObservableList<StudentMovement> movementList;
    Student student;
    
    private double totalDeservedAmount,totalPaidAmount;
    public StudentPdf(ObservableList<StudentMovement> movementList,Student student) {
        this.movementList=movementList;
        this.student=student;
        totalDeservedAmount=0;
        totalPaidAmount=0;
    }

    @Override
    public void create() {
        dest+=" "+student.getName()+" .pdf";
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
            table1.addCell(getInfo(student.getName(),INFO_FONT));
            table1.addCell(getInfo(LocalDate.now().toString(),CELL_FONT));
            
            document.add(table1);
            
            float w=PageSize.A4.getWidth()-20;
            PdfPTable table2=new PdfPTable(new float[]{2,1.5f,2,2,2,2,2,2,1.5f});
            table2.setSpacingBefore(20);
            table2.setTotalWidth(w);
            table2.setLockedWidth(true);
            
            table2.addCell(getHeader("تاريخ الإضافة", HEADER_FONT));
            table2.addCell(getHeader("حالة الطالب", HEADER_FONT));
            table2.addCell(getHeader("الباقي", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المدفوع", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المستحق", HEADER_FONT));
            table2.addCell(getHeader("حالة الدورة", HEADER_FONT));
            table2.addCell(getHeader("تاريخ الانشاء", HEADER_FONT));
            table2.addCell(getHeader("المادة", HEADER_FONT));
            table2.addCell(getHeader("رقم الدورة", HEADER_FONT));
            for(StudentMovement m:movementList){
                table2.addCell(getCell(m.getAddedAt(), CELL_FONT));
                table2.addCell(getCell(m.getStudentStatus(), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",m.getRemainAmount()), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",m.getPaidAmount()), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",m.getDeservedAmount()), CELL_FONT));
                table2.addCell(getCell(m.getCourseStatus(), CELL_FONT));
                table2.addCell(getCell(m.getCourseDate(), CELL_FONT));
                table2.addCell(getCell(m.getSubjectName(), CELL_FONT));
                table2.addCell(getCell(String.valueOf(m.getCourseId()), CELL_FONT));
                totalDeservedAmount+=m.getDeservedAmount();
                totalPaidAmount+=m.getPaidAmount();
                
            }
            
            // add the summary row
            
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getHeader(String.format("%.0f",totalDeservedAmount-totalPaidAmount), HEADER_FONT));
            table2.addCell(getHeader(String.format("%.0f",totalPaidAmount), HEADER_FONT));
            table2.addCell(getHeader(String.format("%.0f",totalDeservedAmount), HEADER_FONT));
            table2.addCell(getHeader("المجموع", HEADER_FONT));
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
