package com.gas;

 

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
 
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
 
import org.primefaces.model.menu.MenuModel;

@Named("gasMenu")
@SessionScoped
public class Menu implements Serializable {
    
    @EJB
    private com.gas.GasMenuRolFacade  gasMenuRol;    
    private MenuModel menus;     
    private List<GasMenuRol> lgasmenu = new ArrayList<GasMenuRol>();
    
    
    public MenuModel generarMenus() {
        MenuModel vmodulos =  new DefaultMenuModel();
        DefaultSubMenu submenux = null;
        DefaultSubMenu submenu =  new DefaultSubMenu("Prepago");
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue("Home");         
        item.setIcon("ui-icon-home");
           
          
        DefaultMenuItem item2 = new DefaultMenuItem();
        item2 = new DefaultMenuItem("Creacion");
        item2.setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
        item2.setIcon("fa fa-bank ");
        item2.setCommand("#{gasEmpresaController.redirect('List')}");
        submenu.addElement(item2);
        
        lgasmenu = gasMenuRol.findByCodRol("ADM");
        
        if(!lgasmenu.isEmpty()){
            
            for (GasMenuRol m  : lgasmenu ){
                System.out.println("meni--"+m.getGasMenu().getNobre());
                if(m.getGasMenu().getPrincipal().intValue()== 1){
                    if(submenux!=null){
                        
                        vmodulos.addElement(submenux);
                    }
                    
                    submenux =  new DefaultSubMenu(m.getGasMenu().getNobre());
                    submenux.setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
                    System.out.println("Principal---"+submenux);
                }else{
                    
                    DefaultMenuItem itemx = new DefaultMenuItem();
                    itemx.setValue(m.getGasMenu().getNobre());   
                    itemx.setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
                    itemx.setIcon(m.getGasMenu().getIcon());
                    itemx.setCommand(m.getGasMenu().getUrl());
                    if(submenux!= null){
                        submenux.addElement(itemx);
                    }
                    
                }    
              
                
            }
            
        }       
         vmodulos.addElement(submenux);
        //vmodulos.addElement(item);
        //vmodulos.addElement(submenu);
        
         
        
        vmodulos.generateUniqueIds();
        return vmodulos;
     }

    public MenuModel getMenus() {
           menus = generarMenus();
        System.out.println("menus---."+menus);
        return menus;
    }

    public void setMenus(MenuModel menus) {
        this.menus = menus;
    }
     
     

}
