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
 * TODO: refactor
 * @author Stefan Lamprecht
 */
public class ExcelExporter {
    private static final Logger log = LoggerFactory.getLogger(ExcelExporter.class);
    private File savePath;

    public ExcelExporter(File savePath) {
        this.savePath = savePath;
    }

    public void exportActionGroup(List<Observation> observations, List<Subject> subjects, Node actionGroup) {

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
                if (isRequestedSubjectAndAction(subjects, action, c, subject) && c.getModifier() == null) {
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

        XSSFWorkbook workbook = new XSSFWorkbook();
        generateExcelSheet(workbook, countsPerAction, action, subjects, "totalCounts");
        generateExcelSheet(workbook, totalDurationPerAction, action, subjects, "totalDuration");
        writeExcelSheetToDisk(workbook, action);

    }

    private boolean isRequestedSubjectAndAction(List<Subject> subjects, Action action, Coding c, Subject subject) {
        return subjects.contains(subject) && c.getAction().equals(action);
    }

    private boolean isRequestedSubjectModifier(Coding c, List<Subject> subjects) {
        return subjects.contains(((c.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) && subjects.contains(c.getModifier().get())));
    }

    private XSSFSheet generateExcelSheet(XSSFWorkbook wb, Map<String, ArrayList<Long>> data, Action action, List<Subject> finalSubjects, String type) {

        //Create a blank sheet and set name like the action
        XSSFSheet sheet = wb.createSheet(action.getName() + "-" + type);

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();

        int rownum = 0;

        //set matrix title from left to right
        //TODO Set cell font color like subject color
        Row row = sheet.createRow(rownum++);
        for (Subject subject : finalSubjects) {
            int cellNum = 1;
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(subject.getName());
        }

        //set matrix title from top to bottom and set values
        for (String key : keyset) {
            sheet.createRow(rownum++);
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
        return null;
    }

    public void writeExcelSheetToDisk(XSSFWorkbook workbook, Action action) {

        String fileSeparator = System.getProperty("file.separator");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String blank = " ";

        try {
            log.debug("Start writing Excel to {}", savePath.getAbsolutePath());
            //Write the workbook to the file system
            FileOutputStream out = new FileOutputStream(savePath + fileSeparator + "Export" + blank + dateFormat.format(new Date()) + blank + action.getName());
            workbook.write(out);
            out.close();
            log.debug("Excel successfully written to {}", savePath.getAbsolutePath());
        } catch (Exception e) {
            log.debug("Something has gone terribly wrong: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }


}
