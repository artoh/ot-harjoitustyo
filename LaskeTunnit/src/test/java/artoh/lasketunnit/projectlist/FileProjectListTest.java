package artoh.lasketunnit.projectlist;

import artoh.lasketunnit.domain.ProjectInformation;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
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
public class FileProjectListTest {
    
    static final String FILENAME = "/tmp/lasketunnittest1.txt";
    
    public FileProjectListTest() {
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
            bwriter.write("Test Project");
            bwriter.newLine();
            bwriter.write("teststorage");
            bwriter.newLine();
            bwriter.write("test information 123456789");
            bwriter.newLine();
            bwriter.close();
        } catch (IOException e) {
            fail("IO Exception");
        }         
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testFileReadedOneProject() {
        FileProjectList list = new FileProjectList(FILENAME);
        List<ProjectInformation> projects = list.getProjects();
        assertEquals(1,projects.size());
    }
    
    @Test
    public void noProjectsIfFileNotFound() {
        FileProjectList list = new FileProjectList("/dippadappa/dippadappa.dii");
        List<ProjectInformation> projects = list.getProjects();
        assertEquals(0,projects.size());
    }
    
    @Test
    public void twoProjectsAfterAdd() {
        FileProjectList list = new FileProjectList(FILENAME);
        assertEquals(1,list.getProjects().size());
        assertTrue(list.addProject(new ProjectInformation("Other name", "Test Storage",
            "Some Information ABCDEFG")));
        assertEquals(2,list.getProjects().size());
    }
    
    @Test 
    public void oneAfterAddAndRemove() {
        FileProjectList list = new FileProjectList(FILENAME);
        assertEquals(1,list.getProjects().size());
        ProjectInformation info = new ProjectInformation("Project For Add", "Test Storage",
            "Some Information ABCDEFG");
        assertTrue(list.addProject(info));
        assertEquals(2,list.getProjects().size());
        
        list.removeProject(info);
        assertEquals(1,list.getProjects().size());
    }

    @Test
    public void addFailsIfBadFilename() {
        FileProjectList list = new FileProjectList("/dippadappa/dippadappa.dii");
        ProjectInformation info = new ProjectInformation("Project For Add", "Test Storage",
            "Some Information ABCDEFG");
        assertFalse(list.addProject(info));
    }
    
}
