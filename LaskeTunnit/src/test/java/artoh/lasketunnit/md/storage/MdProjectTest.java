/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.domain.ProjectInformation;
import artoh.lasketunnit.domain.Task;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
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
public class MdProjectTest {
    
    ProjectInformation incorrectInformation;
    
    public MdProjectTest() {        
        incorrectInformation = 
            new ProjectInformation("Incorrect","md","/duuba/daa/didityy");
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void emptyProjectHaventTasks() {
        MdProject project = new MdProject( incorrectInformation );
        assertEquals( 0, project.allTasks().size());
        
    }
    
    @Test
    public void oneTaskAdded() {
        MdProject project = new MdProject( incorrectInformation );
        project.createTask();
        assertEquals( 1, project.allTasks().size());
    }
    
    @Test
    public void twoAddedOneDeleted() {
        MdProject project = new MdProject( incorrectInformation );        
        Task task = project.createTask();
        project.createTask();
        task.delete();
        assertEquals( 1, project.allTasks().size());
    }
    
    @Test
    public void incorredFileInSaveFails() {
        MdProject project = new MdProject( incorrectInformation );        
        assertFalse( project.save());
    }
    
    @Test
    public void incorrectLoadFileFails() {
        MdProject project = new MdProject( incorrectInformation );        
        assertFalse( project.load());        
    }
    
}
