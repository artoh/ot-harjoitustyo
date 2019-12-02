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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
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
    private MenuItem addTaskMenuItem;
    private MenuItem editTaskMenuItem;
    
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
        
        initMenuBar(window);
        initTableView();
     
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);
        pane.setCenter(table);
        
        Scene scene = new Scene(pane);
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
        refresh();
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
    
    protected void refresh() {
        service.refresh();
        data = FXCollections.observableArrayList(service.allTasks());
        table.setItems(data);
        
        if (service.allProjects().isEmpty()) {
            addTaskMenuItem.setDisable(true);
            table.setPlaceholder(new Label("Ole hyvä ja avaa tai luo uusi \n "+
                "tuntikirjanpito Projekti-valikosta"));
        } else {
            addTaskMenuItem.setDisable(false);
            table.setPlaceholder(new Label("Ole hyvä ja lisää ensimmäinen tehtävä\n" +
                "painamalla Ctrl+N"));            
        }
        
    }

    
    /**
     * Taulukkonäkymän alustaminen
     */
    protected void initTableView() {
        table = new TableView();
        refresh();
        
        TableColumn projectColumn = new TableColumn("Projekti");
        projectColumn.setCellValueFactory(new PropertyValueFactory("projectName"));
        
        TableColumn dateColumn = new TableColumn("Päivämäärä");
        dateColumn.setCellValueFactory(new PropertyValueFactory("dateString"));
        
        TableColumn descriptionColumn = new TableColumn("Kuvaus");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        
        TableColumn minutesColumn = new TableColumn("Kesto");
        minutesColumn.setCellValueFactory(new PropertyValueFactory("hourString"));
        
        table.getColumns().setAll(projectColumn, dateColumn, descriptionColumn, minutesColumn);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                
        ObservableList selectedItems = table.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                editTaskMenuItem.setDisable(selectedItems.isEmpty());
            }                    
        });
    }
    
    /**
     * Valikon alustaminen
     * 
     * @param stage 
     */
    protected void initMenuBar(Stage stage) {
        menuBar = new MenuBar();
        menuBar.getMenus().add(projectMenuBuilder.build(stage, this));
        menuBar.getMenus().add(createTaskMenu());
    }
    
   protected Menu createTaskMenu() {
       Menu taskMenu = new Menu("Tehtävä");
       addTaskMenuItem = new MenuItem("Lisää...");
       
        EventHandler<ActionEvent> addEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (TaskDialog.newTaskDialog(service)) {
                    refresh();
                }
            }
        };
        addTaskMenuItem.setOnAction(addEvent);
        addTaskMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        taskMenu.getItems().add(addTaskMenuItem);
        
        editTaskMenuItem = new MenuItem("Muokkaa...");
        EventHandler<ActionEvent> editEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Task selectedTask = (Task) table.getSelectionModel().getSelectedItem();
                if (selectedTask != null && TaskDialog.editTaskDialog(service, selectedTask) ) {
                    refresh();
                }
            }
        };
        editTaskMenuItem.setOnAction(editEvent);
        editTaskMenuItem.setDisable(true);
        taskMenu.getItems().add(editTaskMenuItem);        
        
        
        return taskMenu;
   }
    
}
