
package artoh.lasketunnit.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;

/**
 * Kirjanpidon tallennusjärjestelmä
 * 
 * Ohjelma mahdollistaa tuntikirjanpidon tallentamisen eri muodoissa.
 * Taustajärjestelmä abstraktoidaan Storage-rajapinnalla.
 * 
 * @author arto
 */
public interface Storage {
    
    /**
     * Palauttaa taustajärjestelmän yksilöivän merkkijonon
     * 
     * @return StorageId
     */
    public String storageId();
    
    /**
     * Luo uuden projektin 
     * 
     * @param information Projektia kuvaava informaatio
     * @return 
     */
    public Project createProject(ProjectInformation information);
    
    /**
     * Lataa olemassaolevan projektin 
     * 
     * @param information Projektia kuvaava informaatio
     * @return 
     */
    public Project loadProject(ProjectInformation information);
    
    /**
     * Poistaa projektin pysyvästi
     * 
     * @param information Projektia kuvaava informaatio
     * @return 
     */
    public boolean deleteProject(ProjectInformation information);
}
