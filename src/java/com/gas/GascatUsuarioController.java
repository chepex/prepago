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
import javax.servlet.http.HttpSession;

@Named("gascatUsuarioController")
@SessionScoped
public class GascatUsuarioController implements Serializable {

    @EJB
    private GascatUsuarioFacade ejbFacade;
    private List<GascatUsuario> items = null;
    private GascatUsuario selected;

    public GascatUsuarioController() {
    }

    public GascatUsuario getSelected() {
        return selected;
    }

    public void setSelected(GascatUsuario selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getGascatUsuarioPK().setCodigoEstacion(selected.getGasEstacion().getGasEstacionPK().getCodigoEstacion());
        selected.getGascatUsuarioPK().setCodigoEmpresa(selected.getGasEstacion().getGasEstacionPK().getCodigoEmpresa());
    }

    protected void initializeEmbeddableKey() {
        selected.setGascatUsuarioPK(new com.gas.GascatUsuarioPK());
    }

    private GascatUsuarioFacade getFacade() {
        return ejbFacade;
    }

    public GascatUsuario prepareCreate() {
        selected = new GascatUsuario();
        initializeEmbeddableKey();
        return selected;
    }
    
    public GascatUsuario prepareCreateUsuario() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);  
        GasEstacion ge =  (GasEstacion) session.getAttribute("SSESTACION" );   
        String usuario = String.valueOf(session.getAttribute("SSUSUARIO" ));
          List <GascatUsuario> lu= this.ejbFacade.findByUsuarioEstacion(usuario,  ge);
          if(!lu.isEmpty()){
          selected = lu.get(0);
          }else{
              selected = new GascatUsuario();
          }
        
        initializeEmbeddableKey();
        return selected;
    }    

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GascatUsuarioCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GascatUsuarioUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GascatUsuarioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GascatUsuario> getItems() {
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

    public GascatUsuario getGascatUsuario(com.gas.GascatUsuarioPK id) {
        return getFacade().find(id);
    }

    public List<GascatUsuario> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GascatUsuario> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GascatUsuario.class)
    public static class GascatUsuarioControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GascatUsuarioController controller = (GascatUsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gascatUsuarioController");
            return controller.getGascatUsuario(getKey(value));
        }

        com.gas.GascatUsuarioPK getKey(String value) {
            com.gas.GascatUsuarioPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gas.GascatUsuarioPK();
            key.setCodigoEmpresa(values[0]);
            key.setCodigoEstacion(values[1]);
            key.setUsername(values[2]);
            return key;
        }

        String getStringKey(com.gas.GascatUsuarioPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodigoEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoEstacion());
            sb.append(SEPARATOR);
            sb.append(value.getUsername());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GascatUsuario) {
                GascatUsuario o = (GascatUsuario) object;
                return getStringKey(o.getGascatUsuarioPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GascatUsuario.class.getName()});
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gascatUsuario/"+url+".xhtml");
        return "ok";
    }     

}
