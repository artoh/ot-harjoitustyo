/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.ProjectInformation;
import javafx.stage.Stage;

/**
 *
 * @author ahyvatti
 */
public interface StorageUi {

    public ProjectInformation createProject(Stage stage);

    public ProjectInformation addProject(Stage stage);

    public String storageName();
    
    public boolean allowCreate();
    
    public boolean allowAdd();
    
}
