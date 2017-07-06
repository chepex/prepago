package com.gas;

 

import com.gas.util.JsfUtil;
import com.gas.util.JsfUtil.PersistAction;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

@Named("gasPrepagoErroresController")
@SessionScoped
public class GasPrepagoErroresController implements Serializable {

    @EJB
    private  GasPrepagoErroresFacade ejbFacade;
    
    @EJB
    private  GasPrepagoDetalleFacade gasPrepagoDetalleFacade;    
    
     
    private List<GasPrepagoErrores> items = null;
    private GasPrepagoErrores selected;

    public GasPrepagoErroresController() {
    }

    public GasPrepagoErrores getSelected() {
        return selected;
    }

    public void setSelected(GasPrepagoErrores selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GasPrepagoErroresFacade getFacade() {
        return ejbFacade;
    }

    public GasPrepagoErrores prepareCreate() {
        selected = new GasPrepagoErrores();
        initializeEmbeddableKey();
        return selected;
    }
    
   public GasPrepagoErrores prepareCreatePista(   BigInteger vnumPrepago) {
        List<GasPrepagoDetalle > lprepago = this.gasPrepagoDetalleFacade.findByNumero(vnumPrepago);
        
        if (lprepago.size()>0) {
            
            GasPrepagoDetalle dp = lprepago.get(0);        
            selected = new GasPrepagoErrores();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
            GasEstacion ge = (GasEstacion) session.getAttribute("SSESTACION" );
            String vusuario = (String) session.getAttribute("SSUSUARIO" );
            selected.setCodigoEmpresa(ge.getGasEmpresa().getCodigoEmpresa());
            selected.setCodigoEstacion(ge.getGasEstacionPK().getCodigoEstacion());
            selected.setCodigoPrepago(dp.getGasPrepagoDetallePK().getCodigoPrepago());
            selected.setFecha(new Date());
            selected.setNoPrepago(dp.getGasPrepagoDetallePK().getNoPrepago());
            selected.setUsuario(vusuario);
            BigDecimal vid = this.ejbFacade.findByMaxID();
            selected.setIdError(vid);
            
            initializeEmbeddableKey();
            
        }
        return selected;
    }    

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoErroresCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoErroresUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GasPrepagoErroresDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GasPrepagoErrores> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
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

    public GasPrepagoErrores getGasPrepagoErrores(java.math.BigDecimal id) {
        return getFacade().find(id);
    }

    public List<GasPrepagoErrores> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GasPrepagoErrores> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GasPrepagoErrores.class)
    public static class GasPrepagoErroresControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GasPrepagoErroresController controller = (GasPrepagoErroresController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gasPrepagoErroresController");
            return controller.getGasPrepagoErrores(getKey(value));
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GasPrepagoErrores) {
                GasPrepagoErrores o = (GasPrepagoErrores) object;
                return getStringKey(o.getIdError());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GasPrepagoErrores.class.getName()});
                return null;
            }
        }

    }

}
