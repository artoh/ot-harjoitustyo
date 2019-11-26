/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.ProjectInformation;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * @author ahyvatti
 */
public class ProjectMenuBuilder {

    private List<StorageUi> storages;
    
    public ProjectMenuBuilder() {
        storages = new ArrayList<>();
    }
    
    public void registerStorageUi(StorageUi storage) {
        storages.add(storage);
    }

    public Menu build(Stage stage, MainWindow mainWindow) {
        Menu projectMenu = new Menu("Projekti");
        
        Menu openMenu = new Menu("Avaa ja tuo");
        Menu newMenu = new Menu("Luo uusi");
        
        for(StorageUi ui : storages) {
            if (ui.allowAdd()) {
                MenuItem addItem = new MenuItem(ui.storageName());
                
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        ProjectInformation info = ui.addProject(stage);
                        if (info.isValid()) {
                            mainWindow.addProject(info);
                        }
                    }
                };
                
                addItem.setOnAction(event);
                openMenu.getItems().add(addItem);
            }
            if( ui.allowCreate()) {
                MenuItem createItem = new MenuItem(ui.storageName());
                
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        ProjectInformation info = ui.createProject(stage);
                        if (info.isValid()) {
                            mainWindow.createProject(info);
                        }
                    }
                };
                createItem.setOnAction(event);                
                newMenu.getItems().add(createItem);
            }
        }
        projectMenu.getItems().add(openMenu);
        projectMenu.getItems().add(newMenu);
        
        return projectMenu;
    }
    
}
