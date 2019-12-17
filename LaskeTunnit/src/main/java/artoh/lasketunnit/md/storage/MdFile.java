package artoh.lasketunnit.md.storage;

import artoh.lasketunnit.service.AbstractTask;
import artoh.lasketunnit.service.Task;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown-tiedoston käsittely
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
    private final Pattern taskLinePattern = Pattern.compile("^(\\d{2}[.]\\d{2}[.]\\d{4})\\s*[|]\\s*(\\d+[,.]?\\d*)\\s*[|]\\s*(.*)");

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
    final static int ERROR = 9;

    /**
     * Lataa md-tiedoston
     *
     * @param filename
     * @return Onnistuiko
     */
    boolean load(String filename) {

        try {
            InputStream in = new FileInputStream(filename);
            Reader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader breader = new BufferedReader(reader);
            String row = null;
            int status = BEFORE;
            beforeTable = "";
            while ((row = breader.readLine()) != null) {
                status = loadRow(row, status);
                if (status == ERROR) {
                    return false;
                }
            }
            breader.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private int loadRowBefore(String row) {
        if (row.contains("|")) {
            tableHeader = row + "\n";
            return ROWS;
        } else {
            beforeTable = beforeTable + row + "\n";
            return BEFORE;
        }
    }

    private int loadTableRow(String row) {
        Matcher matcher = taskLinePattern.matcher(row);
        if (matcher.matches()) {
            return loadTaskRow(matcher.group(1), matcher.group(2), matcher.group(3));
        } else {
            if (row.startsWith("---")) {
                return ROWS;
            } else if (row.contains("|")) {
                sumTitle = row.substring(0, row.indexOf("|") - 1);
            }
            return AFTER;
        }
    }

    private int loadRowAfter(String row) {
        if (row.contains("|")) {
            return ERROR;
        } else {
            afterTable = afterTable + row + "\n";
            return AFTER;
        }
    }

    private int loadRow(String row, int status) {
        if (status == BEFORE) {
            return loadRowBefore(row);
        } else if (status == ROWS) {
            return loadTableRow(row);
        } else  {
            return loadRowAfter(row);
        }        
    }

    private int loadTaskRow(String date, String time, String description) {

        try {
            LocalDate localdate = LocalDate.parse(date, dateFormat);
            int minutes = parseMinutes(time);

            Task task = project.createTask();
            task.setDate(localdate);
            task.setMinutes(minutes);
            task.setDescription(description);
            return ROWS;
        } catch (DateTimeParseException e) {
            return ERROR;
        }
    }

    private int parseMinutes(String txt) {
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

    /**
     * Tallettaa md-tiedoston
     *
     * @param filename
     * @return Onnistuiko
     */
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
                    AbstractTask.hourString(task.getMinutes()),
                    task.getDescription()));
        }
        return list;
    }

    private String sumrow() {
        return String.format("%s | %s | \n\n",
                sumTitle, AbstractTask.hourString(project.sumMinutes()));
    }

}
