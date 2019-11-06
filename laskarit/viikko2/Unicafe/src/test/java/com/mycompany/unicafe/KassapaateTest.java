/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

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
public class KassapaateTest {
    
    Kassapaate kassa;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void kassassaRahaaAlussa() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void eiLounaitaMyytyAlussa() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());        
    }
    
    @Test
    public void ostetaanEdullinenLounas() {
        assertEquals( 60, kassa.syoEdullisesti(300));
        assertEquals(100240, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
    }

    @Test
    public void rahaEiRiitaEdulliseenLounaaseen() {
        assertEquals( 230, kassa.syoEdullisesti(230));
        assertEquals( 100000, kassa.kassassaRahaa());
        assertEquals( 0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
    }

    @Test
    public void ostetaanMaukasLounas() {
        assertEquals( 50, kassa.syoMaukkaasti(450));
        assertEquals(100400, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());        
        assertEquals( 0, kassa.edullisiaLounaitaMyyty());
    }
    
@Test
    public void ostetaanMaukasLounasTasarahalla() {
        assertEquals( 0, kassa.syoMaukkaasti(400));
        assertEquals(100400, kassa.kassassaRahaa());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());        
        assertEquals( 0, kassa.edullisiaLounaitaMyyty());
    }    
    
    @Test
    public void rahaEiRiitaMaukkaaseenLounaaseen() {
        assertEquals( 390, kassa.syoMaukkaasti(390));
        assertEquals( 100000, kassa.kassassaRahaa());
        assertEquals( 0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
    }
    
    @Test
    public void ostetaanEdullinenLounasKortilla() {
        Maksukortti kortti = new Maksukortti(500);
        assertTrue( kassa.syoEdullisesti(kortti));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
        assertEquals(260, kortti.saldo());
    }

    @Test
    public void rahaEiRiitaEdulliseenLounaaseenKortilla() {
        Maksukortti kortti = new Maksukortti(239);
        assertFalse( kassa.syoEdullisesti(kortti));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
        assertEquals(239, kortti.saldo());
    }

    @Test
    public void ostetaanEdullinenLounasKortillaKunTasmaRahaa() {
        Maksukortti kortti = new Maksukortti(240);
        assertTrue( kassa.syoEdullisesti(kortti));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
        assertEquals(0, kortti.saldo());
    }
    
@Test
    public void ostetaanMaukasLounasKortilla() {
        Maksukortti kortti = new Maksukortti(500);
        assertTrue( kassa.syoMaukkaasti(kortti));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());        
        assertEquals(100, kortti.saldo());
    }    

    @Test
    public void rahaEiRiitaMaukkaaseenLounaaseenKortilla() {
        Maksukortti kortti = new Maksukortti(380);
        assertFalse( kassa.syoMaukkaasti(kortti));
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());        
        assertEquals(380, kortti.saldo());
    }

    @Test
    public void ladataanRahaaKortille() {
        Maksukortti kortti = new Maksukortti(100);        
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals( 600, kortti.saldo());
        assertEquals( 100500, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivistaSummaa() {
        Maksukortti kortti = new Maksukortti(600);
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals( 600, kortti.saldo());
        assertEquals( 100000, kassa.kassassaRahaa());
    }
}


