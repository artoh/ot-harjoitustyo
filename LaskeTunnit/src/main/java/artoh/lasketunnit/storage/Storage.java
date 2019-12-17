
package artoh.lasketunnit.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;

/**
 * Kirjanpidon tallennusjärjestelmä
 * 
 * Ohjelma mahdollistaa tuntikirjanpidon tallentamisen eri muodoissa.
 * Taustajärjestelmä abstraktoidaan Storage-rajapinnalla.
 * 
 * Konkeettinen taustajärjestelmä (esimerkiksi markdown-tiedostoon tallentaminen)
 * tehdään toteuttamalla tämän rajapinnan toteuttava luokka, joka rekisteröidään
 * Storagesiin. Rekisteröiminen tehdään LaskeTunnitApplications-luokan
 * registerComponents()-metodissa.
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
     * @return Luotu projekti
     */
    public Project createProject(ProjectInformation information);
    
    /**
     * Lataa olemassaolevan projektin 
     * 
     * @param information Projektia kuvaava informaatio
     * @return Ladattu projekti
     */
    public Project loadProject(ProjectInformation information);
    
    /**
     * Poistaa projektin pysyvästi
     * 
     * @param information Projektia kuvaava informaatio
     * @return Onnistuiko
     */
    public boolean deleteProject(ProjectInformation information);
}
