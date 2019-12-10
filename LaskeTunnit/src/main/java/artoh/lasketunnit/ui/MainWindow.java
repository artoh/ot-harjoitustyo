package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.Task;
import artoh.lasketunnit.service.TasksService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Ohjelman pääikkuna
 *
 * Pääikkunassa on kaksi vaihtoehtoista taulukkonäkymää (tehtävät ja projektit)
 * sekä valikot ohjelman toiminnoille.
 *
 * @author arto
 */
public class MainWindow {

    private TasksService service;
    private ProjectMenuBuilder projectMenuBuilder;

    private TableView taskTable;
    private TableView projectTable;
    private ObservableList<Task> taskData;
    private ObservableList<Project> projectData;

    private MenuBar menuBar;
    private MenuItem hideProjectMenuItem;
    private MenuItem deleteProjectMenuItem;
    private MenuItem addTaskMenuItem;
    private MenuItem editTaskMenuItem;
    private CheckMenuItem viewTasksMenuItem;
    private CheckMenuItem viewProjectsMenuItem;
    private CheckMenuItem viewTasksOfProjectMenuItem;

    private Project selectedProject;
    private Task selectedTask;

    private BorderPane mainPane;

    private enum ViewMode {
        TASKS,
        PROJECTS,
        TASKSOFPROJECT
    }

    private ViewMode currentViewMode = ViewMode.TASKS;

    /**
     * Pääikkunan luominen
     *
     * @param service TasksService ohjelmalogiikkaan yhdistämiseen
     * @param projectMenuBuilder Project-menun rakentaja, joka huolehtii
     * yhteydestä tallennustaustajärjestelmien (Storages)
     * käyttöliittymäosuuksiin.
     */
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
        initTaskTableView();
        initProjectTableView();
        refresh();

        mainPane = new BorderPane();
        mainPane.setTop(menuBar);
        setViewMode(ViewMode.TASKS);

