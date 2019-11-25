/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.TasksService;

/**
 *
 * @author arto
 */
public class MainWindow {
   
    private TasksService service;
    private TableView table;
    private ObservableList data;
    
    
    public MainWindow(TasksService service) {
        this.service = service;
    }
    
    
    
    public void init(Stage window) {
        table = new TableWindow();
        data = new FXCollections.observableList(service.allTasks());
        table.setItems(data);
        
        TableColumn projectColumn = new TableColumn("Projekti");
        projectColumn.setCellValueFactory(new PropertyValueFactory("project"));
        
        TableColumn dateColumn = new TableColumn("Päivämäärä");
        dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
        
        TableColumn descriptionColumn = new TableColumn("Kuvaus");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        
        TableColumn minutesColumn = new TableColumn("Kesto");
        minutesColumn.setCellValueFactory(new PropertyValueFactory("minutes"));
        
        table.getColumns().setAll(projectColumn, dateColumn, descriptionColumn, minutesColumn);
     
        
    }
    
}
