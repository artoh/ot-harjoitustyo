
package artoh.lasketunnit.service;

import java.util.List;

/**
 *
 * Tuntikirjanpidossa seurattava projekti
 * 
 * @author arto
 */
public interface Project {
    
    /**
     * Kaikki projektiin kuuluvat tehtävät
     * 
     * @return Lista projektin tehtävistä
     */
    List<Task> allTasks();
    
    /**
     * Projektiin käytettävä yhteisaika
     * 
     * @return Aika minuutteina
     */
    int sumMinutes();
    
    /**
     * Projektin tallennusinformaatio
     * 
     * @return Projektia kuvaava ProjectInformaton
     */
    ProjectInformation getInformation();
    
    /**
     * Projektin nimi
     * 
     * @return Projektin nimi
     */
    String getName();
    
    
    /**
     * Luo projektiin uuden tehtävän
     * 
     * Kun tehtävään on asetettu tehtävän tiedot, tulee tehtävä vielä
     * tallentaa erikseen Task.save()-metodilla
     * 
     * @return Luotu tehtävä
     */
    Task createTask();
    
    /**
     * Projektiin käytetty yhteisaika merkkijonona
     * 
     * @return Projektin yhteisaika
     */
    String getSumHoursString();
    
}
