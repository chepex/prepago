package com.gas;

import com.gas.util.JsfUtil;
import com.gas.util.JsfUtil.PersistAction;
import com.gas.util.mmx;
import com.gasEjb.gasPrepago;
import static com.sun.faces.facelets.tag.jstl.fn.JstlFunction.trim;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.MessagingException; 
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import jxl.read.biff.BiffException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.primefaces.event.FileUploadEvent;


@Named("gasPrepagoController")
@SessionScoped
public class GasPrepagoController implements Serializable {

   
    @EJB
    private com.gas.GasPrepagoFacade ejbFacade;
    @EJB
    private com.gas.PrepagoControlConsumoFacade prepagoControlConsumoFacade;       
    @EJB
    private com.gas.PrepagoDetalleFacade prepagoDetalleFacade;       
    @EJB
    private gasPrepago gasPrepago;    
    @EJB
    private com.gas.GasPrepagoDetalleFacade gasPrepagoDetalleFacade;    
    @EJB
    private com.gasEjb.EmailBeanGmail emailBeanGmail;    
    @EJB
    private mmx mmx;   
    @EJB
    private XLS_read ejbXLS2;       
    @EJB
    private com.gas.GascatUsuarioFacade gascatUsuarioFacade;    
    
    private List<GasPrepago> pendientes = new ArrayList<GasPrepago>();
    
    
      
       
    
    private GasPrepagoDetalle selectedgpd;
    
    private List<GasPrepago> items = null;
    private GasPrepago selected;
    private Cliente cliente;
    private BigInteger numeroPrepago;
    private String estado;    
    private String autorizacion;     
    private List<GascatCuentaBanco> lcuenta;
    private String cuenta;
    private List<PrepagoControlConsumo> lprepagoControlConsumo ;
    private BigInteger vmonto ;
    private BigInteger vcantidad ;
    private List<PrepagoDetalle> lprepagodetalle = new ArrayList<PrepagoDetalle>();
    private Date now;
    private GasSaborCombustible gscombustible;
    private int cantidaImprimir;
    
    
//    private GascatCuentaBanco cb;
    
    
    
    public GasPrepagoController() {
    }

    @PostConstruct
    protected void init(){
 
        this.selected = null;
        pendientes = this.ejbFacade.findByPendiente();

    }    

    public List<GasPrepago> getPendientes() {
        return pendientes;
    }

    public void setPendientes(List<GasPrepago> pendientes) {
        this.pendientes = pendientes;
    }

    
    
    public int getCantidaImprimir() {
        return cantidaImprimir;
    }

    public void setCantidaImprimir(int cantidaImprimir) {
        this.cantidaImprimir = cantidaImprimir;
    }
    

    public GasSaborCombustible getGscombustible() {
        return gscombustible;
    }

    public void setGscombustible(GasSaborCombustible gscombustible) {
        this.gscombustible = gscombustible;
    }
        
        

    public Date getNow() {
         now = new Date();
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

        
        
    public List<PrepagoDetalle> getLprepagodetalle() {
        return lprepagodetalle;
    }

    public void setLprepagodetalle(List<PrepagoDetalle> lprepagodetalle) {
        this.lprepagodetalle = lprepagodetalle;
    }

        
        
    public BigInteger getVmonto() {
        return vmonto;
    }

    public void setVmonto(BigInteger vmonto) {
        this.vmonto = vmonto;
    }

    public BigInteger getVcantidad() {
        return vcantidad;
    }

    public void setVcantidad(BigInteger vcantidad) {
        this.vcantidad = vcantidad;
    }

        
    public List<PrepagoControlConsumo> getLprepagoControlConsumo() {
        return lprepagoControlConsumo;
    }

    public void setLprepagoControlConsumo(List<PrepagoControlConsumo> lprepagoControlConsumo) {
        this.lprepagoControlConsumo = lprepagoControlConsumo;
    }        
        
    public GasPrepagoDetalle getSelectedgpd() {
        return selectedgpd;
    }

    public void setSelectedgpd(GasPrepagoDetalle selectedgpd) {
        this.selectedgpd = selectedgpd;
    }        
        
        
    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }


        
        
    
        
    public List<GascatCuentaBanco> getLcuenta() {
        return lcuenta;
    }

