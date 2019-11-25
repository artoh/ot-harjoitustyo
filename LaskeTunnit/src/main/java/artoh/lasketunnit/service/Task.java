
package artoh.lasketunnit.service;

import java.time.LocalDate;


/**
 *
 * Tuntikirjanpidossa oleva yksittäinen tehtävä
 * 
 * @author arto
 */
public interface Task {
    
    /**
     * Tehtävän päiväys
     * 
     * @return Päiväys
     */
    LocalDate getDate();
 
    /**
     * Tehtävän kuvaus
     * 
     * @return Kuvaus 
     */
    String getDescription();
    
    /**
     * Tehtävään käytetty aika
     * @return Aika minuutteina
     */
    int getMinutes();
    
    /**
     * Projekti, johon tehtävä kuuluu
     * 
     * @return Projekti
     */
    Project getProject();
    
    /**
     * Asetaa tehtävän päiväyksen
     * @param date Päiväys
     */
    void setDate(LocalDate date);
    
    /**
     * Asettaa tehtävän kuvauksen
     * @param description Kuvaus
     */
    void setDescription(String description);
    
    /**
     * Asettaa tehtävään käyteyn ajan
     * @param minutes Aika minuutteina
     */
    void setMinutes(int minutes);
    
    /**
     * Tallettaa tehtävän
     * @return Onnistuiko
     */
    boolean save();
    
    /**
     * Poistaa tehtävän 
     * @return Onnistuiko
     */
    boolean delete();
    
}
