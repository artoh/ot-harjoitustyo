package artoh.lasketunnit.projectlist;

import artoh.lasketunnit.domain.ProjectInformation;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 *
 *  Seurattavien projektien listan tallentaminen tiedostoon
 * 
 *  Jokaisesta projektista kirjataan tiedostoon kolme riviä:
 *  1) Projektin nimi
 *  2) Projektin tallennusjärjestelmän tunniste
 *  3) Projektin tallentamiseen tarvittavat tiedot
 * 
 * @author arto
 */
public class FileProjectList implements ProjectList {

    protected ArrayList<ProjectInformation> projects;
    protected String filename;
    
    /**
     * Seurattavien projektien listan tallentaminen tiedostoon.
     * 
     * Oliota luotaessa projektien luettelo luetaan tiedostosta
     * 
     * @param filename Luettelotiedoston nimi
     */
    public FileProjectList(String filename) {
        this.filename = filename;
        this.projects = new ArrayList<>();
        load();
    }
    
    @Override
    public List<ProjectInformation> getProjects() {
        return projects;
    }

    @Override
    public boolean addProject(ProjectInformation project) {
        projects.add(project);
        return save();
    }

    @Override
    public boolean removeProject(ProjectInformation project) {
        projects.remove(project);
        return save();        
    }
    
    /**
     * Lataa luettelon tiedostosta
     */
    protected boolean load() {
        try {
            InputStream in = new FileInputStream(filename);
            Reader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader breader = new BufferedReader(reader);
            String name = null;
            while ((name = breader.readLine()) != null) {
                String storage = breader.readLine();
                String info = breader.readLine();
                projects.add(new ProjectInformation(name, storage, info));
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Tallentaa luettelon
     * 
     * @return Onnistuiko tallentaminen
     */
    protected boolean save() {
        try {
            OutputStream out = new FileOutputStream(filename);
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bwriter = new BufferedWriter(writer);
            for (ProjectInformation project : projects) {
                bwriter.write(project.getName());
                bwriter.newLine();
                bwriter.write(project.getStorageId());
                bwriter.newLine();
                bwriter.write(project.getStorageInfo());
                bwriter.newLine();
            }           
            bwriter.close();
        } catch (IOException e) {
            return false;
        } 
        return true;
    }    
}
