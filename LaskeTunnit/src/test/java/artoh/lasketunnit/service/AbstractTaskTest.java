/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.domain;

import artoh.lasketunnit.md.storage.MdTask;
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
public class AbstractTaskTest {
    
    public AbstractTaskTest() {
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
    public void canSetDate() {
        MdTask task = new MdTask(null);
        task.setDate(LocalDate.of(2019, 11, 13));
        assertEquals(2019, task.getDate().getYear());
        assertEquals(11, task.getDate().getMonthValue());
        assertEquals(13, task.getDate().getDayOfMonth());
    }
    
    @Test
    public void canSetDescription() {
        MdTask task = new MdTask(null);
        task.setDescription("This is a test");
        assertEquals("This is a test", task.getDescription() );
    }
    
    @Test
    public void canSetMinutes() {
        MdTask task = new MdTask(null);
        task.setMinutes(38);
        assertEquals(38, task.getMinutes());
    }  
}
