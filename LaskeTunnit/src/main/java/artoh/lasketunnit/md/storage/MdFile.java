/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.domain.Task;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arto
 */
public class MdFile {
    
    private MdProject project;
    
    private String beforeTable;
    private String tableHeader;
    private String sumTitle;
    private String afterTable;
    private DateTimeFormatter dateFormat;   
    
    public MdFile(MdProject project) {
        this.project = project;
        
        this.beforeTable = "# Työaikakirjanpito\n\n";
        this.tableHeader = "Päivä | Tunnit | Tehtävät \n";
        this.sumTitle = "Yhteensä";
        this.afterTable = "";
        
        this.dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        
    }

    final static int BEFORE = 0;
    final static int ROWS = 1;    
    final static int AFTER = 2;           
    
    
    boolean load(String filename) {        
        Pattern taskLinePattern = Pattern.compile("^(\\d{1,2}[.]\\d{1,2}[.]\\d{4})\\s*[|]\\s*(\\d+[,.]?\\d*)\\s*[|]\\s*(.*)");
           
        try {
            InputStream in = new FileInputStream(filename);
            Reader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader breader = new BufferedReader(reader);
            String row = null;
            int status = BEFORE;                        
            while ((row = breader.readLine()) != null) {
                if (status == BEFORE) {
                    if (row.contains("|")) {
                        tableHeader = row + "\n";
                        status = ROWS;
                    } else {
                        beforeTable = beforeTable + row + "\n";
                    }
                } else if (status == ROWS && !row.startsWith("---")) {
                    Matcher matcher = taskLinePattern.matcher(row);
                    if (matcher.matches()) {
                        loadRow(matcher.group(1), matcher.group(2), matcher.group(3));
                    } else {
                        if (row.contains("|")) {
                            sumTitle = row.substring(0, row.indexOf("|") - 1);
                        }
                        status = AFTER;
                    }                    
                } else if (status == AFTER) {
                    afterTable = afterTable + row + "\n";
                }                                
            }
            breader.close();
        } catch (IOException e) {
            return false;
        }
        return true;        
    }
    
    
   
    void loadRow(String date, String time, String description) {            
   
        LocalDate localdate = LocalDate.parse(date, dateFormat);
        int minutes = parseMinutes(time);
        
        Task task = project.createTask();
        task.setDate(localdate);
        task.setMinutes(minutes);
        task.setDescription(description);
    }
    
    int parseMinutes(String txt) {
        if (txt.contains(".")) {
            int hours = Integer.parseInt(txt.substring(0, txt.indexOf(".")));
            int minutes = Integer.parseInt(txt.substring(txt.indexOf(".") + 1));
            return hours * 60 + minutes;
        } else if (txt.contains(",")) {
            txt = txt.replace(",", ".");
            double hours = Double.parseDouble(txt);
            return (int) (hours * 60.0);
        } else {
            return Integer.parseInt(txt) * 60;
        }
    }
   
    boolean save(String filename) {
        
        try {
            OutputStream out = new FileOutputStream(filename);
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bwriter = new BufferedWriter(writer);
                        
            bwriter.write(this.beforeTable);            
            bwriter.write(this.tableHeader);         
            bwriter.write("----|-----|-----------------------\n");            
            
            for (String line : tasksToWrite()) {
                bwriter.write(line);         
            }
                        
            bwriter.write(sumrow());
            bwriter.write(afterTable);            
            bwriter.close();
            
        } catch (IOException ex) {
            return false;
        }
        
        return true;    
    }
    
    
    private List<String> tasksToWrite() {
        List<String> list = new ArrayList<>();
        for (Task task : project.allTasks()) {            
            list.add(String.format("%s | %s | %s\n", 
                    task.getDate().format(dateFormat),
                    hourString(task.getMinutes()),
                    task.getDescription()));
        }
        return list;
    }
    
    private String sumrow() {
        return String.format("%s | %s | \n\n", 
                sumTitle, hourString(project.sumMinutes()));
    }    

    static public String hourString(int minutes) {
        int hours = minutes / 60;
        int extraMinutes = minutes % 60;
        return String.format("%d.%02d", hours, extraMinutes);
    }    
    
}
