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

@Named("gasEstacionController")
@SessionScoped
public class GasEstacionController implements Serializable {

    @EJB
    private com.gas.GasEstacionFacade ejbFacade;
    @EJB
    private GascatUsuarioFacade gascatUsuarioFacade;    
    private List<GasEstacion> items = null;
    private GasEstacion selected;
    private String nombreEstado;
    
    public GasEstacionController() {
    }

   

   

    public GasEstacion getSelected() {
        
       
        return selected;
    }

    public void setSelected(GasEstacion selected) {
        this.selected = selected;
    }
 
 
 
      

    protected void setEmbeddableKeys() {
        selected.getGasEstacionPK().setCodigoEmpresa(selected.getGasEmpresa().getCodigoEmpresa());
    }

    protected void initializeEmbeddableKey() {
        selected.setGasEstacionPK(new com.gas.GasEstacionPK());
    }

    private GasEstacionFacade getFacade() {
        return ejbFacade;
    }

    public GasEstacion prepareCreate() {
        selected = new GasEstacion();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GasEstacionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GasEstacionUpdated"));
    }

    public void destroy() {
        
        List<GascatUsuario> lusuario = gascatUsuarioFacade.findByCodigoEmpresa(selected);
        
        System.out.println("lusuario--->"+lusuario);
        
        if(lusuario.isEmpty()){
            persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GasEstacionDeleted"));
            if (!JsfUtil.isValidationFailed()) {
                selected = null; // Remove selection
                items = null;    // Invalidate list of items to trigger re-query.
            }
        }else{
          JsfUtil.addErrorMessage("Error", "Ya existen usuarios relacionadas a esta estacion, no se puede eliminar");  
        }
        
        
    }

    public List<GasEstacion> getItems() {
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

    public GasEstacion getGasEstacion(com.gas.GasEstacionPK id) {
        return getFacade().find(id);
    }

    public List<GasEstacion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GasEstacion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GasEstacion.class)
    public static class GasEstacionControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GasEstacionController controller = (GasEstacionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gasEstacionController");
            return controller.getGasEstacion(getKey(value));
        }

        com.gas.GasEstacionPK getKey(String value) {
            com.gas.GasEstacionPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.gas.GasEstacionPK();
            key.setCodigoEmpresa(values[0]);
            key.setCodigoEstacion(values[1]);
            return key;
        }

        String getStringKey(com.gas.GasEstacionPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCodigoEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getCodigoEstacion());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GasEstacion) {
                GasEstacion o = (GasEstacion) object;
                return getStringKey(o.getGasEstacionPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GasEstacion.class.getName()});
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("../gasEstacion/"+url+".xhtml");
        return "ok";
    }     

}
