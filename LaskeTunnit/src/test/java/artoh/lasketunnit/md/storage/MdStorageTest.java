/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.service.Project;
import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.storage.Storage;
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
public class MdStorageTest {
    
    static final String FILENAME = "/tmp/lasketunnitmdstorage1.md";
    static final String NEWFILENAME = "/tmp/lasktunnitmdstorage2.md";
    
    public MdStorageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
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
            
            java.nio.file.Files.deleteIfExists(Paths.get(NEWFILENAME));        
            
        } catch (IOException e) {
            fail("IO Exception");
        }           
        
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void storageIdisCorrect() {
        Storage storage = new MdStorage();
        assertEquals("md", storage.storageId());
    }
    
    @Test
    public void addFailsIfFileNotExits() {
        Storage storage = new MdStorage();
        ProjectInformation incorrect = new ProjectInformation("Try to find me!",
            "md","/usr/dippiduppadippa/never.md");
        assertNull(storage.loadProject(incorrect));
    }
    
    @Test
    public void addSuccessIfExists() {
        Storage storage = new MdStorage();
        ProjectInformation correct = new ProjectInformation("Project","md",
            FILENAME);
        assertNotNull(storage.loadProject(correct));
    }
    
    @Test
    public void createNewProject() {
        Storage storage = new MdStorage();
        ProjectInformation info = new ProjectInformation("New","md",
            NEWFILENAME);
        Project project = storage.createProject(info);
        assertEquals("New", project.getInformation().getName());
    }
    
    @Test
    public void deleteExistingProject() {
        Storage storage = new MdStorage();
        ProjectInformation correct = new ProjectInformation("Project","md",
            FILENAME);
        assertTrue(storage.deleteProject(correct));
    }
    
    @Test
    public void cantDeleteInexistingProject() {
        Storage storage = new MdStorage();
        ProjectInformation incorrect = new ProjectInformation("Project","md",
            NEWFILENAME);
        assertFalse(storage.deleteProject(incorrect));        
    }
   
    
}
