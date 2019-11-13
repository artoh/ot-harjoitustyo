/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.domain.ProjectInformation;
import artoh.lasketunnit.domain.Task;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
public class MdFileTest {
    
    ProjectInformation correctInformation;   
    static final String FILENAME = "/tmp/lasketunnitstorage1.md";
    static final String SECONDFILENAME = "/tmp/lasketunnitstorage2.md";
    static final String SAVEFILENAME = "/tmp/lasketunnitstorage3.md";
    static final String SECONDSAVEFILENAME = "/tmp/lasketunnitstorage4.md";
    
    public MdFileTest() {
        correctInformation = 
            new ProjectInformation("Correct","md", SAVEFILENAME);        
    }
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        java.nio.file.Files.deleteIfExists(Paths.get(SECONDSAVEFILENAME));            
        java.nio.file.Files.deleteIfExists(Paths.get(SAVEFILENAME));        
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
            
            
        } catch (IOException e) {
            fail("IO Exception");
        }     
        
        try {
            OutputStream out = new FileOutputStream(SECONDFILENAME);
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bwriter = new BufferedWriter(writer);
            bwriter.write("# Some Little Broken File \n\n");
            bwriter.write("I Hope Not To Break Everything. \n\n");
            bwriter.write("Date | Time spend | What I have done\n");
            bwriter.write("-----|------------|-----------------\n");
            bwriter.write("18.10.2019 | 0.05 | Jippiajee! \n");
            bwriter.write("11.07.2019 | 0.07 | More work \n");
            bwriter.write("Some \n More \n footnotes stay here.");
            bwriter.close();
            
            
        } catch (IOException e) {
            fail("IO Exception");
        }         
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void saveFile() throws IOException {
        MdProject project = new MdProject( correctInformation );        
        Task task = project.createTask();
        task.setDate(LocalDate.of(2019, 11, 13));
        task.setDescription("Test Description");
        task.setMinutes(75);
        Task anothertask = project.createTask();
        anothertask.setDate(LocalDate.of(2019, 11, 14));
        anothertask.setDescription("Another Test Description");
        anothertask.setMinutes(25);
        
        MdFile file = new MdFile(project);
        assertTrue( file.save(correctInformation.getStorageInfo() ));
        
        String contents = new String(Files.readAllBytes(Paths.get(SAVEFILENAME)));
        assertTrue( contents.contains("13.11.2019 | 1.15 | Test Description"));
        assertTrue( contents.contains("14.11.2019 | 0.25 | Another Test Description"));
        assertTrue( contents.contains("Yhteensä | 1.40"));        
    }
    
    @Test
    public void loadFile() {
        MdProject project = new MdProject( new ProjectInformation("My Project",
            "md",FILENAME) );
        
        MdFile file = new MdFile(project);
        file.load(FILENAME);
        
        assertEquals(8*60,project.sumMinutes());
    }

    @Test
    public void loadAnotherFile() {
        MdProject project = new MdProject( new ProjectInformation("My Project",
            "md",SECONDFILENAME ) );
        
        MdFile file = new MdFile(project);
        file.load(SECONDFILENAME);
        
        assertEquals(12,project.sumMinutes());
    }
    
    @Test
    public void loadAndSaveFile() throws IOException {
        MdProject project = new MdProject( new ProjectInformation("My Project",
            "md",FILENAME) );
        
        MdFile file = new MdFile(project);
        assertTrue(file.load(FILENAME));                
        assertEquals(8*60,project.sumMinutes());                
        assertTrue(file.save(SECONDSAVEFILENAME));
        
        String contents = new String(Files.readAllBytes(Paths.get(SECONDSAVEFILENAME)));
        assertTrue( contents.contains("# My Project Hours Accounting"));
        assertTrue( contents.contains("Date | Time spend | What I have done"));
        assertTrue( contents.contains("18.07.2019 | 3.30 | More work"));
        assertTrue( contents.contains("Sum | 8.00"));          
        assertTrue( contents.contains("Some footnote stay here."));
        
    }
    
}
