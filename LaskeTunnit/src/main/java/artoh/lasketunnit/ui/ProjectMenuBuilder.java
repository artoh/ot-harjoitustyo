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
 * Muodostaa Projekti-valikon, jonka kautta avataan projekteja ja lisätään
 * uusia projekteja.
 * 
 * LaskeTunnitApplication huoletii StorageUi-käyttöliittymien rekisteröimisestä
 * tämän luokan registerStorageUi()-metodilla. Nämä rekisteröidyt
 * käyttöliitttymät lisätään projektien avaamisen ja luomisen valikoihin.
 * 
 * @author ahyvatti
 */
public class ProjectMenuBuilder {

    private List<StorageUi> storages;
    
    public ProjectMenuBuilder() {
        storages = new ArrayList<>();
    }
    
    /**
     * Rekisteröi StorageUi:n käyttöliittymäkomponenetit
     * 
     * @param storage StorageUi
     */
    public void registerStorageUi(StorageUi storage) {
        storages.add(storage);
    }

    /**
     * Muodostaa Projekti-valikon
     * 
     * Valikossa on toiminnot projektien avaamiseen ja uusien projektien
     * luomiseen. 
     * 
     * @param stage Stage, joka uusien ikkunoiden vanhempana
     * @param mainWindow MainWindow-olio
     * @return 
     */
    public Menu build(Stage stage, MainWindow mainWindow) {
        Menu projectMenu = new Menu("Projekti");
        
        Menu openMenu = new Menu("Avaa ja tuo");
        Menu newMenu = new Menu("Luo uusi");
        
        for (StorageUi ui : storages) {
            if (ui.allowAdd()) {
                openMenu.getItems().add(menuItemAdd(ui, stage, mainWindow));
            }
            if (ui.allowCreate()) {                   
                newMenu.getItems().add(menuItemCreate(ui, stage, mainWindow));
            }
        }
        projectMenu.getItems().add(openMenu);
        projectMenu.getItems().add(newMenu);
        
        return projectMenu;
    }
    
    private MenuItem menuItemAdd(StorageUi ui, Stage stage, MainWindow mainWindow) {
        MenuItem addItem = new MenuItem(ui.storageName());

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ProjectInformation info = ui.addProject(stage);
                if (info != null) {
                    mainWindow.addProject(info);
                }
            }
        };
        
        addItem.setOnAction(event);    
        return addItem;
    }
    
    private Menu menuItemCreate(StorageUi ui, Stage stage, MainWindow mainWindow) {
        MenuItem createItem = new MenuItem(ui.storageName());

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ProjectInformation info = ui.createProject(stage);
                if (info != null) {
                    mainWindow.createProject(info);
                }
            }
        };
        createItem.setOnAction(event);             
        return createItem;
    }
}
