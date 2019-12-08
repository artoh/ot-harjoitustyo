/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class SqliteStorageTest {
    
    static final String NEWFILENAME = "/tmp/sqlitestoragetest.sqlite";
    
    public SqliteStorageTest() {
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
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void canInitStorage() {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        assertNotNull(storage);
        File file = new File(NEWFILENAME);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
    
    @Test
    public void canCreateProject() {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Project project = storage.createProject(new ProjectInformation("Name","-","-"));
        assertNotNull(project);
        assertEquals("sqlite", project.getInformation().getStorageId());
        assertEquals("1", project.getInformation().getStorageInfo());
        File file = new File(NEWFILENAME);
        assertTrue(file.exists());        
        assertTrue(file.length() > 0);
    }
    
    @Test
    public void createdProjectExitsInDb() throws ClassNotFoundException, SQLException {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Project project = storage.createProject(new ProjectInformation("Name","-","-"));
        
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + NEWFILENAME);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Project");
        assertTrue( rs.next() );
        assertEquals( "Name", rs.getString("Name"));
        assertEquals(1,rs.getInt("Id"));
    }
    
    @Test
    public void createProjectFailsWithIncorrectDb() {
        SqliteStorage fakeStorage = new SqliteStorage(":/dippadappu/duu");
        Project project = fakeStorage.createProject(new ProjectInformation("Name","-","-"));
        assertNull(project);
    }
    
    @Test
    public void createProjectFailsWithFileError() throws IOException {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        java.nio.file.Files.deleteIfExists(Paths.get(NEWFILENAME));
        Project project = storage.createProject(new ProjectInformation("Name","-","-"));
        assertNull(project);
    }
    
    @Test
    public void loadUnexitstProjectFails() {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Project project = storage.loadProject(new ProjectInformation("Name","-","5"));
        assertNull(project);
    }
    
    @Test
    public void loadFromIncorrectStorageFails() {
        SqliteStorage fakeStorage = new SqliteStorage(":/dippadappu/duu");
        Project project = fakeStorage.loadProject(new ProjectInformation("Name","-","0"));
        assertNull(project);
    }

    @Test
    public void loadProjectFailsSqlError() throws IOException, SQLException {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Statement stmt = storage.getConnection().createStatement();
        stmt.execute("DROP TABLE Project");        
        Project project = storage.loadProject(new ProjectInformation("Name","-","15"));
        assertNull(project);
    }

    
    @Test
    public void loadExitsProjectSuccess() {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Project project = storage.createProject(new ProjectInformation("Name","-","0"));
        
        SqliteStorage another = new SqliteStorage(NEWFILENAME);
        Project loaded = storage.loadProject(new ProjectInformation("Something","-","1"));
        assertNotNull(loaded);
        assertEquals("Name",loaded.getInformation().getName());
        assertEquals("sqlite",loaded.getInformation().getStorageId());
    }        
    
    @Test
    public void deleteProjectSuccess() throws ClassNotFoundException, SQLException {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Project project = storage.createProject(new ProjectInformation("Name","-","0"));
        storage.deleteProject(project.getInformation());
        
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + NEWFILENAME);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Project");
        assertFalse( rs.next() );
    }

    @Test
    public void deleteFromIncorrectStorageFails() {
        SqliteStorage fakeStorage = new SqliteStorage(":/dippadappu/duu");
        assertFalse(fakeStorage.deleteProject(new ProjectInformation("Name","sqlite","12")));
    }
    
    @Test
    public void deleteProjectFailsWhenDbError() throws IOException, SQLException {
        SqliteStorage storage = new SqliteStorage(NEWFILENAME);
        Statement stmt = storage.getConnection().createStatement();
        stmt.execute("DROP TABLE Task");
        assertFalse(storage.deleteProject(new ProjectInformation("dii","daa","1")));
    }
    
    
}
