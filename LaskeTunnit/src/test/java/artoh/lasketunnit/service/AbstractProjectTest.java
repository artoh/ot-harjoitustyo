/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.service.AbstractProject;
import artoh.lasketunnit.service.Task;
import artoh.lasketunnit.md.storage.MdProject;
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
public class AbstractProjectTest {
    ProjectInformation testProject;
    
    public AbstractProjectTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testProject = new ProjectInformation("Test Project","test", "Test Data");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void setProjectInformation() {
        AbstractProject project = new MdProject(testProject);
        assertEquals( testProject, project.getInformation());
        ProjectInformation second = 
                new ProjectInformation("Another","second","ABCDEFG");
        project.setProjectInformation(second);
        assertEquals( second, project.getInformation());
    }
    
    @Test
    public void initialProjectZeroMinutes() {
        AbstractProject project = new MdProject(testProject);
        assertEquals(0, project.sumMinutes());
    }
    
    @Test
    public void readProjectName() {
        AbstractProject project = new MdProject(testProject);
        assertEquals( "Test Project", project.getName());
        ProjectInformation second = 
                new ProjectInformation("Another","second","ABCDEFG");
        project.setProjectInformation(second);
        assertEquals( "Another", project.getName());
    }
    
    @Test
    public void sumWithTasks() {
        AbstractProject project = new MdProject(testProject);
        Task task = project.createTask();
        task.setMinutes(10);
        
        Task another = project.createTask();
        another.setMinutes(20);
        
        assertEquals(30, project.sumMinutes());
    }        
    
    @Test
    public void sumStringWithTasks() {
        AbstractProject project = new MdProject(testProject);
        Task task = project.createTask();
        task.setMinutes(50);
        
        Task another = project.createTask();
        another.setMinutes(20);
        
        assertEquals("1.10", project.getSumHoursString());
    }      
    
    @Test
    public void toStringisName() {
        AbstractProject project = new MdProject(testProject);
        assertEquals("Test Project", project.toString());
    }

}
