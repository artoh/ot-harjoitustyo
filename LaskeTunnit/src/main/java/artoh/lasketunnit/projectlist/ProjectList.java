
package artoh.lasketunnit.projectlist;

import artoh.lasketunnit.service.ProjectInformation;

import java.util.List;


/**
 * Luettelo seurattavista projekteista
 * 
 * @author arto
 */
public interface ProjectList {
 
    /**
     * Luettelo seurattavista projekteista
     * 
     * @return Luettelo projekteista
     */
    public List<ProjectInformation> getProjects();
    
    
    /**
     * Lisää projektin seurattavien projektien luetteloon
     * 
     * @param project Projektin tiedot
     * @return Onnistuiko lisääminen
     */
    public boolean addProject(ProjectInformation project);
    
    
    /**
     * Poista projekti seurannasta
     * 
     * @param project Projektin tiedot
     * @return Onnistuiko poistaminen
     */
    public boolean removeProject(ProjectInformation project);
}
