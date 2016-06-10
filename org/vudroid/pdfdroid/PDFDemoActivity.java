package org.vudroid.pdfdroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFDemoActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PDFManager.initAssetsFontLib(this, "stzhongs.ttf")) {
            String fontPath = PDFManager.getFontPath(this);
            Document document = new Document();
            String path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/cnlaunch/X431Pro/manual_cn.pdf").toString();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();
                Font titleFont = new Font(BaseFont.createFont(fontPath, "Identity-H", true), 18.0f, 0);
                document.add(new Paragraph("Hello World"));
                document.add(new Paragraph("\u6211\u662f\u4e2d\u56fd\u4ebasafdasdfasdf", titleFont));
                Image png1 = Image.getInstance(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/cnlaunch/headimg.png").toString());
                png1.setAlignment(1);
                png1.scaleAbsolute(100.0f, 100.0f);
                document.add(png1);
                if (document != null) {
                    document.close();
                }
            } catch (DocumentException de) {
                de.printStackTrace();
                if (document != null) {
                    document.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                if (document != null) {
                    document.close();
                }
            } catch (Throwable th) {
                if (document != null) {
                    document.close();
                }
            }
            PDFManager.open(this, path);
        }
    }
}
