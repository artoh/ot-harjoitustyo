package artoh.lasketunnit.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.storage.Storage;
import artoh.lasketunnit.projectlist.ProjectList;
import java.util.ArrayList;
import java.util.List;

/**
 * Tuntikirjanpitoja tallentavien taustajärjestelmien katalogi
 * 
 * Taustajärjestelmät rekisteröidään tätä luokkaa käyttäen, ja tämä
 * luokka pitää yllä taustajärjestelmien luetteloa.
 * 
 * @author arto
 */
public class Storages {
   
    private List<Storage> storages;
    private ProjectList projectList;
    
    public Storages(ProjectList projectList) {
        this.projectList = projectList;
        storages = new ArrayList<>();
    }
    
    /**
     * Lisää olemassaolevan projektin seurantaan
     * 
     * @param info Projektia kuvaava informaatio
     * @return Lisätty projekti
     */
    public Project addProject(ProjectInformation info) {
        Storage storage = getStorage(info.getStorageId());
        if (storage != null) {
            Project project = storage.loadProject(info);
            if (project != null) {
                if (projectList.addProject(project.getInformation()) == true) {
                    return project;
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * Luo uuden projektin
     * 
     * @param info Projektia kuvaava informaatio
     * @return Luotu projekti
     */
    public Project createProject(ProjectInformation info) {
        Storage storage = getStorage(info.getStorageId());
        if (storage != null) {
            Project project = storage.createProject(info);
            if (project != null) {
                if (projectList.addProject(project.getInformation()) == true) {
                    return project;
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * Poistaa projektin
     * 
     * @param info Projektia kuvaava informaatio
     * @return Onnistuiko poistaminen
     */
    public boolean removeProject(ProjectInformation info) {
        Storage storage = getStorage(info.getStorageId());
        if (storage != null) {
            if (storage.deleteProject(info) == true) {
                return projectList.removeProject(info);
            }
        }
        return false;
    }
    
    /**
     * Piilottaa projektin luettelosta. Projektin kirjanpitoa
     * ei poisteta
     * 
     * @param info Projektia kuvaava informaatio.
     * @return Onnistuiko
     */
    public boolean hideProject(ProjectInformation info) {
        return projectList.removeProject(info);
    }
    
    /**
     * Listaa kaikki seurattavat projektit 
     * 
     * @return Lista kaikista projekteista
     */    
    public List<Project> allProjects() {
        List<Project> list = new ArrayList<>();
        
        for (ProjectInformation info : projectList.getProjects()) {
            Storage storage = getStorage(info.getStorageId());
            if (storage != null) {
                Project project = storage.loadProject(info);
                if (project != null) {
                    list.add(storage.loadProject(project.getInformation()));
                }
            }
        }
        return list;
    }
    
    /**
     * Rekisteröi tallennustaustajärjestelmän (Storagen)
     * 
     * @param storage Rekisteröitä Storage
     */
    public void registerStorage(Storage storage) {
        storages.add(storage);
    }

    /**
     * Palauttaa taustajärjestelmän (Storagen)
     * 
     * @param storageId Taustajärjestelmän tunniste
     * @return Taustajärjestelmä
     */
    Storage getStorage(String storageId) {
        for (Storage storage : storages) {
            if (storage.storageId().equals(storageId)) {
                return storage;
            }
        }
        return null;
    }    
}
