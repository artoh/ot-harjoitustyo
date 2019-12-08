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
 * Tehtävän tietojen käsittely SQLiten storagessa
 * 
 * Tietokantatoiminnot on toteutettu SqliteProject -luokassa, jota tämän 
 * luokan funktiot kutsuvat. 
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
    
    /**
     * Asettaa tietokannan riviavaimena olevan id:n
     * @param id 
     */
    void setId(int id) {
        this.taskId = id;
    }
    
    /**
     * Palauttaa tietokannan riviavaimena olevan id:n tai 0, jos ei ole vielä tallennettu tietokantaan
     * @return 
     */
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
