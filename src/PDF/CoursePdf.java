/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Model.CourseStudent;
import static PDF.PdfCreater.CELL_FONT;
import static PDF.PdfCreater.HEADER_FONT;
import static PDF.PdfCreater.INFO_FONT;
import static PDF.PdfCreater.getCell;
import static PDF.PdfCreater.getHeader;
import static PDF.PdfCreater.getInfo;
import static PDF.PdfCreater.openFile;
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
public class CoursePdf extends PdfCreater{

    private ObservableList<CourseStudent> csList;
    public CoursePdf(ObservableList<CourseStudent> csList) {
        this.csList=csList;
    }

    
    @Override
    public void create() {

         dest+=" "+csList.get(0).getSubjectName()+".pdf";
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
            table1.addCell(getInfo(csList.get(0).getSubjectName(),INFO_FONT));
            table1.addCell(getInfo("رقم الدورة :"+csList.get(0).getCourseId(),CELL_FONT));
            table1.addCell(getInfo(LocalDate.now().toString(),CELL_FONT));
            
            document.add(table1);
            
            float w=PageSize.A4.getWidth()-20;
            PdfPTable table2=new PdfPTable(new float[]{w/7,w/7,w/7,w/7,w/7,w*3/14,w/14});
            table2.setSpacingBefore(20);
            table2.setTotalWidth(w);
            table2.setLockedWidth(true);
            
            table2.addCell(getHeader("تاريخ الإضافة", HEADER_FONT));
            table2.addCell(getHeader("حالة الطالب", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المتبقي", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المدفوع", HEADER_FONT));
            table2.addCell(getHeader("المبلغ المستحق", HEADER_FONT));
            table2.addCell(getHeader("اسم الطالب", HEADER_FONT));
            table2.addCell(getHeader("", HEADER_FONT));
            int i=1;
            for(CourseStudent cs:csList){
                table2.addCell(getCell(cs.getDate(), CELL_FONT));
                table2.addCell(getCell(cs.getStatus(), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",cs.getRemainAmount()), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",cs.getPaidAmount()), CELL_FONT));
                table2.addCell(getCell(String.format("%.0f",cs.getDeservedAmount()), CELL_FONT));
                table2.addCell(getCell(cs.getStudentName(), CELL_FONT));
                table2.addCell(getCell(String.valueOf(i++), CELL_FONT));
            }
            
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
