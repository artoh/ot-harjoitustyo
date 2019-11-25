/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.storage.Storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author arto
 */
public class MdStorage implements Storage {

    @Override
    public String storageId() {
        return "md";
    }

    @Override
    public Project createProject(ProjectInformation information) {
        MdProject project = new MdProject(information);
        if (project.save()==false) {
            return null;
        }
        return project;
    }

    @Override
    public Project loadProject(ProjectInformation information) {
        MdProject project = new MdProject(information);
        if (project.load()) {
            return project;
        }
        return null;
    }

    @Override
    public boolean deleteProject(ProjectInformation information) {
        try {
            return Files.deleteIfExists(Paths.get(information.getStorageInfo()));
        } catch (IOException e) {
            return false;
        }
    }
    
}
