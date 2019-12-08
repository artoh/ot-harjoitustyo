/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.Task;
import static artoh.lasketunnit.sqlite.storage.SqliteStorageTest.NEWFILENAME;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arto
 */
public class SqliteProjectTest {
    
    static final String NEWFILENAME = "/tmp/sqliteprojecttest.sqlite";
    SqliteStorage storage;
    Project project;
    
    public SqliteProjectTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
        java.nio.file.Files.deleteIfExists(Paths.get(NEWFILENAME));
        storage = new SqliteStorage(NEWFILENAME);
        project = storage.createProject(new ProjectInformation("Test SQLite Project","sqlite","0"));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void taskInsertSuccess() throws ClassNotFoundException, SQLException {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("Test Description");
        assertTrue(task.save());
        assertEquals(project, task.getProject());
        
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + NEWFILENAME);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Task");
        assertTrue( rs.next() );
        assertEquals( "Test Description", rs.getString("Description"));
        assertEquals( 75, rs.getInt("Minutes"));
        assertEquals(1,rs.getInt("Id"));        
    }
    
    @Test
    public void taskListAfterInsertSuccess() {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("Second Test Description");
        assertTrue(task.save());        
        
        assertEquals(1, project.allTasks().size());
        
        Task another = project.createTask();
        another.setDate(LocalDate.of(2019,11,10));
        another.setMinutes(25);
        another.setDescription("Another");
        assertTrue(another.save());
        
        assertEquals(2, project.allTasks().size());
        assertEquals(100, project.sumMinutes());
    }
    
    @Test 
    public void taskIdSuccess() {
        Task task = project.createTask();
        SqliteTask sqltask = (SqliteTask) task;
        sqltask.setDate(LocalDate.of(2019, 12, 6));
        sqltask.setMinutes(360);
        sqltask.setDescription("Make tests");
        assertTrue(sqltask.save());
        assertEquals(1, sqltask.getId());        
    }
    
    @Test
    public void taskUpdateSuccess() throws ClassNotFoundException, SQLException {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("Test Description");
        assertTrue(task.save());

        Task loaded = project.allTasks().get(0);
        assertNotNull(loaded);
        SqliteTask sqltask = (SqliteTask) loaded;
        assertEquals(1, sqltask.getId());
        loaded.setDescription("Updated");
        assertTrue(loaded.save());
        
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + NEWFILENAME);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Task");
        assertTrue( rs.next() );
        assertEquals( "Updated", rs.getString("Description"));
        assertEquals( 75, rs.getInt("Minutes"));
        assertEquals(1,rs.getInt("Id"));                
    }
    
    @Test
    public void taskDeleteSuccess() {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("Second Test Description");
        assertTrue(task.save());        
        
        assertEquals(1, project.allTasks().size());
        
        Task another = project.createTask();
        another.setDate(LocalDate.of(2019,11,10));
        another.setMinutes(25);
        another.setDescription("Another");
        assertTrue(another.save());
        
        assertEquals(2, project.allTasks().size());
        assertEquals(100, project.sumMinutes());    
        
        another.delete();
        
        assertEquals(1, project.allTasks().size());
        assertEquals(75, project.sumMinutes());   
    }        
    
    @Test
    public void taskListEmptyWithSqlCorrupted() throws SQLException {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("One Test Description");
        task.save();
        assertEquals(1, project.allTasks().size());
        Statement stmt = storage.connection.createStatement();
        stmt.execute("DROP TABLE Task");
        assertEquals(0, project.allTasks().size());
    }
    
    @Test
    public void createTaskFailsWithSqlCorrupted() throws SQLException {        
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("One Test Description");
        Statement stmt = storage.connection.createStatement();
        stmt.execute("DROP TABLE Task");        
        assertFalse(task.save());
    }
    
    @Test
    public void updateFailsWhenSqlCorrupted() throws SQLException {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("One Test Description");
        assertTrue(task.save());
        task.setDescription("Updated task");
        assertEquals(1, project.allTasks().size());
        Statement stmt = storage.connection.createStatement();
        stmt.execute("DROP TABLE Task");                
        assertFalse(task.save());
    }
    
    @Test
    public void taskDeleteFailsWhenSqlCorrupted() throws SQLException {
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, Month.MARCH, 15));
        task.setMinutes(75);
        task.setDescription("One Another Test Description");
        task.save();
        Statement stmt = storage.connection.createStatement();
        stmt.execute("DROP TABLE Task");
        assertFalse(task.delete());
    }
}
