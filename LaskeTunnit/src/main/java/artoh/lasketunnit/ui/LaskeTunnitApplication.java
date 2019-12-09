package artoh.lasketunnit.ui;

import artoh.lasketunnit.md.storage.MdStorage;
import artoh.lasketunnit.md.ui.MdUi;
import artoh.lasketunnit.projectlist.FileProjectList;
import artoh.lasketunnit.projectlist.ProjectList;
import artoh.lasketunnit.service.TasksService;
import artoh.lasketunnit.sqlite.storage.SqliteStorage;
import artoh.lasketunnit.sqlite.ui.SqliteUi;
import artoh.lasketunnit.storage.Storage;
import artoh.lasketunnit.storage.Storages;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Ohjelman pääluokka
 * 
 * Ohjelman käynnistyessä ohjelman eri komponentit otetaan käyttöön, ja
 * sen jälkeen alustetaan ohjelman käyttöliittymä.
 * 
 * Kun ohjelmaan lisätään tallennuksen taustajärjestelmiä, rekisteröidään
 * ne käytettäväksi registerCompnents()-metodissa.
 * 
 * 
 * @author arto
 */
public class LaskeTunnitApplication extends Application {
    
    Storages storages;
    TasksService service;
    ProjectMenuBuilder projectMenuBuilder;
    
    @Override
    /**
     * Ohjelman käynnistystoimet
     * 
     * Ottaa ohjelman eri komponentit käyttöön ja muodostaa käyttöliittymän
     * 
     */
    public void start(Stage window) {
        
        ProjectList projectList = new FileProjectList("lasketunnit.ini");
        storages = new Storages(projectList);
        projectMenuBuilder = new ProjectMenuBuilder();
        
        registerComponents();
        
        service = new TasksService(storages);
        service.refresh();
        
        MainWindow mainWindow = new MainWindow(service, projectMenuBuilder);
        mainWindow.init(window);        

    }
    
    /**
     * Rekisteröi tallennusjärjestelmät
     * 
     * Kun ohjelmaan lisätään uusi tallennusjärjestelmä, rekisteröidään
     * se käyttöön tässä metodissa - muita muutoksia olemassa olevaan 
     * ohjelmakoodiin ei tarvita.
     */
    protected void registerComponents() {
        registerComponent(new MdStorage(), new MdUi());
        registerComponent(new SqliteStorage("lasketunnit.sqlite"), new SqliteUi());
        
    }
    
    /**
     * Rekisteröi tallennusjärjestelmän
     * 
     * @param storage Tiedostojen käsittelyn Storage-luokka
     * @param ui Käyttöliittymätoimintojen StoragaUi-luokka 
     */
    protected void registerComponent(Storage storage, StorageUi ui) {
        storages.registerStorage(storage);
        projectMenuBuilder.registerStorageUi(ui);
    }
    
    /**
     * Ohjelman käynnistävä metodi
     * 
     * JavaFX-yhteensopivuuden takia ohjelmassa on erikseen Main-luokka
     * ohjelman käynistämistä varten. Main-luokka kutsuu tätä
     * metodia.
     * 
     * @param args 
     */
    public static void main(String[] args) {        
               
        launch(LaskeTunnitApplication.class);
    }
}
