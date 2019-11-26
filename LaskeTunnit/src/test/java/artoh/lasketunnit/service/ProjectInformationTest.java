/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ahyvatti
 */
public class ProjectInformationTest {
    
    public ProjectInformationTest() {
    }
    
    @Test
    public void invalidInformationIsInvalid() {
        ProjectInformation info = new ProjectInformation();
        assertFalse(info.isValid());
    }
    
    @Test
    public void validInformationIsValid() {
        ProjectInformation info = new ProjectInformation("Test","test","Testiiing!");
        assertTrue(info.isValid());
    }
}
