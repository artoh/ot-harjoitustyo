/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.ui;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;

/**
 * Uuden tai lisättävän projektin nimeä kysyvä dialogi
 * 
 * Projektia kysytään staattisella funktiolla getProjectName()
 * 
 * @author arto
 */
public class ProjectNameDialog extends TextInputDialog {
    
    public ProjectNameDialog() {
        this.setTitle("LaskeTunnit");
        this.setHeaderText("Projektin nimi");
        this.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);        
    }
    
    /**
     * Kysyy projektille nimeä
     * 
     * @return Projektin nimi tai null, jos peruttu
     */
    public static String getProjectName() {
        ProjectNameDialog dialog = new ProjectNameDialog();
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return null;
        }
        return dialog.getEditor().getText();
    }
    
}