    public void setLcuenta(List<GascatCuentaBanco> lcuenta) {
        this.lcuenta = lcuenta;
    }
    
        
        
    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }    
    
    public BigInteger getNumeroPrepago() {
        return numeroPrepago;
    }

    public void setNumeroPrepago(BigInteger numeroPrepago) {
        this.numeroPrepago = numeroPrepago;
    }
        
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public GasPrepago getSelected() {
        return selected;
    }
    
    public void setSelected(GasPrepago selected) {
        this.selected = selected;
    }
    
    protected void setEmbeddableKeys() {
        /*selected.getGasPrepagoPK().setCodigoEstacion(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion());
        selected.getGasPrepagoPK().setCodigoEmpresa(selected.getGasEstacion().getGasEstacionPK().getCodigoEmpresa());*/
    }
    
    protected void initializeEmbeddableKey() {
        selected.setGasPrepagoPK(new com.gas.GasPrepagoPK());
     //   selected.getGasPrepagoPK().setCodigoEmpresa(cliente.gasClientePK.getCodigoEmpresa());
     //   selected.getGasPrepagoPK().setCodigoEstacion(cliente.getGasClientePK().getCodigoEstacion());
    }
    
    private GasPrepagoFacade getFacade() {
        return ejbFacade;
    }
    
    public GasPrepago prepareCreate() {
        selected = new GasPrepago();      
        BigDecimal rangoI = this.ejbFacade.findByMaxRango();
        selected.setNumeroInicial(rangoI.toBigInteger());
        lcuenta = null;
        initializeEmbeddableKey();
        selected.setCliente(cliente);
        lprepagodetalle = null;
        this.vcantidad= null;
        this.vmonto = null;
        return selected;
    }
    
    
    public void createEspecial() throws MessagingException {
           
            BigDecimal codigoPrepago = this.ejbFacade.findByMaxID();
            selected.getGasPrepagoPK().setCodigoEmpresa(selected.getGasEstacion().getGasEstacionPK().getCodigoEmpresa());
            selected.getGasPrepagoPK().setCodigoEstacion(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion());
            selected.getGasPrepagoPK().setCodigoPrepago(codigoPrepago.toBigInteger());
            //selected.setCodigoCliente(this.selected.getGasCliente().getGasClientePK().getCodigoCliente());
            selected.setIdCliente(cliente.getIdCliente());
            selected.setNoRegistro(selected.getCliente().getNoRegistro());
            selected.setCliente(cliente);
            selected.setEstado("A");
            selected.setImpreso("N");
            if(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion().equals("0000")){
                selected.setTodas(true);
            }
            //selected.setValorDePrepago(selected.getMontoPrepagoUsd().divide(selected.getTotalPrepagos()));
            //selected.setSaldoPrepagoUsd(selected.getMontoPrepagoUsd());
            selected.setFechaCreacion(new Date());
            selected.setCodigoBanco(this.selected.getGascatBanco().getCodigoBanco());
            selected.setCodigoCuenta( cuenta);
            //selected.setMontoPrepagoUsd(numeroPrepago);            
            String msg = this.gasPrepago.generarPrepagoEspecial(selected , this.lprepagodetalle);          
            if(msg.equals("OK")){
              for(PrepagoDetalle p: lprepagodetalle)  {
                  p.setIdDetalle(new BigDecimal(prepagoDetalleFacade.count()+1));
                  p.setCodigoPrepago(codigoPrepago.toBigInteger());
                  this.prepagoDetalleFacade.edit(p);
              }
              enviarCorreo();
            
            }       
             
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        
        consultar();
    }
    
    
    public void create() throws MessagingException {
           
            BigDecimal codigoPrepago = this.ejbFacade.findByMaxID();
            selected.getGasPrepagoPK().setCodigoEmpresa(selected.getGasEstacion().getGasEstacionPK().getCodigoEmpresa());
            selected.getGasPrepagoPK().setCodigoEstacion(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion());
            selected.getGasPrepagoPK().setCodigoPrepago(codigoPrepago.toBigInteger());
            //selected.setCodigoCliente(this.selected.getGasCliente().getGasClientePK().getCodigoCliente());
            selected.setIdCliente(cliente.getIdCliente());
            selected.setNoRegistro(selected.getCliente().getNoRegistro());
            selected.setCliente(cliente);
            selected.setEstado("A");
            selected.setImpreso("N");
            selected.setValorDePrepago(selected.getMontoPrepagoUsd().divide(selected.getTotalPrepagos()));
            selected.setSaldoPrepagoUsd(selected.getMontoPrepagoUsd());
            selected.setFechaCreacion(new Date());
            selected.setCodigoBanco(this.selected.getGascatBanco().getCodigoBanco());
            selected.setCodigoCuenta( cuenta);  
            if(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion().equals("0000")){
                selected.setTodas(true);
            }
            String msg = this.gasPrepago.generarPrepago(selected, this.gscombustible);
            
            if(msg.equals("OK")){
            enviarCorreo();
            //  gasPrepago.enviarEncabezado(selected);
                JsfUtil.addSuccessMessage("Prepago creado correctamente");
            }else{
               JsfUtil.addErrorMessage("Error", "Surgio un error al generar el prepago");
            }
             
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        
        consultar();
    }
    
    
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoUpdated"));
    }
    
    public void destroy() {
        try{
            List<GasPrepagoDetalle > lprepago  = gasPrepagoDetalleFacade.findByCodigoPrepagoUtilizado(selected);      
            if(lprepago.isEmpty()){
                persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoDeleted"));
                if (!JsfUtil.isValidationFailed()) {
                    selected = null; // Remove selection
                    items = null;    // Invalidate list of items to trigger re-query.
                }
            }else{
                JsfUtil.addErrorMessage("Error", "Ya existen prepagos utilizados en este correlativo");
            }
            
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Error", "Este gasPrepago->Create : "+ex.getMessage());
        }
        
    }
        
    public List<GasPrepago> getItems() {
    
        return items;
    }
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public GasPrepago getGasPrepago(com.gas.GasPrepagoPK id) {
        return getFacade().find(id);
    }
    
    public List<GasPrepago> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }
    
    public List<GasPrepago> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    @FacesConverter(forClass = GasPrepago.class)
    public static class GasPrepagoControllerConverter implements Converter {
        
        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GasPrepagoController controller = (GasPrepagoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gasPrepagoController");
            return controller.getGasPrepago(getKey(value));
        }
        
        com.gas.GasPrepagoPK getKey(String value) {
            com.gas.GasPrepagoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gas.GasPrepagoPK();
            key.setCodigoEmpresa(values[0]);
            key.setCodigoEstacion(values[1]);
            key.setCodigoPrepago(new BigInteger(values[2]));
            return key;
        }
        
        String getStringKey(com.gas.GasPrepagoPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodigoEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoEstacion());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoPrepago());
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GasPrepago) {
                GasPrepago o = (GasPrepago) object;
                return getStringKey(o.getGasPrepagoPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GasPrepago.class.getName()});
                return null;
            }
        }
        
    }
    
    public void actualizaNumeroFinal() {
        BigInteger num = new BigInteger("0");
        if (selected != null) {
            num = selected.getNumeroInicial().add(this.selected.getTotalPrepagos());
            selected.setNumeroFinal(num);
        }
      
        
    }

    public void consulta2() {
        this.autorizacion = null;
        
       List<GasPrepagoDetalle > lprepago = this.gasPrepagoDetalleFacade.findByNumero(numeroPrepago);
        if (lprepago.size()>0) {
              selected = lprepago.get(0).getGasPrepago();
              estado = gasPrepago.generarAutorizacion(numeroPrepago); 
        }else{
            selected =null;
            JsfUtil.addErrorMessage("Error", "no se encontraron datos");
        }
        
        lprepago.clear();
    }    
    
    public void consultaBloqueados() {
        
        this.autorizacion = null;        
        List<GasPrepagoDetalle > lprepago = this.gasPrepagoDetalleFacade.findByNumeroBloqueados(numeroPrepago, "B" );
        if (lprepago.size()>0) {
            
            
            selected = lprepago.get(0).getGasPrepago();
            estado = gasPrepago.generarAutorizacion(numeroPrepago); 
            lprepagoControlConsumo = this.prepagoControlConsumoFacade.findByPK(lprepago.get(0));
            
              
        }else{
            selected =null;
            JsfUtil.addErrorMessage("Error", "no se encontraron datos");
        }
        
        lprepago.clear();
        
    } 
    
    public void consultar() {
     
        items = this.ejbFacade.findByCodigoCliente(this.cliente);
        if (items.isEmpty()) {
            JsfUtil.addErrorMessage("error", "no se encontraron datos");
        }
        
    }    
    
    public void consultaAuorizacion() {
     
        pendientes = this.ejbFacade.findByPendiente();
        if(pendientes.isEmpty()){
            JsfUtil.addErrorMessage("error", "no se encontraron datos");
        }     
        
        
    }     


    public void consultarEspecial() {
        System.out.println("cliente-->"+cliente);
        this.prepareCreate();
        
    }     
    
    public void consumir() {
        
        /*validar fecha */
        
        /*validar estacion*/
        
     
      
            List<GasPrepagoDetalle> lgpd  = gasPrepagoDetalleFacade.findByNumero(numeroPrepago);
            if(lgpd.size()>0){
                
                
                
                
               GasPrepagoDetalle gpd =  lgpd.get(0);   
               
               
               if (!gpd.getEstado().equals("P") ){
                 JsfUtil.addErrorMessage("error", "PREPAGO BLOQUEADO LLAME AL ADMINISTRADOR");
                   
               }else{
                   
                    if(gpd.getGasPrepago().isTodas()){
                        consume(   gpd );                    
                    }else{
                        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);         
                        GasEstacion ge =  (GasEstacion) session.getAttribute("SSESTACION" );
                        if(gpd.getGasPrepago().getGasEstacion().equals(ge)){
                            consume(   gpd );                    
                        }else{
                             JsfUtil.addErrorMessage("error", "Este prepago no puede ser utilizado en esta estacion");
                        }
                        
                    }
                      

               }

            }else{
                JsfUtil.addErrorMessage("error", "No se encontraron registros ");
            }
    
                
    }      
    
    
    public void consume( GasPrepagoDetalle gpd ){
     HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
          List<GasPrepago> lp =  this.ejbFacade.findByVigencia(gpd.getGasPrepagoDetallePK().getCodigoPrepago());        
                        if(lp.isEmpty()){
                            JsfUtil.addErrorMessage("Error", "Prepago vencido");            
                        }else{
                            if(gpd.getAutorizacion().equals(this.autorizacion)){
                               gpd.setEstado("C");
                               gpd.setFechaUso(new Date());
                                GasEstacion ge = (GasEstacion) session.getAttribute("SSESTACION" );
                               
                                       
                               String vmas = gasPrepago.enviarAutorizacion(gpd, ge );

                                    if(vmas.equals("OK")){

                                         gasPrepagoDetalleFacade.edit(gpd);
                                         this.estado= "Utilizado";
                                         JsfUtil.addSuccessMessage("Proceso realizado correctamente");  

                                    }else{

                                         JsfUtil.addErrorMessage("error", "Surgio un error al enviar la autorizacion a la estacion");
                                    }

                            }else{
                                
                                String usuario = String.valueOf(session.getAttribute("SSUSUARIO" ));
                                String msg = gasPrepago.conteoAutorizacion(  gpd, usuario,   autorizacion); 
                                if(!msg.equals("OK")){
                                    JsfUtil.addErrorMessage("error", msg);
                                    this.estado="Bloqueado";
                                }
                                
                                JsfUtil.addErrorMessage("error", "Autorizacion no valida ");
                            }           
                        }
    }
    
    
    public void anular() {
        
            System.out.println("---->anulando");
            String msg = gasPrepago.anular(selected);
            if(msg.equals("OK")){
              
                JsfUtil.addSuccessMessage("Se anularos todos los prepagos pendientes");
            }
            else{
                JsfUtil.addErrorMessage("Error","No fue posible realizar la anulacion");
            }
            
    }  
    
    public void autorizar() {
            String msg = gasPrepago.generarAutorizacion(selected);
            if(msg.equals("OK")){
                
                  msg = gasPrepago.enviarEncabezado(selected);
                  
                  /*System.out.println("Mensaje envio encabezado---------->"+msg);
                  if(msg.equals("OK")){*/
                        selected.setAutorizacion("OK");               
                        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                        selected.setFechaAutorizado(new Date(fecha) );
                        String hora = new SimpleDateFormat(" hh:mm:ss").format(new Date());          
                        selected.setHoraAutorizado(trim(hora));
                        ejbFacade.edit(selected);
                        JsfUtil.addSuccessMessage("Se ha autorizado el prepago Numero: "+selected.getGasPrepagoPK().getCodigoPrepago() );
                  /*}else{
                    JsfUtil.addErrorMessage("Error","No se tuvo coneccion con 1 o mas estaciones, favor probar mas tarde");
                  }*/
                  
            }
            else{
                JsfUtil.addErrorMessage("Error","No fue posible realizar la autorizacion");
            }
            
    }    
    
    public String redirect(String url) throws IOException{
    
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        selected = null;
        items = null;
        cliente = null;
        numeroPrepago = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gasPrepago/"+url+".xhtml");
        return "OK";
    }
    

    public void cambiarCuenta(){        
        System.out.println("Cambiando cuenta--->");
        System.out.println("Cambiando cuenta--->");
        if(selected!=null){
            this.lcuenta = this.selected.getGascatBanco().getGascatCuentaBancoList();
        }
        
        System.out.println("Cambiando cuenta--->");
    }
    
    
    public void desbloquear(){    
        
             List<GasPrepagoDetalle> lgpd  = gasPrepagoDetalleFacade.findByNumero(numeroPrepago);
            if(lgpd.size()>0){                
                GasPrepagoDetalle gpd =  lgpd.get(0);   
                gpd.setEstado("P"); 
                this.gasPrepagoDetalleFacade.edit(gpd);                    
                JsfUtil.addSuccessMessage("Proceso generado correctamente");                
                consulta2();
            }
            
        
       
        
         
    }
  
    public String generarReporte2() throws SQLException, NamingException, PrinterException  {
   
        
            if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/prepago2.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                m.put("VCODIGO",codigo  ); 
                reporte(ruta, m);
              
            }
            
            
                selected.setImpreso("S");
                this.ejbFacade.edit(selected);
            
        return "Ok";
    }    
    
    
    public String cartaEntrega() throws SQLException, NamingException, PrinterException  {
   
        
            if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/cartaEntrega.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                 
                m.put("VCODIGO",codigo  ); 
               
                reporte(ruta, m);
            }
            
      
            
        return "Ok";
    }    
    
