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

@Named("gascatBancoController")
@SessionScoped
public class GascatBancoController implements Serializable {

    @EJB
    private com.gas.GascatBancoFacade ejbFacade;
    @EJB
    private NewSessionBean newSessionBean;
    
    private List<GascatBanco> items = null;
    private GascatBanco selected;

    public GascatBancoController() {
    }

    public GascatBanco getSelected() {
        return selected;
    }

    public void setSelected(GascatBanco selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GascatBancoFacade getFacade() {
        return ejbFacade;
    }

    public GascatBanco prepareCreate() {
        selected = new GascatBanco();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GascatBancoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GascatBancoUpdated"));
    }

    public void destroy() {
       newSessionBean.mas();
    }

    public List<GascatBanco> getItems() {
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

    public GascatBanco getGascatBanco(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<GascatBanco> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GascatBanco> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GascatBanco.class)
    public static class GascatBancoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GascatBancoController controller = (GascatBancoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gascatBancoController");
            return controller.getGascatBanco(getKey(value));
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
            if (object instanceof GascatBanco) {
                GascatBanco o = (GascatBanco) object;
                return getStringKey(o.getCodigoBanco());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GascatBanco.class.getName()});
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gascatBanco"+url+".xhtml");
        return "ok";
    }     

}
