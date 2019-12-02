/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.Task;
import artoh.lasketunnit.service.TasksService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * Tehtävän luomisen ja muokkaamisen dialogi
 * 
 * Dialogi näytetään käyttämällä staattisia metodeja newTaskDialog() ja
 * editTaskDialog().
 * 
 * Dialogi huolehtii muutosten tallentamisesta. Jos muutos onnistui, palautetaan
 * true ja käyttöliittymä päivittää itsensä.
 * 
 * @author ahyvatti
 */
public class TaskDialog {

    private Task task;
    private TasksService service;
    private Dialog<Task> dialog;
    private List<Project> projects;

    private ComboBox projectCombo;
    private DatePicker datePicker;
    private TextField descriptionField;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;

    private TaskDialog(TasksService service) {
        this.service = service;
        this.projects = service.allProjects();
        createDialog();

    }

    /**
     * Luo projektin valinnassa käytettävän ComboBoxin
     * 
     * ComboBox sisältää kaikki projektit.
     * 
     * @return ComboBox
     */
    private ComboBox createProjectCombo() {
        ComboBox combo = new ComboBox(FXCollections.observableArrayList(projects));
        combo.setValue(projects.get(0));
        return combo;
    }

    /**
     * Asettaa spinnerin käsittelemään vain numeorita
     *
     * https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
     *
     * @param spinner
     */
    private void setSpinnerNumeric(Spinner spinner) {
        spinner.getEditor().addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                    t.consume();
                }
            }
        });
    }

    /**
     * Luo tehtävän pituuden valinnassa käytettävän komponentin, joka koostuu
     * kahdesta spinneristä (tunnit ja minuutit) ja niitä selostavista labeleista.
     * 
     * @return 
     */
    private HBox createSpinners() {

        hourSpinner = new Spinner<>(0, 24, 1, 1);
        setSpinnerNumeric(hourSpinner);
        hourSpinner.setEditable(true);
        Label hourLabel = new Label("h ");
        minuteSpinner = new Spinner<>(0, 59, 0, 15);
        setSpinnerNumeric(minuteSpinner);
        minuteSpinner.setEditable(true);
        Label minuteLabel = new Label("min");

        return new HBox(10, hourSpinner, hourLabel, minuteSpinner, minuteLabel);

    }

    /**
     * Dialogin luominen
     */
    private void createDialog() {
        dialog = new Dialog<>();
        if (task == null) {
            dialog.setTitle("Uusi tehtävä");
        } else {
            dialog.setTitle("Muokkaa tehtävää");
        }

        Label projectLabel = new Label("Projekti");
        projectCombo = createProjectCombo();
        Label dateLabel = new Label("Päivämäärä");
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.getEditor().setDisable(true);
        Label descriptionLabel = new Label("Tehtävän kuvaus");
        descriptionField = new TextField();
        Label durationLabel = new Label("Kesto");

        GridPane grid = new GridPane();
        grid.add(projectLabel, 0, 0);
        grid.add(projectCombo, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(descriptionLabel, 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(durationLabel, 0, 3);
        grid.add(createSpinners(), 1, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                if (task != null && task.getProject() != (Project) projectCombo.getValue()) {
                    // Jos projekti on vaihdettu, poistetaan vanha tehtävä ja
                    // luodaan uusi
                    task.delete();
                    task = null;
                }
                if (task == null) {
                    Project project = (Project) projectCombo.getValue();
                    task = project.createTask();
                }
                task.setDate(datePicker.getValue());
                task.setDescription(descriptionField.getText());
                task.setMinutes(Integer.parseInt(hourSpinner.getEditor().getText()) * 60
                        + Integer.parseInt(minuteSpinner.getEditor().getText()));
                task.save();
                return task;
            }
            return null;
        });
    }

    private boolean exec() {
        return dialog.showAndWait().isPresent();
    }

    /**
     * Uuden tehtävän luominen
     * 
     * Dialogi huolehtii luodun tehtävän tallentamisesta.
     * 
     * @param service TaskService
     * @return Tosi, jos uusi tehtävä tallennettu. Tällöin käyttöliittymän pitää päivittää itsensä.
     */
    public static boolean newTaskDialog(TasksService service) {
        TaskDialog dlg = new TaskDialog(service);
        return dlg.exec();
    }

    /**
     * Tehtävän muokkaaminen
     * 
     * Dialogi huolehtii muokatun tehtävän talletamisesta. Jos tehtävän projekti muuttuu, 
     * poistetaan vanha tehtävä ja luodaan uusi,
     * 
     * @param service TasksService
     * @param task Muokattava tehtävä
     * @return Tosi, jos tehtävää muokattu. Tällöin käyttöliittymän pitää päivittää itsensä.
     */
    public static boolean editTaskDialog(TasksService service, Task task) {
        TaskDialog dlg = new TaskDialog(service);
        dlg.task = task;
        dlg.projectCombo.setValue(task.getProject());
        dlg.descriptionField.setText(task.getDescription());
        String hourString = "" + (task.getMinutes() / 60);
        dlg.hourSpinner.getEditor().setText(hourString);
        String minuteString = "" + (task.getMinutes() % 60);
        dlg.minuteSpinner.getEditor().setText(minuteString);
        return dlg.exec();
    }

}