public String cartaCompromiso() throws SQLException, NamingException, PrinterException  {
   
        
            if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/cartaCompromiso.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                 
                m.put("VCODIGO",codigo  ); 
               
                reporte(ruta, m);
            }
            
      
            
        return "Ok";
    }


    public String generarReporte() throws SQLException, NamingException, PrinterException  {
   
        
            if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/reportePrepago.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                BigDecimal cant = new BigDecimal("100000"); 
                m.put("VCODIGO",codigo  ); 
                m.put("VCANT",cant  ); 
                reporte(ruta, m);
                
                selected.setImpreso("S");
                this.ejbFacade.edit(selected);
            }
            
            
            
      
            
        return "Ok";
    }
    
    public String generarReporteASinVigencia() throws SQLException, NamingException, PrinterException  {
   
        
            if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/reportePrepagoSinFecha.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                 BigDecimal cant = new BigDecimal("100000"); 
                m.put("VCODIGO",codigo  ); 
                m.put("VCANT",cant  ); 
                reporte(ruta, m);
                
                selected.setImpreso("S");
                this.ejbFacade.edit(selected);
            }
            
      
            
        return "Ok";
    }    
    
    
    public String addDetalle (){
    
        if(selected!=null){
            if ( lprepagodetalle == null){
              lprepagodetalle =   new ArrayList<PrepagoDetalle>();
            }
            PrepagoDetalle  pd =    new PrepagoDetalle();
            pd.setIdDetalle(new BigDecimal(lprepagodetalle.size()+1));
            pd.setCodigoPrepago( new BigInteger("0"));
            pd.setCantidad(vcantidad);
            pd.setValor(vmonto);
            pd.setIdSabor(gscombustible);
            
            lprepagodetalle.add(pd);
            
            
        }
        return "OK";
    }
    
    
    public void enviarCorreo() throws MessagingException{
        List<GascatUsuario> lgu = gascatUsuarioFacade.findByRol("GG");
        String correo  = "";
        if(!lgu.isEmpty()){
            GascatUsuario usuario= lgu.get(0);
            correo = usuario.getCorreo();
        }
        
        if(correo.isEmpty() || correo ==null){
             correo = "mmixco7@gmail.com";
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoCreated"));

        String cuerpo = "";
        cuerpo = "<span>Se ha creado una serie de prepago y requiere de tu aprobacion</span><br/>";
        cuerpo += "<table>"
                + "<tr><td><b>Cliente</b></td><td>     "+selected.getCliente().getNombres()+"</td></tr>"
                + "<tr><td><b>Total</b></td><td>     $"+selected.getMontoPrepagoUsd()+"</td></tr>"
                + "<tr><td><b>Cantidad</b></td><td>     "+selected.getTotalPrepagos()+"</td></tr>"
                + "<tr><td><b>Valor</b></td><td>     $"+selected.getValorDePrepago()+"</td></tr>"
                + "</table>";
        cuerpo += "<table>"
                + "<tr><td></td><td><b>Remesa</b></td></tr>"
                + "<tr><td><b>Banco</b></td><td>     "+selected.getGascatBanco().getBanco() +"</td></tr>"
                + "<tr><td><b>Cuenta</b></td><td>     "+cuenta+"</td></tr>"
                + "<tr><td><b>Referencia</b></td><td>     "+selected.getNumRemesa()+"</td></tr>"
                + "<tr><td><b>Valor</b></td><td>     $"+selected.getValor()+"</td></tr>"
                + "</table>";          
        
        emailBeanGmail.enviarTmp2(correo, "Nuevo prepago creado  Cliente:"+selected.getCliente().getNombres()  , cuerpo) ;
        
        emailBeanGmail.enviarTmp2("mmixco7@gmail.com", "Nuevo prepago creado  Cliente:"+selected.getCliente().getNombres()  , cuerpo) ;
    
    }
    
    
       
 public String reporte(String rep, HashMap params)  {
         
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
 
    public String mas (String rep,HashMap params,int nro) throws SQLException, NamingException, PrinterException{
               try{
            String reporPath = "";
            reporPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(rep);
            Context ctx= new InitialContext();
	    Connection conn = null;
            DataSource ds;
            //Desarrollo
            //ds = (DataSource)ctx.lookup("jdbc/desa");
            //Produccion 
            ds = (DataSource)ctx.lookup("jdbc/sicgas");
	    conn = ds.getConnection();

	    String dir= "DIRECCION REPORTE";

	    JasperReport reporteJasper = JasperCompileManager.compileReport(dir);

	    Map parametro = new HashMap();

	    parametro.put("@nro", nro);

	    JasperPrint mostrarReporte = JasperFillManager.fillReport(reporteJasper,parametro, conn);
 
	// ESTABLECE DATOS DE IMPRESORAS
 
	    PrinterJob job = PrinterJob.getPrinterJob();
	    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
	    int selectedService = 0;
	    for(int i = 0; i < services.length;i++){
                if(services[i].getName().toUpperCase().contains("FX-890")){
                    selectedService = i;
                }

 

	    }

 

	      job.setPrintService(services[selectedService]);

	      PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

	      MediaSizeName mediaSizeName = MediaSize.findMedia(4,4,MediaPrintableArea.INCH);

	      printRequestAttributeSet.add(mediaSizeName);

	      printRequestAttributeSet.add(new Copies(1));

	      JRPrintServiceExporter exporter;

	      exporter = new JRPrintServiceExporter();

	      exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);

	    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());

	    exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);

	    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);

	    exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, mostrarReporte);

	    exporter.exportReport();

    }catch(JRException ex){

        JOptionPane.showMessageDialog(null, "Error de JREEXEPCION: " + ex);

 

    } catch (PrinterException ex) {

        JOptionPane.showMessageDialog(null,"ERROR PRINTEREXCEPCION " + ex);

    }

            return "ok";
    }
    
    public void onload() throws SQLException, NamingException, PrinterException{
        
        
        if(cantidaImprimir ==0){
            cantidaImprimir = 10;
        }
       if (selected!=null){
                HashMap m = new HashMap();
                String ruta = "/reportes/reportePrepago.jasper";
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                System.out.println("----->--->1");
                BigDecimal codigo = new BigDecimal(selected.getGasPrepagoPK().getCodigoPrepago()); 
                m.put("VCODIGO",codigo  ); 
                m.put("VCANT",new BigDecimal(cantidaImprimir )); 
                reporte(ruta, m);
            }
        System.out.println("aqui--->");
        System.out.println("aqui--->");
        System.out.println("aqui--->");
        System.out.println("aqui--->");
        System.out.println("aqui--->");
        System.out.println("aqui--->");
    }
    
    public void actualizarImpresos(){
         System.out.println("Actualizar Impresos");   
         System.out.println("Actualizar Impresos");   
         System.out.println("Actualizar Impresos");   
    
    }
    
    
  public void upload(FileUploadEvent event) throws IOException, BiffException  {      
     
        ReadXls a = new ReadXls();
        String destination = "/opt/lib/"+event.getFile().getFileName();
        a.copyFile(event.getFile().getFileName(), event.getFile().getInputstream()); 
        String mensaje ="";        
             
            System.out.println("aqui1");
            mensaje=ejbXLS2.read(destination);  
     
        JsfUtil.addSuccessMessage( mensaje);      
    }      
    
}
