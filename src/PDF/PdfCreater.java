/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PDF;

import Additional.SceneAdapter;
import Model.CourseStudent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import javafx.collections.ObservableList;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author ASUS
 */
public abstract class PdfCreater{
    public static final Font INFO_FONT=FontFactory.getFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H,20,Font.BOLD);
    public static final Font HEADER_FONT=FontFactory.getFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H,14,Font.BOLD,BaseColor.WHITE);
    public static final Font CELL_FONT=FontFactory.getFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H,12);

    protected static String dir,dest;    
  //  private static PdfCreater intstance;
    
   public static void print(PdfCreater creater){
      // intstance=creater;
       dir=getDirectory();
       if(dir.isEmpty())
            return;
        dest = dir+"\\"+
                    LocalDate.now().format(DateTimeFormatter.ofPattern("y-M-d"))+" "+
                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-S"))+" ";
            
      new Thread(new Runnable() {
           @Override
           public void run() {
               creater.create();
           }
       }).start();
       
   }
   
   public abstract void create();
   
    
     public static void openFile(String s){
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(s);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
            }
        }
    }
     
    public static String getDirectory(){
        
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Courses management");
        File defaultDirectory = new File("c:/");
        
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog((Stage)SceneAdapter.getMainStackPane().getScene().getWindow());
        if(selectedDirectory!=null){
            return selectedDirectory.getAbsolutePath();
        
        }

        return "";
    }
    
    public static PdfPCell getInfo(String s,Font f){
      PdfPCell cell = new PdfPCell();
      Paragraph p =new Paragraph(s,f);
      p.setAlignment(0);
      cell.addElement(p);
      cell.setUseDescender(true);
      cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
      cell.setBorder(0);
      return cell;
    }
    
    
    public static PdfPCell getHeader(String s,Font f){
      PdfPCell cell = new PdfPCell();
      Paragraph p =new Paragraph(s,f);
      p.setAlignment(1);
      cell.addElement(p);
      cell.setUseDescender(true);
      cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
      cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
      return cell;
    }
    
    public static PdfPCell getCell(String s,Font f){
      PdfPCell cell = new PdfPCell();
      Paragraph p =new Paragraph(s,f);
      p.setAlignment(1);
      cell.addElement(p);
      cell.setUseDescender(true);
      cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
      return cell;
    }
    
    class Header extends PdfPageEventHelper {
        Font font;
        
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            try {
                font =  new Font(BaseFont.createFont("C:/Windows/Fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            } catch (IOException ex) {
                Logger.getLogger(PdfCreater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(PageSize.A4.getWidth()-90);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.addCell(new Phrase(" ", font));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(new Phrase(String.valueOf(writer.getPageNumber()), font));
            table.addCell(new Phrase(" ", font));
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ART);
            table.writeSelectedRows(0, -1, 36, 30, canvas);
            canvas.endMarkedContentSequence();

        }
        
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {

        }
    }
    
    
}
