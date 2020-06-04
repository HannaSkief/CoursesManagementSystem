/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Model.CoursePayment;
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
public class CashReportPdf extends PdfCreater{

    private ObservableList<CoursePayment> paymentList;
    String fromDate,toDate;
    private double totalAmount;
    public CashReportPdf(ObservableList<CoursePayment> paymentList,String fromDate,String toDate) {
        this.paymentList = paymentList;
        this.fromDate=fromDate;
        this.toDate=toDate;
        totalAmount=0;
    }
    
    @Override
    public void create() {
        dest+=" الحركة المالية"+" .pdf";
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
            table1.addCell(getInfo("الحركة المالية",INFO_FONT));
            table1.addCell(getInfo("من :"+" "+fromDate,CELL_FONT));
            table1.addCell(getInfo("الى:"+" "+toDate,CELL_FONT));
            table1.addCell(getInfo(LocalDate.now().toString(),CELL_FONT));
            
            document.add(table1);
            
            float w=PageSize.A4.getWidth()-20;
            PdfPTable table2=new PdfPTable(8);
            table2.setSpacingBefore(20);
            table2.setTotalWidth(w);
            table2.setLockedWidth(true);
            
            table2.addCell(getHeader("ملاحظات", HEADER_FONT));
            table2.addCell(getHeader("المستخدم", HEADER_FONT));
            table2.addCell(getHeader("التاريخ", HEADER_FONT));
            table2.addCell(getHeader("المبلغ", HEADER_FONT));
            table2.addCell(getHeader("نوع الحركة", HEADER_FONT));
            table2.addCell(getHeader("المادة", HEADER_FONT));
            table2.addCell(getHeader("الطالب", HEADER_FONT));
            table2.addCell(getHeader("رقم الحركة", HEADER_FONT));
            for(CoursePayment cp:paymentList){
                table2.addCell(getCell(cp.getNote(), CELL_FONT));
                table2.addCell(getCell(cp.getUserName(), CELL_FONT));
                table2.addCell(getCell(cp.getDate(), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",cp.getAmount()), CELL_FONT));
                table2.addCell(getCell(cp.getType(), CELL_FONT));
                table2.addCell(getCell(cp.getSubjectName(), CELL_FONT));
                table2.addCell(getCell(cp.getStudentName(), CELL_FONT));
                table2.addCell(getCell(String.valueOf(cp.getId()), CELL_FONT));
                if(cp.getType().equals("قبض"))
                    totalAmount+=cp.getAmount();
                else
                    totalAmount-=cp.getAmount();
            }
            
            // add the summary row
            
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getCell("", CELL_FONT));
            table2.addCell(getHeader("", HEADER_FONT));
            table2.addCell(getHeader(String.format("%.0f",totalAmount), HEADER_FONT));
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
