/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas;






 

import com.gas.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB; 
import javax.enterprise.context.SessionScoped; 

import javax.faces.context.FacesContext;

import javax.inject.Named;
 

import javax.servlet.http.Cookie; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author mmixco
 */

@Named("loginBean")
@SessionScoped
public class LoginBean  implements Serializable {
    private static final long serialVersionUID = 5443351151396868724L;
    private String username;
    private BigInteger vislero;
    private String password;
    private GasEstacion gasEstacion;
    private GascatUsuario gascatUsuario;    
    
    @EJB
    private GascatUsuarioFacade gascatUsuarioFacade;
    @EJB
    private SB_token sb_token;
    
    
    private String rol ;
  
    
  
    private String token;
    
    public LoginBean() {
    }

    public BigInteger getVislero() {
        return vislero;
    }

    public void setVislero(BigInteger vislero) {
        this.vislero = vislero;
    }

    
    
    public GascatUsuario getGascatUsuario() {
        return gascatUsuario;
    }

    public void setGascatUsuario(GascatUsuario gascatUsuario) {
        this.gascatUsuario = gascatUsuario;
    }
    
    

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    

    public GasEstacion getGasEstacion() {
        return gasEstacion;
    }

    public void setGasEstacion(GasEstacion gasEstacion) {
        this.gasEstacion = gasEstacion;
    }
    
    

    public String getToken() {
        
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   
    
    public String  loginIslero() throws IOException   {
                              
        List<GascatUsuario> lusuario = gascatUsuarioFacade.findByLoginEstacionIslero(this.vislero, this.password, this.gasEstacion);
       
        if(lusuario.isEmpty()){
            JsfUtil.addErrorMessage("Error","Usuario o Password invalido");
            return "error";
        }      
        GascatUsuario GC =  lusuario.get(0);  
        System.out.println("usuario--->"+GC);
        rol = GC.getCodigoTiporol().getCodigoTiporol();
        System.out.println("Rol---->"+rol);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);          
      
        
     
        session.setAttribute("SSUSUARIO", GC.getGascatUsuarioPK().getUsername() );     
        session.setAttribute("SSESTACION", gasEstacion );     
        
        
        GC.setConectado(true);
        GC.setUltimoAcceso(new Date());        
        String tk = generateToken(GC.getGascatUsuarioPK().getUsername());
        this.token =tk;
        GC.setToken(tk);
        System.out.println("login token--->"+tk);
        gascatUsuario = GC;
        token = tk;
        gascatUsuarioFacade.edit(GC);
        
        sb_token.setToken(tk);
        
        JsfUtil.addSuccessMessage("Usuario Conectado");      
        
        
        return "ok";
    }

  public String  login() throws IOException   {
                              
        List<GascatUsuario> lusuario = gascatUsuarioFacade.findByLoginEstacion(this.username, this.password, this.gasEstacion);
        System.out.println("usuario-->" +this.username);
        System.out.println("password-->" +this.password);
        System.out.println("gasEstacion-->" +this.gasEstacion);
        if(lusuario.isEmpty()){
            JsfUtil.addErrorMessage("Error","Usuario o Password invalido");
            return "error";
        }      
        GascatUsuario GC =  lusuario.get(0);  
        System.out.println("usuario--->"+GC);
        rol = GC.getCodigoTiporol().getCodigoTiporol();
        System.out.println("Rol---->"+rol);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);          
        /*pasarGarbageCollector();
        
        request.getSession(true);                
        request.getSession().setMaxInactiveInterval(900);   */
        session.setAttribute("SSUSUARIO", GC.getGascatUsuarioPK().getUsername() );     
        session.setAttribute("SSESTACION", gasEstacion );     
        
        
        GC.setConectado(true);
        GC.setUltimoAcceso(new Date());        
        String tk = generateToken(username);
        this.token =tk;
        GC.setToken(tk);
        System.out.println("login token--->"+tk);
        gascatUsuario = GC;
        token = tk;
        gascatUsuarioFacade.edit(GC);
        
        sb_token.setToken(tk);
        
