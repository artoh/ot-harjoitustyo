package artoh.lasketunnit.service;

import java.util.List;

/**
 * Projektin abstrakti kantaluokka
 * 
 * Tällä tasolla on toteutettu projektin kokonaiskeston laskeminen sekä
 * projekti-informaation käsittely, jotka eivät riipu siitä, miten
 * tieto tallennetaan
 * 
 * @author arto
 */
public abstract class AbstractProject implements Project {
    
    private ProjectInformation projectinformation;
    
    @Override
    public int sumMinutes() {
        int minutes = 0;
        for (Task task : this.allTasks()) {
            minutes += task.getMinutes();
        }
        return minutes;
    }

    @Override
    public String getName() {
        return this.getInformation().getName();
    }
    
    @Override
    public ProjectInformation getInformation() {
        return this.projectinformation;
    }
    
    protected void setProjectInformation(ProjectInformation projectinformation) {
        this.projectinformation = projectinformation;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    
}
