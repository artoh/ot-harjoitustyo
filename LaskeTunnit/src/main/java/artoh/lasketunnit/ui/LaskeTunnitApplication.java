/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.md.storage.MdStorage;
import artoh.lasketunnit.projectlist.FileProjectList;
import artoh.lasketunnit.projectlist.ProjectList;
import artoh.lasketunnit.service.TasksService;
import artoh.lasketunnit.storage.Storages;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author arto
 */
public class LaskeTunnitApplication extends Application {
    
    Storages storages;
    TasksService service;
    
    @Override
    public void start(Stage window) {
        
        ProjectList projectList = new FileProjectList("lasketunnit.ini");
        storages = new Storages(projectList);
        registerComponents();
        
        service = new TasksService(storages);
        service.refresh();
        
        MainWindow mainWindow = new MainWindow(service);
        mainWindow.init(window);        

    }
    
    protected void registerComponents() {
        storages.registerStorage(new MdStorage());
        
    }
    
    
    public static void main(String[] args) {        
               
        launch(LaskeTunnitApplication.class);
    }
}
