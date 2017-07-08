/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author mmixco
 */
/**
 *
 * @author mmixco
 */
@ManagedBean(name = "readXls")
@ViewScoped
public class ReadXls {

File inputWorkbook;
 
private String destination="/opt/lib/";
public String inputFile;

public String getInputFile() {
    return inputFile;
}

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }  
  
    public void handleFileUpload(FileUploadEvent event) throws IOException {  
        this.setInputFile(event.getFile().getInputstream().toString());   
        FacesMessage msg = new FacesMessage("bien", event.getFile().getFileName() + " is subida.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }      

    public void upload(FileUploadEvent event) {        
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);           
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }  

    public void copyFile(String fileName, InputStream in) {
           try {
             
                OutputStream out = new FileOutputStream(new File(destination + fileName));             
                int read = 0;
                byte[] bytes = new byte[1024];             
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }             
                in.close();
                out.flush();
                out.close();
                         
                System.out.println("New file created!");
                } catch (IOException e) {
                System.out.println(e.getMessage());
                }
    }
    
    
      
      
      
 

      
}