        if(rol.equals("ADM")){
            FacesContext.getCurrentInstance().getExternalContext().redirect("gasPrepago/List.xhtml");
        }else{
            FacesContext.getCurrentInstance().getExternalContext().redirect("gasPrepago/Consulta.xhtml");
        }        
        
        
        return "ok";
    }
     
    public void cerrar() {
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
        String usuario = String.valueOf(session.getAttribute("SSUSUARIO" ));
        
        List<GascatUsuario> luser = gascatUsuarioFacade.findByToken(token);
        if(!luser.isEmpty()){
            GascatUsuario us = luser.get(0);
            us.setToken("0");
            us.setConectado(false);
            gascatUsuarioFacade.edit(us);
        }
        List<GascatUsuario> luser2 = gascatUsuarioFacade.findByUsername(usuario);
        if(!luser2.isEmpty()){
            GascatUsuario us = luser2.get(0);
            us.setToken("0");
            us.setConectado(false);
            gascatUsuarioFacade.edit(us);
        }
        
        sb_token.setToken(null);
          
          
    }   
    
   public String  logoutIslero() throws IOException   {        
            cerrar() ;
            gascatUsuario = null;
            token = null;    
            System.out.println("<--------------------------->");
            System.out.println("<--------------------------->");
           
            // pasarGarbageCollector();
             HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
            if (session != null) {
                 session.removeAttribute("SSUSUARIO");
                 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();


            }
            this.vislero = null;
            this.username = null;
            this.password = null;
            JsfUtil.addSuccessMessage("Sesion cerrada");
            
            
        return "ok";
    } 
   
    
    public String  logout() throws IOException   {
        
        cerrar() ;
         
        gascatUsuario = null;
        token = null;    
        System.out.println("<--------------------------->");
        System.out.println("<--------------------------->");
           /* HttpServletRequest httpResponse = (HttpServletRequest)  FacesContext.getCurrentInstance().getExternalContext().getRequest();         
            Cookie[] cookies = httpResponse.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            System.out.println("ubicacion--->"+cookies[i].getPath());
            System.out.println("domain --->"+cookies[i].getDomain());
            System.out.println("Name: " + cookies[i].getName() + "; Value: " + cookies[i].getValue());            
        }*/
       // requestCookieMap.put("Usuario",username );
        System.out.println("1------------->");
        System.out.println("1------------->");
      //  System.out.println("1------------->"+requestCookieMap.get("Usuario"));
        System.out.println("1------------->");
           
         
       
       // pasarGarbageCollector();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
       if (session != null) {
               session.removeAttribute("SSUSUARIO");
              
     //         FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
       
       }
     /*  FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");*/       
       // test.conectado( "0");
        
        return "ok";
    }      
        
    public void pasarGarbageCollector(){
 
        Runtime garbage = Runtime.getRuntime();
        System.out.println("Memoria libre antes de limpieza: "+ garbage.freeMemory() );
        System.out.println("Memoria libre antes de limpieza: "+ garbage.freeMemory() );
 
        garbage.gc();
 
        System.out.println("Memoria total: "+ garbage.totalMemory());
        System.out.println("Memoria libre antes de limpieza: "+ garbage.freeMemory() );
    } 
    
    public boolean validarSesion() throws IOException{
       
        boolean msg = false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
        /*    try{
                String usuario = String.valueOf(session.getAttribute("SSUSUARIO" ));
                if(usuario!=null && usuario!="null"){
                    if(usuario.length()>0){
                        System.out.println("usuario-->"+usuario);
                        msg = true;
                         
                    }
                }
            }catch(Exception ex){
                System.out.println("error");
            }*/
            
        //System.out.println("tok: ---->"+token);
       // System.out.println("token: ---->"+sb_token.getToken());
            token = sb_token.getToken();
         if(msg==false && this.token!=null){
            List<GascatUsuario> luser = gascatUsuarioFacade.findByToken(token);
           
                if(!luser.isEmpty()){
                    
                    session.setAttribute("SSUSUARIO",  luser.get(0).getGascatUsuarioPK().getUsername() );  
                    this.setGascatUsuario(luser.get(0));
                    this.setRol(luser.get(0).getCodigoTiporol().getCodigoTiporol());
                    msg = true;
                }
         }
        
         /*
         if(msg==false){
           HttpServletResponse httpResponse = (HttpServletResponse)  FacesContext.getCurrentInstance().getExternalContext().getRequest();
            httpResponse.sendRedirect("login.xhtml");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
         }
         */
         
        return msg;
    }
    
    public boolean tiempoSesion() throws IOException{
       
        boolean msg = false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
            try{
                String usuario = String.valueOf(session.getAttribute("SSUSUARIO" ));
                if(usuario!=null && usuario!="null"){
                    if(usuario.length()>0){
                        System.out.println("usuario-->"+usuario);
                        msg = true;
                         List<GascatUsuario> lusuario = gascatUsuarioFacade.findByUsername(usuario);
                            if(!lusuario.isEmpty()){
                                GascatUsuario gc = lusuario.get(0);
                                gc.setUltimoAcceso(new Date());
                                gc.setConectado(true);
                                gascatUsuarioFacade.edit(gc);
                             
                            } 
                    }
                }
            }catch(Exception ex){
                System.out.println("error");
            }
            
       
       
        
         /*
         if(msg==false){
           HttpServletResponse httpResponse = (HttpServletResponse)  FacesContext.getCurrentInstance().getExternalContext().getRequest();
            httpResponse.sendRedirect("login.xhtml");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
         }
         */
         
        return msg;
    }    
            
     
    public   String generateToken( String username ) {
        Date fecha = new Date();
        String valorFecha = String.valueOf( fecha.getTime());
        double longToken = Math.abs( Math.floor(Math.random() * (99999 - 1)) + 1 );
        String random = Long.toString((long) longToken, 16 );
        
        return (valorFecha  +username +   random );
    }     
   
    public String mas() throws IOException{
    
        FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.xhtml");
          
        return "ok";
    
    }
    
    public String limpiar(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("limpiando ");
        System.out.println("limpiando ");
        System.out.println("limpiando ");
        
        return "ok";
    }
    
    
            
}
