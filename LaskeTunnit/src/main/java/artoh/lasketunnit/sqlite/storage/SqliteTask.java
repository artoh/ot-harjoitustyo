/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.service.AbstractTask;
import artoh.lasketunnit.service.Project;
import java.time.LocalDate;

/**
 *
 * @author arto
 */
public class SqliteTask extends AbstractTask {

    protected SqliteProject project;
    protected int taskId = 0;
    
    public SqliteTask(SqliteProject project) {
        this.project = project;
    }
    
    public SqliteTask(SqliteProject project, int id, LocalDate date, int minutes, String description) {
        this.project = project;
        this.setId(id);
        this.setDate(date);
        this.setMinutes(minutes);
        this.setDescription(description);
    }
    
    void setId(int id) {
        this.taskId = id;
    }
    
    int getId() {
        return taskId;
    }
    
    @Override
    public Project getProject() {
        return (Project) project;
    }

    @Override
    public boolean save() {
        return project.saveTask(this);
    }

    @Override
    public boolean delete() {
        return project.deleteTask(this);
    }
    
}
