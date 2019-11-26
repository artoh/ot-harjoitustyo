/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import artoh.lasketunnit.md.storage.MdStorage;
import artoh.lasketunnit.projectlist.FileProjectList;
import artoh.lasketunnit.projectlist.ProjectList;
import artoh.lasketunnit.storage.Storages;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
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
public class TasksServiceTest {
    
    Storages storages;
    static final String LISTFILENAME = "/tmp/lasketunnit_taskservicetest_list.txt";
    
    static final String FILENAME = "/tmp/lasketunnit_taskservicetest_file.md";    
    
    public TasksServiceTest() {
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
        ProjectList projectlist = new FileProjectList(LISTFILENAME);
        storages = new Storages(projectlist);
        storages.registerStorage(new MdStorage());
        
        try {
            OutputStream out = new FileOutputStream(FILENAME);
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bwriter = new BufferedWriter(writer);
            bwriter.write("# My Project Hours Accounting \n\n");
            bwriter.write("I will wrote here my work statistics. \n\n");
            bwriter.write("Date | Time spend | What I have done\n");
            bwriter.write("-----|------------|-----------------\n");
            bwriter.write("01.06.2019 | 3.30 | Started the job \n");
            bwriter.write("18.07.2019 | 3,5 | More work \n");
            bwriter.write("22.10.2019 | 1 | One Final Hour \n");
            bwriter.write("Sum | 8.00 |  \n\n");
            bwriter.write("Some footnote stay here.");
            bwriter.close();

            storages.addProject(new ProjectInformation("Test","md",FILENAME));
            
        } catch (IOException e) {
            fail("IO Exception");
        }                  
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void noProjectsBeforeRefresh() {
        TasksService service = new TasksService(storages);        
        assertEquals(0,service.allProjects().size());
    }
    
    @Test
    public void oneProjectInService() {
        TasksService service = new TasksService(storages);
        service.refresh();
        assertEquals(1,service.allProjects().size());
    }
    
    @Test
    public void threeTasksInService() {
        TasksService service = new TasksService(storages);
        service.refresh();
        assertEquals(3,service.allTasks().size());
    }
    
    @Test
    public void storagesOfTaskIsCorrect() {
        TasksService service = new TasksService(storages);
        assertEquals(storages,service.getStorages());
    }
}
