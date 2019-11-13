/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.domain;

import java.time.LocalDate;
import java.util.Date;

/**
 * Tehtävien Storage-luokkien kantaluokka
 * 
 * @author arto
 */
public abstract class AbstractTask implements Task {
    
    protected LocalDate date = LocalDate.now();
    protected String description = new String();
    protected int minutes = 0;    
    
    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getMinutes() {
        return this.minutes;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }    
    
}
