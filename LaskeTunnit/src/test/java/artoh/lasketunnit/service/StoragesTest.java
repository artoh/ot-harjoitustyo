/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import artoh.lasketunnit.service.ProjectInformation;
import artoh.lasketunnit.storage.Storages;
import artoh.lasketunnit.md.storage.MdStorage;
import artoh.lasketunnit.projectlist.FileProjectList;
import artoh.lasketunnit.projectlist.ProjectList;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
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
public class StoragesTest {
    
    Storages storages;
    static final String LISTFILENAME = "/tmp/lasketunnitstoragelist.txt";
    
    static final String FILENAME = "/tmp/lasketunnitstoragenb1.md";
    static final String NEWFILENAME = "/tmp/lasketunnitstorageb2.md";    
    
    public StoragesTest() {
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
            
            java.nio.file.Files.deleteIfExists(Paths.get(NEWFILENAME));        
            
        } catch (IOException e) {
            fail("IO Exception");
        }         
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void incorrectStorageFailsWhenAdd() {
        assertNull( storages.addProject(new ProjectInformation("Test","duubadaa","diipiduu")) );
    }
    
    @Test
    public void incorrectStorageFailsWhenCreate() {
        assertNull( storages.createProject(new ProjectInformation("Test","duubadaa","diipiduu")) );
    }

    @Test
    public void incorrectStorageFailsWhenRemove() {
        assertFalse( storages.removeProject(new ProjectInformation("Test","duubadaa","diipiduu")) );
    }
    
    @Test
    public void loadFailsWhenNotExists() {
        assertNull(storages.addProject(new ProjectInformation("Test","md","/diipa/daadii")));
    }
    
    @Test
    public void loadSuccessWhenExists() {
        assertNotNull(storages.addProject(new ProjectInformation("Test","md",FILENAME)));
    }
    
    @Test
    public void  canCreateNewProject() {
        assertNotNull(storages.createProject(new ProjectInformation("Test","md",NEWFILENAME)));
    }
    
    @Test
    public void canDeleteProject() {
        assertTrue(storages.removeProject(new ProjectInformation("Test","md",FILENAME)));
    }
    
    @Test
    public void deleteUnexistsFails() {
        assertFalse(storages.removeProject(new ProjectInformation("Test","md",NEWFILENAME)));
    }
    
    @Test
    public void canHideProject() {
        assertTrue(storages.hideProject(new ProjectInformation("Test","md",FILENAME)));
    }
    
    @Test
    public void zeroProjectsBeforeAdd() {
        assertEquals(0,storages.allProjects().size());
    }
    
    @Test
    public void oneProjectAfterAdd() {
        storages.addProject(new ProjectInformation("Test","md",FILENAME));
        assertEquals(1,storages.allProjects().size());
    }
    
    @Test
    public void oneProjectAfterCreate() {
        assertNotNull(storages.createProject(new ProjectInformation("New","md",NEWFILENAME)));
        assertEquals(1,storages.allProjects().size());
    }
    
    @Test 
    public void twoProjectsAfterAddAndCreate() {
        assertNotNull(storages.addProject(new ProjectInformation("Test","md",FILENAME)));
        assertNotNull(storages.createProject(new ProjectInformation("New","md",NEWFILENAME)));
        assertEquals(2,storages.allProjects().size());
    }

    @Test
    public void createFailsWithIncorrectList() {
        ProjectList incorrect = new FileProjectList("/duupa/daa");
        Storages icstorages = new Storages(incorrect);
        icstorages.registerStorage(new MdStorage());
        assertNull(icstorages.createProject(new ProjectInformation("Test","md",NEWFILENAME)));
        
    }
    
    @Test
    public void createFailsWithIncorrectFilename() {        
        assertNull(storages.createProject(new ProjectInformation("Test","md","/diipadaa/duu")));
    }
    
    @Test
    public void addFailsWithIncorrectList() {
        ProjectList incorrect = new FileProjectList("/duupa/daa");
        Storages icstorages = new Storages(incorrect);
        icstorages.registerStorage(new MdStorage());
        assertNull(icstorages.addProject(new ProjectInformation("Test","md",FILENAME)));
    }
 
    @Test
    public void invalidStorageHasZeroProjects() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        final String listFileName = "/tmp/lasketunnitstoragelist.txt";
        OutputStream out = new FileOutputStream(listFileName);
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.write("Test");
        bwriter.write("test");
        bwriter.write("duupadaa");
        bwriter.close();
        
        ProjectList projectList = new FileProjectList(listFileName);
        Storages testStorages = new Storages(projectList);
        assertEquals(0, testStorages.allProjects().size());
    }
    
    @Test
    public void noExitsFilesHasZeroProjects() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        storages.addProject(new ProjectInformation("Test","md",FILENAME));
        Files.delete(Paths.get(FILENAME));
        assertEquals(0,storages.allProjects().size());
    }
       
}
