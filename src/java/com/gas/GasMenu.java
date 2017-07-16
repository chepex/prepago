package com.gas;

 

import java.io.Serializable;
 
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
 
import org.primefaces.model.menu.MenuModel;

@Named("gasMenu")
@SessionScoped
public class GasMenu implements Serializable {
     private MenuModel menus;
     
     public MenuModel generarMenus() {
        MenuModel vmodulos =  new DefaultMenuModel();
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
                    
        vmodulos.addElement(item);
        vmodulos.addElement(submenu);
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