        Scene scene = new Scene(mainPane);
        window.setScene(scene);
        window.show();
    }

    /**
     * Projektin lisääminen
     *
     * StorageUi-käyttöliittymä kutsuu tätä funktiota, kun käyttäjä lisää
     * olemassa olevan projektin seurattavaksi. Aiheuttaa käyttöliittymän
     * päivittämisen. MainWindow välittää lisäämisen TasksService-luokalle.
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
     * StorageUi-käyttöliittymä kutsuu tätä funktiota, kun käyttäjä lisää uuden
     * projektin seurattavaksi. Käyttöliittymää ei päivitetä, koska uudessa
     * projektissa ei vielä ole yhtään tehtävää.
     *
     * @param information Projektia kuvaava informaatio
     */
    protected void createProject(ProjectInformation information) {
        service.getStorages().createProject(information);
        refresh();
    }

    /**
     * Käyttöliittymän päivittäminen
     *
     * Kutsutaan, kun projektit ja/tai tehtävät muuttuneet. Aiheuttaa
     * TasksServicessä luetteloiden päivittämisen ja sen jälkeen sekä näkyvillä
     * että piilossa olevien taulukkonäkymien päivittymisen.
     *
     */
    protected void refresh() {
        service.refresh();

        if (selectedProject != null) {
            updateSelectedProject();
        }

        switch (currentViewMode) {
            case TASKS:
                refreshTaskView();
                break;
            case PROJECTS:
                refreshProjectView();
                break;
            case TASKSOFPROJECT:
                refreshTasksOfProjectView();
                break;
        }
    }

    /**
     * Päivittää valitun projektin
     *
     * Koska service.refresh hakee uuden projektilistauksen, ei valittua
     * projektia löydy enää projektilistauksesta, ja valittu projekti pitää
     * korvata uuden lista projektilla, jolla on sama ProjectInformation
     *
     */
    private void updateSelectedProject() {
        for (Project project : service.allProjects()) {
            if (project.getInformation() == selectedProject.getInformation()) {
                selectedProject = project;
                break;
            }
        }
    }

    /**
     * Päivittää tehtävänäkymän
     */
    protected void refreshTaskView() {
        taskData = FXCollections.observableArrayList(service.allTasks());
        taskTable.setItems(taskData);

        if (service.allProjects().isEmpty()) {
            addTaskMenuItem.setDisable(true);
            taskTable.setPlaceholder(new Label("Ole hyvä ja avaa tai luo uusi \n "
                    + "tuntikirjanpito Projekti-valikosta"));
        } else {
            addTaskMenuItem.setDisable(false);
            taskTable.setPlaceholder(new Label("Ole hyvä ja lisää ensimmäinen tehtävä\n"
                    + "painamalla Ctrl+N"));
        }
    }

    /**
     * Päivittää projektinäkymän
     */
    protected void refreshProjectView() {
        projectData = FXCollections.observableArrayList(service.allProjects());
        projectTable.setItems(projectData);
    }

    /**
     * Päivittää projektin tehtävät -näkymän
     *
     * Valitsee näytettäväksi vain nykyiseen projektiin kuuluvat tehtävät
     *
     */
    protected void refreshTasksOfProjectView() {
        List<Task> taskList = service.allTasks()
                .stream()
                .filter(t -> t.getProject() == selectedProject)
                .collect(Collectors.toCollection(ArrayList::new));
        taskData = FXCollections.observableArrayList(taskList);
        taskTable.setItems(taskData);
    }

    /**
     * Tehtävätaulukkonäkymän alustaminen
     */
    protected void initTaskTableView() {
        taskTable = new TableView();

        TableColumn projectColumn = new TableColumn("Projekti");
        projectColumn.setCellValueFactory(new PropertyValueFactory("projectName"));

        TableColumn dateColumn = new TableColumn("Päivämäärä");
        dateColumn.setCellValueFactory(new PropertyValueFactory("dateString"));

        TableColumn descriptionColumn = new TableColumn("Kuvaus");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));

        TableColumn minutesColumn = new TableColumn("Kesto");
        minutesColumn.setCellValueFactory(new PropertyValueFactory("hourString"));

        taskTable.getColumns().setAll(projectColumn, dateColumn, descriptionColumn, minutesColumn);

        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList selectedItems = taskTable.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                taskSelected((Task) selectedItems.get(0));
            }
        });
    }

    /**
     * Projektitaulukkonäkymän alustaminen
     */
    protected void initProjectTableView() {
        projectTable = new TableView();

        TableColumn nameColumn = new TableColumn("Projektin nimi");
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn durationColumn = new TableColumn("Kesto yhteensä");
        durationColumn.setCellValueFactory(new PropertyValueFactory("sumHoursString"));

        projectTable.getColumns().setAll(nameColumn, durationColumn);
        projectTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList selectedItems = projectTable.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                projectSelected((Project) selectedItems.get(0));
            }
        });
    }

    /**
     * Päivittää projektin valinnan
     *
     * @param project Valittu projekti tai null, jos ei valittu
     */
    protected void projectSelected(Project project) {
        if (project == null && currentViewMode == ViewMode.TASKSOFPROJECT) {
            // In TasksOfProject view current project stays as selected
            return;
        }
        selectedProject = project;
        viewTasksOfProjectMenuItem.setDisable(project == null);
        hideProjectMenuItem.setDisable(project == null);
        deleteProjectMenuItem.setDisable(project == null);
    }

    /**
     * Päivittää tehtävän valinnan
     *
     * @param task Valittu tehtävä tai null, jos ei valittu
     */
    protected void taskSelected(Task task) {
        selectedTask = task;
        if (task == null) {
            projectSelected(null);
        } else {
            projectSelected(task.getProject());
        }
        editTaskMenuItem.setDisable(task == null);
    }

    /**
     * Valikon alustaminen
     *
     * @param stage
     */
    protected void initMenuBar(Stage stage) {
        menuBar = new MenuBar();
        menuBar.getMenus().add(createProjectMenu(stage));
        menuBar.getMenus().add(createTaskMenu());
        menuBar.getMenus().add(createViewMenu());
    }

    /**
     * Piilottaa tai poistaa nykyisen projektin
     *
     * @param remove
     */
    protected void hideOrRemoveCurrentProject(boolean remove) {
        Project project = selectedProject;
        Alert alert = new Alert(AlertType.CONFIRMATION, remove ? "Poistetaanko projektin tuntikirjanpito pysyvästi?" : "Poistetaanko projekti  seurannasta? Tuntikirjanpitoa ei poisteta.", ButtonType.YES, ButtonType.CANCEL);
        alert.setHeaderText(project.getName());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if (remove) {
                service.getStorages().removeProject(project.getInformation());
            } else {
                service.getStorages().hideProject(project.getInformation());
            }
            if (currentViewMode == ViewMode.TASKSOFPROJECT) {
                setViewMode(ViewMode.TASKS);
            }
        }
    }

    /**
     * Projektivalikon luominen
     *
     * Projektivalikossa on valinnat uusien projektien luomiseen
     * (ProjectMenuBuilderilla) sekä nykyisen projektin piilottaminen ja
     * poistaminen
     *
     * @param stage
     * @return
     */
    protected Menu createProjectMenu(Stage stage) {
        Menu projectMenu = projectMenuBuilder.build(stage, this);
        projectMenu.getItems().add(new SeparatorMenuItem());

        hideProjectMenuItem = new MenuItem("Poista seurannasta");
        EventHandler<ActionEvent> hideEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                hideOrRemoveCurrentProject(false);
            }
        };
        hideProjectMenuItem.setOnAction(hideEvent);

        deleteProjectMenuItem = new MenuItem("Poista projektin kirjanpito");
        EventHandler<ActionEvent> deleteEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                hideOrRemoveCurrentProject(true);
            }
        };
        deleteProjectMenuItem.setOnAction(deleteEvent);

        MenuItem exitMenuItem = new MenuItem("Lopeta");
        exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        EventHandler<ActionEvent> exitEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        };
        exitMenuItem.setOnAction(exitEvent);

        projectMenu.getItems().add(hideProjectMenuItem);
        projectMenu.getItems().add(deleteProjectMenuItem);
        projectMenu.getItems().add(new SeparatorMenuItem());
        projectMenu.getItems().add(exitMenuItem);

        return projectMenu;
    }

    /**
     * Tehtävävalikon luominen
     *
     * Tehtävävalikossa on valinnat uuden tehtävän lisäämiselle sekä
     * olemassaolevan tehtävän muokkaamiselle.
     *
     * @return Valikko
     */
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
                if (selectedTask != null && TaskDialog.editTaskDialog(service, selectedTask)) {
                    refresh();
                }
            }
        };
        editTaskMenuItem.setOnAction(editEvent);
        editTaskMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        editTaskMenuItem.setDisable(true);
        taskMenu.getItems().add(editTaskMenuItem);

        return taskMenu;
    }

    /**
     * Näkymän valitseminen
     *
     * Päivittää pääikkunan näkymän mukaiseksi sekä päivittää muutokset
     * valikkoon
     *
     * @param viewMode Valittu näkymä
     */
    protected void setViewMode(ViewMode viewMode) {
        currentViewMode = viewMode;

        viewTasksMenuItem.setSelected(viewMode == ViewMode.TASKS);
        viewProjectsMenuItem.setSelected(viewMode == ViewMode.PROJECTS);
        viewTasksOfProjectMenuItem.setSelected(viewMode == ViewMode.TASKSOFPROJECT);

        if (viewMode == ViewMode.PROJECTS) {
            mainPane.setCenter(projectTable);
            taskSelected(null);
            projectSelected((Project) projectTable.getSelectionModel().getSelectedItem());
            refreshProjectView();
        } else {
            mainPane.setCenter(taskTable);
            taskSelected((Task) taskTable.getSelectionModel().getSelectedItem());
            if (viewMode == ViewMode.TASKS) {
                refreshTaskView();
            } else {
                refreshTasksOfProjectView();
            }
        }

    }

    /**
     * Näkymä-valikon luominen
     *
     * Näkymä-valikosta valitaan, näytetäänkö tehtävien vai projektien luettelo.
     *
     * @return Valikko
     */
    protected Menu createViewMenu() {
        Menu viewMenu = new Menu("Näkymä");
        viewTasksMenuItem = new CheckMenuItem("Tehtävät");
        viewProjectsMenuItem = new CheckMenuItem("Projektit");
        viewTasksOfProjectMenuItem = new CheckMenuItem("Projektin tehtävät");

        EventHandler<ActionEvent> viewTasksEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setViewMode(ViewMode.TASKS);
            }
        };
        viewTasksMenuItem.setOnAction(viewTasksEvent);
        viewTasksMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN));

        EventHandler<ActionEvent> viewProjectsEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setViewMode(ViewMode.PROJECTS);
            }
        };
        viewProjectsMenuItem.setOnAction(viewProjectsEvent);
        viewProjectsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN));

        EventHandler<ActionEvent> viewTasksOfProjectEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setViewMode(ViewMode.TASKSOFPROJECT);
            }
        };
        viewTasksOfProjectMenuItem.setOnAction(viewTasksOfProjectEvent);
        viewTasksOfProjectMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN));

        viewMenu.getItems().add(viewTasksMenuItem);
        viewMenu.getItems().add(viewProjectsMenuItem);
        viewMenu.getItems().add(viewTasksOfProjectMenuItem);

        return viewMenu;
    }
}
