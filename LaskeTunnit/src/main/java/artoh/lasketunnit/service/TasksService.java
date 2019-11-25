/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import artoh.lasketunnit.storage.Storages;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arto
 */
public class TasksService {
    
    private Storages storages;
    
    List<Project> projects;
    List<Task> tasks;
    
    
    public TasksService(Storages storages) {
        this.storages = storages;
        this.projects = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    
    public void refresh() {
        projects = storages.allProjects();
        tasks.clear();
        
        for(Project project : projects) {
            List<Task> taskList = project.allTasks();
            for( Task task : taskList) {
                tasks.add(task);
            }
        }
    }
    
    
    
}
