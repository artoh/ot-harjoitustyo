package artoh.lasketunnit.domain;

/**
 * Projektin tiedot
 * 
 * @author arto
 */
public class ProjectInformation {
    
    protected String name;
    protected String storageId;
    protected String storageInfo;
        

    /**
     * Projektin käyttöön tarvittavat tiedot
     * 
     * @param name Projektin nimi
     * @param storageId Projektin tallennusjärjestelmän (Storage) tunniste
     * @param storageInfo Projektin tallennusta kuvaavat tarkemmat tiedot
     */
    public ProjectInformation(String name, String storageId, String storageInfo) {
        this.name = name;
        this.storageId = storageId;
        this.storageInfo = storageInfo;
    }
    
    /**
     * Projektin nimi
     * 
     * @return Projektin näytettävä nimi
     */
    public String getName() {
        return name;
    }
    
    /**
     * Projektin tallennusjärjestelmän tunniste
     * 
     * @return Projektin tallennusjärjestelmän tunniste
     */
    public String getStorageId() {
        return storageId;
    }
    
    /**
     * Projektin tallentamisessa tarvittavat tiedot
     * 
     * Tallennusjärjestelmän (Storage) määrittelemä merkkijono, jonka avulla
     * Storage tallentaa ja lataa projektin, Esimerkiksi tiedostoon tallennettaessa
     * sisältää tiedoston polun.
     * 
     * @return Projektin tallentamisessa tarvittavat tiedot
     */
    public String getStorageInfo() {
        return storageInfo;
    }        
    
}
