/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.ui;

import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.ui.ProjectNameDialog;
import artoh.lasketunnit.ui.StorageUi;
import javafx.stage.Stage;

/**
 *
 * SQLite-tietokannan käsittelyyn liittyvät käyttöliittymätoiminnot
 *
 * Koska tietokanta on vain tämän ohjelman käytössä, ovat tietokannan
 * kirjanpidot jo luettelossa, eikä niitä ole siis tarpeen tuodoa. Siksi
 * allowAdd() palauttaa falsen, eikä addProject-metodia ole toteutettu.
 *
 * @author arto
 */
public class SqliteUi implements StorageUi {

    @Override
    public ProjectInformation createProject(Stage stage) {

        String name = ProjectNameDialog.getProjectName();
        if (name == null) {
            return null;
        }

        return new ProjectInformation(name, "sqlite", "0");
    }

    @Override
    public ProjectInformation addProject(Stage stage) {
        // allowAdd() return false -> this operation not supported
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String storageName() {
        return "Tallenna tietokantaan";
    }

    @Override
    public boolean allowCreate() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return false;
    }

}
