/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.sqlite.storage;

import artoh.lasketunnit.projectlist.FileProjectList;
import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.Task;
import artoh.lasketunnit.service.TasksService;
import artoh.lasketunnit.storage.Storages;
import java.io.IOException;
import java.nio.file.Paths;
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
public class SqliteIntegrationTest {    
    TasksService service;
    Project project1;
    Project project2;
    Storages storages;
    SqliteStorage sqliteStorage;
    
    static final String LISTFILENAME = "/tmp/sqlintegration.list";
    static final String SQLITEFILENAME = "/tmp/sqlintegration.sqlite";
    
    public SqliteIntegrationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {      
        java.nio.file.Files.deleteIfExists(Paths.get(LISTFILENAME));        
        java.nio.file.Files.deleteIfExists(Paths.get(SQLITEFILENAME));        
        
        sqliteStorage = new SqliteStorage(SQLITEFILENAME);
        
        storages = new Storages(new FileProjectList(LISTFILENAME));
        storages.registerStorage(sqliteStorage);
        service = new TasksService(storages);
        
        project1 = service.getStorages().createProject(new ProjectInformation("Project One","sqlite",""));       
        project2 = service.getStorages().createProject(new ProjectInformation("Project Two","sqlite",""));       
    }
    
    @After
    public void tearDown() {
    }    
    
    @Test
    public void createdProjectNotNulls() {
        assertNotNull(project1);
        assertNotNull(project2);
        assertEquals(sqliteStorage.storageId(), project1.getInformation().getStorageId());
    }
    
    @Test
    public void createProjectsSuccess() {
        service.refresh();
        assertEquals(2, service.allProjects().size());        
    }
    
    @Test
    public void createFirstTaskSuccess() {
        Task task = project1.createTask();
        task.setDate(LocalDate.of(2019, 5, 1));
        task.setMinutes(15);
        task.setDescription("Just coding...");
        task.save();
        
        service.refresh();
        assertEquals(1, project1.allTasks().size());
        assertEquals(15, project1.sumMinutes());
        assertEquals(1, service.allTasks().size());
    }
    
    /**
     * Melkoisen pitkä integraatiotesti, jolla testataan eri projekteissa
     * olevien tehtävien tallentuminen ja hakemisen lajittelu
     */
    @Test
    public void createThreeTasks() {
        Task task1 = project1.createTask();
        task1.setDate(LocalDate.of(2019, 12, 15));
        task1.setMinutes(25);
        task1.setDescription("Task One");
        task1.save();
        
        Task task2 = project1.createTask();
        task2.setDate(LocalDate.of(2019,12,17));
        task2.setMinutes(30);
        task2.setDescription("Task Two");
        task2.save();
        
        Task task3 = project2.createTask();
        task3.setDate(LocalDate.of(2019,12,16));
        task3.setMinutes(20);
        task3.setDescription("Task Three");        
        task3.save();
        
        service.refresh();
        
        assertEquals(2, service.allProjects().size());
        assertEquals(3, service.allTasks().size());
        assertEquals(55, project1.sumMinutes());
        assertEquals(2, project1.allTasks().size());
        assertEquals("Task Two", service.allTasks().get(0).getDescription());
        
        // Some updating        
        task2.setMinutes(60);
        task2.setDescription("Modified");
        task2.save();
        
        service.refresh();
        assertEquals(85, project1.sumMinutes());
        assertEquals("Modified", service.allTasks().get(0).getDescription());
    }
    
    
}
