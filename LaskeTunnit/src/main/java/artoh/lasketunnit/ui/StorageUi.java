/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import artoh.lasketunnit.service.ProjectInformation;
import javafx.stage.Stage;

/**
 * Tallennusjärjestelmien käyttöliittymäosien kantaluokka
 * 
 * Koska erilaiset tallennusjärjestelmät tarvitsevat erilaisia valintoja
 * (esimerkiksi tiedostoon tallennettaessa valitaan tiedosto mutta
 * pilvipalveluita käytettäessä annetaan käyttövaltuustietoja ja suoraan
 * tietokantapalvelinta käytettäessä käyttäjätiedot ja taulutiedot), on
 * jokaisella taustajärjestelmällä omat käyttöliittymäosansa projektin avaamista
 * ja luomista varten.
 * 
 * Taustajärjestelmät otetaan käyttöön LaskeTunnitApplication-luokan
 * registerComponents()-metodissa
 * 
 * @author ahyvatti
 */
public interface StorageUi {

    /**
     * Projektin luominen
     * 
     * Näyttää projektin määrittelyyn tarvittavat dialogit ja palauttaa projektia
     * määrittelevän ProjectInformation-olion
     * 
     * @param stage Ikkunoiden tarvitsema Stage-vanhempi
     * @return Projektin määrittelevä ProjectInformation, tai null jos peruttu
     */
    public ProjectInformation createProject(Stage stage);

    
    /**
     * Olemassa olevan projektin avaaminen
     * 
     * Näyttää projektia avattaessa tarvittavat dialogit ja palauttaa projektia
     * määrittelevätän ProjectInformation-olion
     * 
     * @param stage Ikkunoiden tarvitsema Stage-vanhempi
     * @return Projektin määrittelevä ProjectInformation, tai null jos peruttu
     */
    public ProjectInformation addProject(Stage stage);

    /**
     * Valikossa näytettävä nimi
     * 
     * Esimerkiksi md-tallennukselle "Markdown-tiedosto"
     * 
     * @return Nimi merkkijonona
     */
    public String storageName();
    
    /**
     * Sisällytetäänkö uusien projektien valikkoon
     * 
     * Jos järjestelmä ei mahdollista uuden projektin luomista (esim. tiedot
     * haetaan kalenteripalvelusta, jossa tarvittavaa merkkausta ei voi luoda
     * ohjelmallisesti), palautetaan false, eikä tätä järjestelmää näytetä
     * Uusi projekti -valikossa
     * 
     * @return 
     */
    public boolean allowCreate();
    
    /**
     * Voidaanko olemassa olevia projekteja lisätä
     * 
     * Jos järjestelmä on esimerkiksi sellainen tietokanta, jota käytetään
     * vain tämän ohjelman kautta, voidaan tässä palauttaa false, jolloin
     * tätä järjestelmää ei näytetä Avaa ja tuo -valikossa
     * 
     * @return 
     */
    
    public boolean allowAdd();
    
}
