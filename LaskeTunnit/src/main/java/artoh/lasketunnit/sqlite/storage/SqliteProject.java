/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.service.AbstractProject;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.Task;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arto
 */
public class SqliteProject extends AbstractProject {

    SqliteStorage storage;

    public SqliteProject(ProjectInformation information, SqliteStorage storage) {
        this.storage = storage;
        this.setProjectInformation(information);
    }

    @Override
    public List<Task> allTasks() {
        List<Task> list= new ArrayList<>();
        try {
            Statement stmt = storage.getConnection().createStatement();
            String query = "SELECT Id, Date, Minutes, Description FROM Task WHERE Project=" + Integer.parseInt(getInformation().getStorageInfo()) +
                    " ORDER BY Date DESC";
            ResultSet rs = stmt.executeQuery(query);            
            while (rs.next()) {                
                list.add(new SqliteTask(this, rs.getInt(1), LocalDate.parse(rs.getString(2)), rs.getInt(3), rs.getString(4)));
            }            
        } catch (SQLException ex) {              
        }
        return list;        
    }

    @Override
    public Task createTask() {
        return new SqliteTask(this);
    }

    boolean saveTask(SqliteTask task) {
        if (task.getId() == 0) {
            return insertTask(task);
        } else {
            return updateTask(task);
        }
    }

    private boolean insertTask(SqliteTask task) {
        try {
            PreparedStatement pstmt = storage.getConnection().prepareStatement("INSERT INTO Task (Date,Minutes,Description,Project) VALUES (?,?,?,?)");
            pstmt.setString(1, task.getDate().toString());
            pstmt.setInt(2, task.getMinutes());
            pstmt.setString(3, task.getDescription());
            pstmt.setInt(4, Integer.parseInt(getInformation().getStorageInfo()) );
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();                
            rs.next(); 
            task.setId(rs.getInt(1));
            return true;
        } catch (SQLException ex) {            
            // Return false when fails 
        }        
        return false;
    }
    
    private boolean updateTask(SqliteTask task) {
        try {
            PreparedStatement pstmt = storage.getConnection().prepareStatement("UPDATE Task SET Date=?, Minutes=?, Description=? WHERE Id=?");
            pstmt.setString(1, task.getDate().toString());
            pstmt.setInt(2, task.getMinutes());
            pstmt.setString(3, task.getDescription());
            pstmt.setInt(4, task.getId() );
            pstmt.execute();
            return true;
        } catch (SQLException ex) {
            // Return false when fails 
            return false;
        }                
    }
            
    boolean deleteTask(SqliteTask task) {
        try {
            Statement stmt = storage.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM Task WHERE Id=" + task.getId());
            return true;
        } catch (SQLException ex) {
            return false;
        }        
    }
}
