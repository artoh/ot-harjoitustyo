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
 * Ohjelman toimintalogiikan käyttöliittymälle tarjoava service-luokka
 * 
 * Käyttöliittymä pääsee selaamaan tehtäviä kutsumalla ensin refresh()-funktiota,
 * joka päivittää tehtävät taustajärjestelmistä, ja sen jälkeen hakemalla tehtävät
 * allTasks()-funktiolla.
 * 
 * Uusien projektien käsittelyn käyttöliittymä tekee kuitenkin suoraan Storages-
 * luokan avulla, minkä on saatavissa getStorages()-metodilla
 *
 * @author arto
 */
public class TasksService {
    
    private Storages storages;
    
    private List<Project> projects;
    private List<Task> tasks;
    
    
    public TasksService(Storages storages) {
        this.storages = storages;
        this.projects = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Päivittää projektien ja tehtävien luettelon
     */
    public void refresh() {
        projects = storages.allProjects();
        tasks.clear();
        
        for (Project project : projects) {
            List<Task> taskList = project.allTasks();
            for (Task task : taskList) {
                tasks.add(task);
            }
        }
    }
    
    /**
     * Kaikki seurattavat projektit
     * 
     * @return 
     */
    public List<Project> allProjects() {
        return this.projects;
    }
    
    /**
     * Kaikkien projektien kaikki tehtävät
     * 
     * @return 
     */
    public List<Task> allTasks() {
        return this.tasks;
    }
    
    /**
     * Tallennukseen käytettävä Storage-olio
     * @return 
     */
    public Storages getStorages() {
        return storages;
    }
    
    
}
