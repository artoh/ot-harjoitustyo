/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.service.AbstractProject;
import artoh.lasketunnit.service.Task;
import java.util.ArrayList;
import java.util.List;

import artoh.lasketunnit.service.ProjectInformation;
import java.util.Collections;

/**
 * Projektin tuntikirjanpidon tallentaminen markdown-tiedostoksi
 * 
 * @author arto
 */
public class MdProject extends AbstractProject { 
    
    private List<MdTask> tasks;    
    MdFile file;
    
    public MdProject(ProjectInformation projectinformation) {
        this.tasks = new ArrayList<>();
        this.file = new MdFile(this);
        this.setProjectInformation(projectinformation);
    }
    
    @Override
    public List<Task> allTasks() {                
        ArrayList<Task> list = new ArrayList<>();
        for (MdTask task : tasks) {
            list.add(task);
        }
        Collections.sort(list, (t1, t2) -> {
            return t1.getDate().compareTo(t2.getDate());
        });
        return list;
    }
        
    @Override
    public Task createTask() {
        MdTask task = new MdTask(this);
        tasks.add(task);
        return task;        
    }
    
    /**
     * Poistaa tehtävän ja tallentaa
     * @param task Poistettava tehtävä
     * @return Onnistuiko
     */
    boolean delete(MdTask task) {
        tasks.remove(task);
        return save();
    }
    
    boolean load() {
        return file.load(this.getInformation().getStorageInfo());
    }
    
    boolean save() {
        return file.save(this.getInformation().getStorageInfo());
    }
    
    
 
    
}
