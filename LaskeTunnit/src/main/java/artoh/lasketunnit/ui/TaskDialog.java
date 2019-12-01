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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
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
    
    private TaskDialog(TasksService service) {
        this.service = service;
        this.projects = service.allProjects();
        createDialog();
        
    }
    
    private ComboBox createProjectCombo() {
        ComboBox combo = new ComboBox(FXCollections.observableArrayList(projects));
        combo.setValue(projects.get(0));
        return combo;
    }
    
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
        Label descriptionLabel = new Label("Tehtävän kuvaus");
        descriptionField = new TextField();
        Label durationLabel = new Label("Kesto");
        
        GridPane grid = new GridPane();
        grid.add(projectLabel, 0, 0);
        grid.add(projectCombo,1,0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(descriptionLabel, 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(durationLabel, 0, 3);
        dialog.getDialogPane().setContent(grid);
        
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        dialog.setResultConverter(dialogButton -> {
           if (dialogButton == buttonTypeOk) {
               if (task != null && task.getProject() != (Project) projectCombo.getValue()) {
                   // To change project, delete (old) task and create 
                   // new one into the new project.
                   task.delete();
                   task = null;
               }
               if (task == null) {
                   Project project = (Project) projectCombo.getValue();
                   task = project.createTask();
               } 
               task.setDate(datePicker.getValue());
               task.setDescription(descriptionField.getText());
               task.save();
           }
           return null;
        });
    }
    
    private boolean exec() {
        return dialog.showAndWait().isPresent();
    }
    
    public static boolean newTaskDialog(TasksService service) {
        TaskDialog dlg = new TaskDialog(service);
        return dlg.exec();
    }
    
    public static boolean editTaskDialog(TasksService service, Task task) {
        TaskDialog dlg = new TaskDialog(service);
        dlg.task = task;
        dlg.projectCombo.setValue(task.getProject());
        dlg.descriptionField.setText(task.getDescription());
        return dlg.exec();
    } 
    
}
