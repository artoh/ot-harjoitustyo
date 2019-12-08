/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.storage.Storage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tuntikirjanpidot yhdeksi SQLite-tiedostoksi tallentava komponentti
 * 
 * @author arto
 */
public class SqliteStorage implements Storage {

    protected Connection connection = null;
    
    private final String createProjectTableString = "CREATE TABLE IF NOT EXISTS Project (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "Name TEXT NOT NULL)";
    private final String createTaskTableString = "CREATE TABLE IF NOT EXISTS Task (" + 
            "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "Project INTEGER REFERENCES Project(id), " +
            "Date DATE NOT NULL, " +
            "Minutes INTEGER NOT NULL, " +
            "Description TEXT )";
    
    public SqliteStorage(String filename) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
            
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createProjectTableString);
            stmt.executeUpdate(createTaskTableString);
            stmt.close();            
            
        } catch (Exception e) {
            System.out.println(e.toString());
            // Do nothing ;(
        }
    }
    
    @Override
    public String storageId() {
        return "sqlite";
    }

    @Override
    public Project createProject(ProjectInformation information) {
        final String createProjectStatement = "INSERT INTO Project(Name) VALUES(?)";
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(createProjectStatement);
            pstmt.setString(1, information.getName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next(); 
            ProjectInformation newInformation = new ProjectInformation(information.getName(), this.storageId(), rs.getString(1));
            return new SqliteProject(newInformation, this);
            
        } catch (SQLException ex) {
            // Return null as fail :(
        }
        return null;
    }

    @Override
    public Project loadProject(ProjectInformation information) {
        if (connection == null) {
            return null;
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Name FROM Project WHERE Id=" + Integer.parseInt(information.getStorageInfo()));
            if (rs.next()) {
                ProjectInformation newInformation = new ProjectInformation(rs.getString(1), this.storageId(), information.getStorageInfo());
                return new SqliteProject(newInformation, this);
            }
        } catch (SQLException ex) {
            // Return null as fail :(            
        }
        return null;
    }

    @Override
    public boolean deleteProject(ProjectInformation information) {
        if (connection == null) {
            return false;
        }
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM Task WHERE Project=" + Integer.parseInt(information.getStorageInfo()));
            stmt.execute("DELETE FROM Project WHERE Id=" + Integer.parseInt(information.getStorageInfo()));
            return true;                    
        } catch (SQLException ex) {
            return false;
        }
    }
    
    Connection getConnection() {
        return connection;
    }
    
    
}
