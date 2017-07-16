/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.applet.*;
import java.net.URL;
import javax.swing.*;
import java.io.*;
import org.faceless.pdf2.*;
 
/**
 * This applet can be used to load and print a PDF from the browser with
 * minimal user interaction. The PDF is not displayed.
 *
 * The PDF can be specified as the "pdf" parameter to the applet (a URL
 * relative to the page containing the applet), or it can be set by
 * calling the "load" method.
 *
 * Once loaded, if the "callback" parameter was set the applet will call
 * the method specified by that parameter. At any point after that, the
 * "print" method can be called to open a print dialog. You can alter the
 * code to print immediately if you want to.
 *
 * This code is in the public domain
 */
public class PrintPDFApplet extends Applet {
 
    private PDF pdf;
    private String callback;
 
    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
        callback = getParameter("callback");
        if (getParameter("pdf")!=null) {
            load(getParameter("pdf"));
        }
    }
 
    /**
     * Load a PDF
     * @param filename the URL of the filename (relative to the current document)
     */
    public void load(String filename) {
        try {
            URL url = new URL(getDocumentBase(), getParameter("pdf"));
            InputStream in = url.openStream();
            pdf = new PDF(new PDFReader(in));
            in.close();
            if (callback!=null) {
                reportLoaded();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Return true if the PDF is loaded
     */
    public boolean isLoaded() {
        return pdf!=null;
    }
 
    /**
     * Called when the PDF is loaded to "call back" to the JavaScript to notify
     * it of the load. This requires the "mayscript" attribute on the applet and
     * the "plugin.jar" file supplied with the JRE to be in your classpath - if
     * you don't need this functionality you can remove the contents of this method.
     */
    private void reportLoaded() {
        try {
            // This method requires the "plugin.jar" supplied with the JRE
            // to be in your ClassPath.
             
          //  netscape.javascript.JSObject.getWindow(this).call(callback, new Object[0] );
        } catch (Throwable e) {
        }
    }
 
    /**
     * Print the PDF loaded by the "load" method. This will display a dialog asking
     * the user to select a printer, papersize etc. and start the print. If you want
     * to print immediately without this dialog, you can alter the method to do that
     * quite easily - see the "PrintPDF.java" example supplied with the PDF package.
     */
    public void print() {
        try {
            if (pdf == null) {
                throw new RuntimeException("PDF not loaded");
            } else {
                PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();
                DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
 
                PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, atts);
                if (services.length > 0) {
                    // Try to set the default page size correctly
                    PDFPage page = pdf.getPage(0);
                    try {
                        float w = page.getWidth() / 72f;
                        float h = page.getHeight() / 72f;
                        if (h > w) {
                            atts.add(MediaSize.findMedia(w, h, Size2DSyntax.INCH));
                        } else {
                            atts.add(MediaSize.findMedia(h, w, Size2DSyntax.INCH));
                            atts.add(OrientationRequested.LANDSCAPE);
                        }
                    } catch (Exception e) {}
 
                    // Choose a print service
                    PrintService service =  ServiceUI.printDialog(null, 50, 50, services, null, flavor, atts);
                    if (service!=null) {
                        final PDFParser parser = new PDFParser(pdf);
                        final PrinterJob job = PrinterJob.getPrinterJob();
                        job.setPrintService(service);
                        job.setPageable(new Pageable() {
                            public int getNumberOfPages() {
                                return parser.getNumberOfPages();
                            }
                            public Printable getPrintable(int pagenumber) {
                                return parser.getPrintable(pagenumber);
                            }
                            public PageFormat getPageFormat(int pagenumber) {
                                PageFormat format = parser.getPageFormat(pagenumber);
                                Paper paper = job.defaultPage(format).getPaper();
                                paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
                                format.setPaper(paper);
                                return format;
                            }
                        });
                        job.print(atts);
                    }
                } else {
                    throw new RuntimeException("No Printers");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
}