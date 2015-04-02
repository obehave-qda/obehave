package org.obehave.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.obehave.model.*;
import org.obehave.model.modifier.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Stefan Lamprecht
 */
public class ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportService.class);
    private File savePath;

    public ExportService(File savePath) {
        this.savePath = savePath;
    }

    public void exportActionGroup(List<Observation> observations, List<Subject> subjects, Node actionGroup) {
        // Map contains counts per actions by subject, LinkedHashMap to preserve order
        Map<String, ArrayList<Long>> countsPerAction = new LinkedHashMap<String, ArrayList<Long>>();
        Map<String, ArrayList<Long>> totalDurationPerAction = new LinkedHashMap<String, ArrayList<Long>>();

        // Initialize map key and values
        for (Subject s : subjects) {
            countsPerAction.put(s.getName(), new ArrayList<Long>(Collections.nCopies(subjects.size(), 0L)));
            totalDurationPerAction.put(s.getName(), new ArrayList<Long>(Collections.nCopies(subjects.size(), 0L)));
        }


        //Iterates over observations and counts interesting actions of a actiongroup node
        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {

                // is this any good really?
                Subject subject = c.getSubject();


                //stirring for right stuff
                if (isRequestedSubjectAndActionGroup(subjects, actionGroup, c, subject) && (c.getModifier() == null || c.getModifier().get().equals(subject))) {
                    Long value = countsPerAction.get(subject.getName()).get(subjects.indexOf(subject));
                    countsPerAction.get(subject.getName()).set(subjects.indexOf(subject),value+1);
                    Long duration = totalDurationPerAction.get(subject.getName()).get(subjects.indexOf(subject));
                    totalDurationPerAction.get(subject.getName()).set(subjects.indexOf(subject),duration+c.getDuration());

                } else if (isRequestedSubjectAndActionGroup(subjects, actionGroup, c, subject) && isRequestedSubjectModifier(c, subjects)) {
                    long value = countsPerAction.get(subject.getName()).get(subjects.indexOf(c.getModifier().get()));
                    countsPerAction.get(subject.getName()).set(subjects.indexOf(c.getModifier().get()),value+1);
                    Long duration = totalDurationPerAction.get(subject.getName()).get(subjects.indexOf(c.getModifier().get()));
                    totalDurationPerAction.get(subject.getName()).set(subjects.indexOf(c.getModifier().get()),duration+c.getDuration());
                }
            }
        }




        export(subjects, actionGroup.getDisplayString(), countsPerAction, totalDurationPerAction);
    }

    public void exportAction(List<Observation> observations, List<Subject> subjects, Action action) {


        // Map contains counts per actions by subject, LinkedHashMap to preserve order
        Map<String, ArrayList<Long>> countsPerAction = new LinkedHashMap<String, ArrayList<Long>>();
        Map<String, ArrayList<Long>> totalDurationPerAction = new LinkedHashMap<String, ArrayList<Long>>();

        // Initialize map key and values
        for (Subject s : subjects) {
            countsPerAction.put(s.getName(), new ArrayList<Long>(Collections.nCopies(subjects.size(), 0L)));
            totalDurationPerAction.put(s.getName(), new ArrayList<Long>(Collections.nCopies(subjects.size(), 0L)));
        }


        //Iterates over observations and counts interesting action
        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {

                // is this any good really?
                Subject subject = c.getSubject();


                //stirring for right stuff
                if (isRequestedSubjectAndAction(subjects, action, c, subject) && (c.getModifier() == null || c.getModifier().get().equals(subject))) {
                    Long value = countsPerAction.get(subject.getName()).get(subjects.indexOf(subject));
                    countsPerAction.get(subject.getName()).set(subjects.indexOf(subject),value+1);
                    Long duration = totalDurationPerAction.get(subject.getName()).get(subjects.indexOf(subject));
                    totalDurationPerAction.get(subject.getName()).set(subjects.indexOf(subject),duration+c.getDuration());

                } else if (isRequestedSubjectAndAction(subjects, action, c, subject) && isRequestedSubjectModifier(c, subjects)) {
                    long value = countsPerAction.get(subject.getName()).get(subjects.indexOf(c.getModifier().get()));
                    countsPerAction.get(subject.getName()).set(subjects.indexOf(c.getModifier().get()),value+1);
                    Long duration = totalDurationPerAction.get(subject.getName()).get(subjects.indexOf(c.getModifier().get()));
                    totalDurationPerAction.get(subject.getName()).set(subjects.indexOf(c.getModifier().get()),duration+c.getDuration());
                }
            }
        }

        export(subjects, action.getDisplayString(), countsPerAction, totalDurationPerAction);

    }

    private void export(List<Subject> subjects, String actionName, Map<String, ArrayList<Long>> countsPerAction, Map<String, ArrayList<Long>> totalDurationPerAction) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        generateExcelSheet(workbook, countsPerAction, actionName, subjects, "totalCounts");
        generateExcelSheet(workbook, totalDurationPerAction, actionName, subjects, "totalDuration");
        writeExcelSheetToDisk(workbook, actionName);
    }

    private boolean isRequestedSubjectAndAction(List<Subject> subjects, Action action, Coding c, Subject subject) {
        return subjects.contains(subject) && c.getAction().equals(action);
    }

    private boolean isRequestedSubjectAndActionGroup(List<Subject> subjects, Node actionGroup, Coding c, Subject subject) {
        return subjects.contains(subject) && actionGroup.contains(c.getAction());
    }

    private boolean isRequestedSubjectModifier(Coding c, List<Subject> subjects) {
        return (c.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) && subjects.contains(c.getModifier().get());
    }

    private XSSFSheet generateExcelSheet(XSSFWorkbook wb, Map<String, ArrayList<Long>> data, String action, List<Subject> finalSubjects, String type) {

        //Create a blank sheet and set name like the action
        XSSFSheet sheet = wb.createSheet(type);

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();

        int rownum = 0;
        int cellnumtitle = 0;
        //set matrix title from left to right
        //TODO Set cell font color like subject color
        Row rowtitle = sheet.createRow(rownum++);
        Cell cellBlank = rowtitle.createCell(cellnumtitle++);
        cellBlank.setCellValue(" ");
        for (Subject subject : finalSubjects) {

            Cell cell = rowtitle.createCell(cellnumtitle++);
            cell.setCellValue(subject.getName());
        }

        //set matrix title from top to bottom and set values
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            ArrayList<Long> longArray = data.get(key);
            int cellnum = 0;

            //title
            Cell title = row.createCell(cellnum++);
            title.setCellValue(key);

            //values
            for (Long values : longArray) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(values);
            }
        }
        return sheet;

    }

    public void writeExcelSheetToDisk(XSSFWorkbook workbook, String title) {

        String fileSeparator = System.getProperty("file.separator");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        String blank = " ";

        try {
            log.debug("Start writing Excel to {}", savePath.getAbsolutePath());
            //Write the workbook to the file system
            FileOutputStream out = new FileOutputStream(savePath + fileSeparator +  dateFormat.format(new Date()) + blank + "Export" + blank + title+".xlsx");
            workbook.write(out);
            out.close();
            log.debug("Excel successfully written to {}", savePath.getAbsolutePath());
        } catch (Exception e) {
            log.debug("Something has gone terribly wrong: {}", e.getMessage(), e);
        }
    }


}
