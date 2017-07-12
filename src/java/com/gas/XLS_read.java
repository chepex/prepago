/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import com.gas.util.JsfUtil;
import com.it.XlsTemporal;
import com.it.XlsTemporalFacade;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import jxl.Cell;
 
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
 

/**
 *
 * @author mmixco
 */
@Stateless
@LocalBean
public class XLS_read {    
    @EJB
    private XlsTemporalFacade xlsTemporalFacade;    
 private File inputWorkbook;    
    
    
 @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)  
 public String read(String mas) throws IOException, BiffException  {        
      String mensaje;
      try{
           this.inputWorkbook= new File(mas);
           Workbook w=null;       
           w = Workbook.getWorkbook(inputWorkbook);     
           Sheet sheet = w.getSheet(0);      
           
              mensaje= guardar(sheet);
          
      }   
      catch(Exception ex){
        return "error";          
   }   
        return "ok " +mensaje;  
 }
 
    public String guardar(Sheet sheet){   
     
     String msg = "ok";
      int xx=0; 
      int total=0; 
       
        String empresa  =""; 
        String estacion  =""; 
        String usuario  =""; 
        String nombre  =""; 
        String apellido  =""; 
        
        BigDecimal carga = xlsTemporalFacade.findCarga();
     
     
        for (int i = 0; i < sheet.getRows(); i++) { 
         
 
            for (int j = 0; j < sheet.getColumns(); j++) {              
                
                Cell cell = sheet.getCell(j, i);
                if(j==0){                
                    empresa =  String.valueOf(cell.getContents());
                }          
                if(j==1){                   
                   estacion = String.valueOf(cell.getContents());
                }   
                if(j==2){                   
                   usuario =  String.valueOf(cell.getContents());
                }  
                if(j==3){                   
                   nombre = String.valueOf(cell.getContents());
                }  
                if(j==4){                   
                   apellido = String.valueOf(cell.getContents());
                } 
                                          
            }
             try{
                 BigDecimal id = xlsTemporalFacade.findID();                 
                 XlsTemporal temp = new XlsTemporal(id);
                 temp.setCarga(carga);
                 temp.setCampo1(empresa);
                 temp.setCampo2(estacion);                                  
                 temp.setCampo3(usuario);                                  
                 temp.setCampo4(nombre);                                  
                 temp.setCampo5(apellido);                                  
                 xlsTemporalFacade.edit(temp);
                 total = total +1;
               
                 }catch(Exception ex){
                JsfUtil.addErrorMessage("Surgio un error en linea: "+i);
                
            }
        } 
        
        msg = "Total lineas cargadas = "+ total;
       
    return msg;
    }   
}
