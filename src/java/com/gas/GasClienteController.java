package com.gas;

import com.gas.util.JsfUtil;
import com.gas.util.JsfUtil.PersistAction;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigInteger;
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

@Named("gasClienteController")
@SessionScoped
public class GasClienteController implements Serializable {

    @EJB
    private com.gas.GasClienteFacade ejbFacade;
    private List<GasCliente> items = null;
    private GasCliente selected;

    public GasClienteController() {
    }

    public GasCliente getSelected() {
        return selected;
    }

    public void setSelected(GasCliente selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
        selected.setGasClientePK(new com.gas.GasClientePK());
    }

    private GasClienteFacade getFacade() {
        return ejbFacade;
    }

    public GasCliente prepareCreate() {
        selected = new GasCliente();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        System.out.println("aqui222-->");
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GasClienteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GasClienteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GasClienteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GasCliente> getItems() {
     /*   if (items == null) {
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

    public GasCliente getGasCliente(com.gas.GasClientePK id) {
        return getFacade().find(id);
    }

    public List<GasCliente> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GasCliente> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GasCliente.class)
    public static class GasClienteControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GasClienteController controller = (GasClienteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gasClienteController");
            return controller.getGasCliente(getKey(value));
        }

        com.gas.GasClientePK getKey(String value) {
            com.gas.GasClientePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gas.GasClientePK();
            key.setCodigoEmpresa(values[0]);
            key.setCodigoEstacion(values[1]);
            key.setCodigoCliente(new BigInteger(values[2]));
            return key;
        }

        String getStringKey(com.gas.GasClientePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodigoEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoEstacion());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoCliente());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GasCliente) {
                GasCliente o = (GasCliente) object;
                return getStringKey(o.getGasClientePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GasCliente.class.getName()});
                return null;
            }
        }

    }

    public List<GasCliente> completeText(String query) {

        query = query.replaceAll("^\\s+", "");
        if (query != null) {
            query = query.toUpperCase();
        }
        System.out.println("--->" + query);
        List<GasCliente> lest = ejbFacade.findByActivos(query);

        return lest;
    }
    
        public String redirect(String url) throws IOException{
    
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        selected = null;
        items = null;
         
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gasCliente/"+url+".xhtml");
        return "ok";
    }

}
