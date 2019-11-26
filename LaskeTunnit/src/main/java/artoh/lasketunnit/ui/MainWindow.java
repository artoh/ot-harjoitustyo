/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.Task;
import artoh.lasketunnit.service.TasksService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Ohjelman pääikkuna
 * 
 * @author arto
 */
public class MainWindow {
   
    private TasksService service;
    private ProjectMenuBuilder projectMenuBuilder;
    
    private TableView table;
    private ObservableList<Task> data;
    private MenuBar menuBar;
    
    
    public MainWindow(TasksService service, ProjectMenuBuilder projectMenuBuilder) {
        this.service = service;
        this.projectMenuBuilder = projectMenuBuilder;
    }
    
    
    /**
     * Pääikkunan alustaminen
     * 
     * @param window 
     */
    public void init(Stage window) {
        window.setTitle("LaskeTunnit");
        
        initTableView();
        initMenuBar(window);
     
        VBox vbox = new VBox(menuBar, table);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.show();
    }
        
    /**
     * Projektin lisääminen
     * 
     * StorageUi-käyttöliittymä kutsuu tätä funktiota, kun käyttäjä lisää olemassa olevan
     * projektin seurattavaksi. Aiheuttaa käyttöliittymän päivittämisen. MainWindow välittää
     * lisäämisen TasksService-luokalle.
     * 
     * @param information Projektia kuvaava informaatio
     */
    protected void addProject(ProjectInformation information) {
        service.getStorages().addProject(information);
        service.refresh();
        data = FXCollections.observableArrayList(service.allTasks());
        table.setItems(data);
    }

    /**
     * Uuden projektin luominen
     * 
     * StorageUi-käyttöliittymä kutsuu tätä funktiota, kun käyttäjä lisää uuden projektin
     * seurattavaksi. Käyttöliittymää ei päivitetä, koska uudessa projektissa ei vielä ole
     * yhtään tehtävää.
     * 
     * @param information Projektia kuvaava informaatio
     */
    protected void createProject(ProjectInformation information) {
        service.getStorages().createProject(information);
        service.refresh();        
    }

    
    /**
     * Taulukkonäkymän alustaminen
     */
    protected void initTableView() {
        table = new TableView();
        data = FXCollections.observableArrayList(service.allTasks());
        table.setItems(data);
        
        TableColumn projectColumn = new TableColumn("Projekti");
        projectColumn.setCellValueFactory(new PropertyValueFactory("projectName"));
        
        TableColumn dateColumn = new TableColumn("Päivämäärä");
        dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
        
        TableColumn descriptionColumn = new TableColumn("Kuvaus");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        
        TableColumn minutesColumn = new TableColumn("Kesto");
        minutesColumn.setCellValueFactory(new PropertyValueFactory("minutes"));
        
        table.getColumns().setAll(projectColumn, dateColumn, descriptionColumn, minutesColumn);
        table.setPlaceholder(new Label("Ole hyvä ja avaa tuntikirjanpidon Markdown-tiedosto\n" +
                "Valikosta Projekti > Avaa ja tuo > Markdown-tiedosto"));
    }
    
    /**
     * Valikon alustaminen
     * 
     * @param stage 
     */
    protected void initMenuBar(Stage stage) {
        menuBar = new MenuBar();
        menuBar.getMenus().add(projectMenuBuilder.build(stage, this));
    }
    
}
