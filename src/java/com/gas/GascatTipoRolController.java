package com.gas;

import com.gas.util.JsfUtil;
import com.gas.util.JsfUtil.PersistAction;
import java.io.IOException;

import java.io.Serializable;
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

@Named("gascatTipoRolController")
@SessionScoped
public class GascatTipoRolController implements Serializable {

    @EJB
    private com.gas.GascatTipoRolFacade ejbFacade;
    private List<GascatTipoRol> items = null;
    private GascatTipoRol selected;

    public GascatTipoRolController() {
    }

    public GascatTipoRol getSelected() {
        return selected;
    }

    public void setSelected(GascatTipoRol selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GascatTipoRolFacade getFacade() {
        return ejbFacade;
    }

    public GascatTipoRol prepareCreate() {
        selected = new GascatTipoRol();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GascatTipoRolCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GascatTipoRolUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GascatTipoRolDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GascatTipoRol> getItems() {
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

    public GascatTipoRol getGascatTipoRol(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<GascatTipoRol> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GascatTipoRol> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GascatTipoRol.class)
    public static class GascatTipoRolControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GascatTipoRolController controller = (GascatTipoRolController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gascatTipoRolController");
            return controller.getGascatTipoRol(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GascatTipoRol) {
                GascatTipoRol o = (GascatTipoRol) object;
                return getStringKey(o.getCodigoTiporol());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GascatTipoRol.class.getName()});
                return null;
            }
        }

    }
    
    public String redirect(String url) throws IOException{
    
        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        System.out.println("limpiando "+url);
        selected = null;
        items = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gascatTipoRol/"+url+".xhtml");
        return "ok";
    }     

}
