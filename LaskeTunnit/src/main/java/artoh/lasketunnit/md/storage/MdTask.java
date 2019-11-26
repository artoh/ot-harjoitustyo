/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.service.AbstractTask;
import artoh.lasketunnit.service.Project;
import java.util.Date;


/**
 * Yksittäinen tehtävä tallennettuna md-tiedostoon
 * 
 * @author arto
 */
public class MdTask extends AbstractTask {

    protected MdProject project;    
    
    public MdTask(MdProject project) {
        this.project = project;
    }    
        
    @Override
    public Project getProject() {
        return (Project) project;
    }

    @Override
    public boolean save() {
        return this.project.save();
    }

    @Override
    public boolean delete() {
        return this.project.delete(this);
    }
    
}
