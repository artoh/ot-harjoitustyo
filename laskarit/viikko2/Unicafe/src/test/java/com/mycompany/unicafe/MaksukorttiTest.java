package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void lataaminenKasvattaaSaldoa() {
        kortti.lataaRahaa(500);
        assertEquals(510, kortti.saldo());
    }
    
    @Test
    public void otaRahaaVahentaaSaldoa() {
        kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiTarpeeksi() {
        kortti.otaRahaa(15);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void palauttaaTrueJosRahaRiittaa() {
        assertTrue(  kortti.otaRahaa(7));
    }
    
    @Test
    public void palauttaaFalseJosRahaEiRiita() {
        assertFalse( kortti.otaRahaa(17));
    }
    
    @Test
    public void toStringAlussa() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void toStringKymmenenEuroa() {
        kortti.lataaRahaa(990);
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void toStringSaldoKymmenenEuroaKymmenenSenttia() {
        kortti.lataaRahaa(1000);
        assertEquals("saldo: 10.10", kortti.toString());
    }
    
}
