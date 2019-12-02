/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Tehtävien abstrakti kantaluokka
 * 
 * Tällä tasolla on toteutettu tehtävien tietojen ylläpitäminen.
 * 
 * @author arto
 */
public abstract class AbstractTask implements Task {
    
    protected LocalDate date = LocalDate.now();
    protected String description = new String();
    protected int minutes = 0;        
    protected final DateTimeFormatter dateFormat;

    public AbstractTask() {
        this.dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }
    
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
    
    @Override
    public String getProjectName() {
        return getProject().getName();
    }
    
    @Override
    public String getDateString() {
        return getDate().format(dateFormat);
    }
    
    @Override
    public String getHourString() {
        return hourString(getMinutes());
    }
    
    /**
     * Muuntaa minuutit tuntia.minuuttia-muotoon
     * 
     * @param minutes Minuutit
     * @return Kesto tunteina ja minuutteina merkkijonona
     */
    static public String hourString(int minutes) {
        int hours = minutes / 60;
        int extraMinutes = minutes % 60;
        return String.format("%d.%02d", hours, extraMinutes);
    }       
}
