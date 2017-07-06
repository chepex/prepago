package com.gas;

import com.gas.util.JsfUtil;
import com.gas.util.JsfUtil.PersistAction;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

@Named("gasPrepagoDetalleController")
@SessionScoped
public class GasPrepagoDetalleController implements Serializable {

    @EJB
    private com.gas.GasPrepagoDetalleFacade ejbFacade;
    private List<GasPrepagoDetalle> items = null;
    private GasPrepagoDetalle selected;
    private Date finicio;
    private Date ffin;    

    public GasPrepagoDetalleController() {
    }

    public Date getFinicio() {
        return finicio;
    }

    public void setFinicio(Date finicio) {
        this.finicio = finicio;
    }

    public Date getFfin() {
        return ffin;
    }

    public void setFfin(Date ffin) {
        this.ffin = ffin;
    }

    
    
    public GasPrepagoDetalle getSelected() {
        return selected;
    }

    public void setSelected(GasPrepagoDetalle selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getGasPrepagoDetallePK().setCodigoPrepago(selected.getGasPrepago().getGasPrepagoPK().getCodigoPrepago());
        selected.getGasPrepagoDetallePK().setCodigoEmpresa(selected.getGasPrepago().getGasPrepagoPK().getCodigoEmpresa());
        selected.getGasPrepagoDetallePK().setCodigoEstacion(selected.getGasPrepago().getGasPrepagoPK().getCodigoEstacion());
    }

    protected void initializeEmbeddableKey() {
        selected.setGasPrepagoDetallePK(new com.gas.GasPrepagoDetallePK());
    }

    private GasPrepagoDetalleFacade getFacade() {
        return ejbFacade;
    }

    public GasPrepagoDetalle prepareCreate() {
        selected = new GasPrepagoDetalle();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoDetalleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoDetalleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoDetalleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GasPrepagoDetalle> getItems() {
       /* if (items == null) {
            items = getFacade().findAll();
        }*/
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

    public GasPrepagoDetalle getGasPrepagoDetalle(com.gas.GasPrepagoDetallePK id) {
        return getFacade().find(id);
    }

    public List<GasPrepagoDetalle> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GasPrepagoDetalle> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GasPrepagoDetalle.class)
    public static class GasPrepagoDetalleControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GasPrepagoDetalleController controller = (GasPrepagoDetalleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gasPrepagoDetalleController");
            return controller.getGasPrepagoDetalle(getKey(value));
        }

        com.gas.GasPrepagoDetallePK getKey(String value) {
            com.gas.GasPrepagoDetallePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gas.GasPrepagoDetallePK();
            key.setCodigoEmpresa(values[0]);
            key.setCodigoEstacion(values[1]);
            key.setCodigoPrepago(new BigInteger(values[2]));
            key.setNoPrepago(new BigInteger(values[3]));
            return key;
        }

        String getStringKey(com.gas.GasPrepagoDetallePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodigoEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoEstacion());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoPrepago());
            sb.append(SEPARATOR);
            sb.append(value.getNoPrepago());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GasPrepagoDetalle) {
                GasPrepagoDetalle o = (GasPrepagoDetalle) object;
                return getStringKey(o.getGasPrepagoDetallePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GasPrepagoDetalle.class.getName()});
                return null;
            }
        }
        
        

    }
    
    public void vestado(){
        
        System.out.println("selected--->"+selected.getEstado()+"<-------");
        System.out.println("<----->");
        System.out.println("<----->");
        System.out.println("<----->");
        System.out.println("<----->");
    
    }
            
            
    
  
    public void consultar() {
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);         
        GasEstacion ge =  (GasEstacion) session.getAttribute("SSESTACION" );        
        Calendar cal = Calendar.getInstance();
        cal.setTime(ffin);
        cal.add(Calendar.DATE, 5);
        ffin = cal.getTime();
                 
        items = this.ejbFacade.findByFechaEstacion(finicio, ffin , ge);
        if (items.isEmpty()) {
            JsfUtil.addErrorMessage("error", "no se encontraron datos");
        }
        
    }     
    
    
   public String redirect(String url) throws IOException{
    
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        selected = null;
        items = null;
       
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gasPrepagoDetalle/"+url+".xhtml");
        return "ok";
    }    

}
