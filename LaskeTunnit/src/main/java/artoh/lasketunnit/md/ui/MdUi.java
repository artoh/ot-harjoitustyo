package artoh.lasketunnit.md.ui;

import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.ui.ProjectNameDialog;
import artoh.lasketunnit.ui.StorageUi;
import java.io.File;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Md-tiedoston käsittelyyn liittyvät käyttölittymätoiminnot
 * * 
 * @author ahyvatti
 */
public class MdUi implements StorageUi {

    @Override
    public ProjectInformation createProject(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        return getInformation(fileChooser.showSaveDialog(stage));
    }

    @Override
    public ProjectInformation addProject(Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        return getInformation(fileChooser.showOpenDialog(stage));
    }

    @Override
    public String storageName() {
        return "Markdown-tiedosto";
    }

    @Override
    public boolean allowCreate() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }
    private ProjectInformation getInformation(File file) {
        if (file == null || file.getName().isEmpty()) {
            return null;
        }
        
        String name = ProjectNameDialog.getProjectName();
        if (name == null) {
            return null;
        }
        
        return new ProjectInformation(name, "md", file.getPath());            
    }
    
}
