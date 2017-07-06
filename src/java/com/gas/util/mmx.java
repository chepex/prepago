/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.util;


 

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.ejb.LocalBean;

 
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


/**
 *
 * @author mmixco
 */
@Stateless
public class mmx {
    
       // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String generar (){
    return "iok";
    }       
    
    
     
    public String mas (){

    return "ok";
    }
    
       static { /* works fine! ! */
      System.setProperty("java.awt.headless", "true");
      System.out.println(java.awt.GraphicsEnvironment.isHeadless());
      /* ---> prints true */
    }

       
   public String GenerarReporte(String rep, HashMap params)   {
       
       System.out.println("----->--->2");
       System.out.println("----->--->2");
       System.out.println("----->--->2");
       
         
             
       try{ 
            String reporPath = "";
            reporPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(rep);
            
         
            
            Context ctx= new InitialContext();
            //LoginBean lb = new LoginBean();
            //String ip = lb.getServerIP();
            
            System.out.println("----->--->3");
            System.out.println("----->--->3");
            System.out.println("----->--->3");
             Connection cn  = null;
          
            DataSource ds;
            //Desarrollo
            //ds = (DataSource)ctx.lookup("jdbc/desa");
            //Produccion 
            ds = (DataSource)ctx.lookup("jdbc/sicgas");

            
          
            
     
           
              cn = ds.getConnection();
  
             HashMap params2 = new HashMap();
             
            JasperPrint jasperprint = JasperFillManager.fillReport(reporPath, params, cn);
            HttpServletResponse httpserveltresponse= (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpserveltresponse.setContentType("application/pdf");
            ServletOutputStream servletOutputStream= null;          
            servletOutputStream = httpserveltresponse.getOutputStream();
         
            
            JasperExportManager.exportReportToPdfStream(jasperprint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
       }
       
               
       catch (NamingException ex){
           System.out.println("----------------------------------------:IOException"+ex.getMessage());
       }        
       catch (IOException ex){
           System.out.println("----------------------------------------:IOException"+ex.getMessage());
       }
       
       catch (JRException ex)
        {
            System.out.println("----------------------------------------:JRException"+ex.getMessage());
            
        }
        catch(ClassCastException ex)
        {
            System.out.println("----------------------------------------:ClassCastException"+ex.getMessage());
        }
        catch (SQLException ex)
        {
            System.out.println("----------------------------------------:SQLException"+ex.getMessage());
        }
            
        return "";
      
    }    
    
   
 public String GenerarReporte2(String rep, HashMap params)  {
         
        try {
            String reporPath = "";
            reporPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(rep);
            Context ctx= new InitialContext();
            //LoginBean lb = new LoginBean();
            //String ip = lb.getServerIP();
            DataSource ds;
            //Desarrollo
            //ds = (DataSource)ctx.lookup("jdbc/desa");
            //Produccion 
            ds = (DataSource)ctx.lookup("jdbc/sicgas");

             Connection cn = ds.getConnection();
            JasperPrint jasperprint = JasperFillManager.fillReport(reporPath, params, cn);
            HttpServletResponse httpserveltresponse= (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpserveltresponse.setContentType("application/pdf");
            ServletOutputStream servletOutputStream = httpserveltresponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperprint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (NamingException | SQLException | JRException | IOException ex) {
            System.out.println("Error"+ex);
            System.out.println("Error"+ex);
            System.out.println("Error"+ex);
        }
        return "";
    }   
   
    
}

