package artoh.lasketunnit.service;

/**
 * Projektin tiedot
 * 
 * ProjectInformation ylläpitää projektin tallentamiseen ja lataamiseen 
 * tarvittavia tietoja.
 * 
 * Jokaiselle projektille määritellään
 * 1) Projektista käyttäjälle näytettävä nimi
 * 2) Projektin tallennusjärjestelmän (storage) tunnus
 * 3) Merkkijono, joka määrittelee projektin tallentamisen kyseisen taustajärjestelmän
 * käyttämässä muodossa.
 * 
 * Tietorakenteen tarkoituksena on mahdollistaa erilaisten tallennusratkaisujen
 * lisääminen ilman, että muuta koodia on tarpeen muuttaa; ja niin, että tuntikirjanpitoa
 * voidaan käsitellä yhtenäisellä tavalla riippumatta siitä, että merkintöjä on tallennettu
 * eri taustajärjestelmään ja eri tiedostoihin (tai muihin tallennusyksiköihin)
 * 
 * @author arto
 */
public class ProjectInformation {
    
    /**
     * Projektin näytettävä nimi
     */
    protected String name;
    /**
     * Projektin tallennusjärjestelmän (Storage) tunniste
     */
    protected String storageId;
    /**
     * Projektin tallennusjärjestelmän tallennusta kuvaavat tarkemmat tiedot
     * 
     * Esimerkiksi tallennettaessa tiedostoon tiedoston polku, tietokantaan
     * tietokantayhteyden tiedot jne.
     */
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
