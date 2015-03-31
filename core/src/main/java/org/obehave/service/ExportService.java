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

    }

    public void exportAction(List<Observation> observations, List<Subject> subjects, Action action) {

        //keep index of subjects in sync with maps
        final List<Subject> finalSubjects = subjects;

        // Map contains counts per actions by subject, LinkedHashMap to preserve order
        Map<String,Integer[]> countsPerAction = new LinkedHashMap<String, Integer[]>();
        Map<String,Long[]> totalDurationPerAction = new LinkedHashMap<String, Long[]>();

        // Initialize map key and values
        for (Subject s : finalSubjects){
            countsPerAction.put(s.getName(), new Integer[finalSubjects.size()]);
        }


        //Iterates over observations and counts interesting action
        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {

                // is this any good really?
                String subjectName = c.getSubject().getName();
                Integer[] counts = countsPerAction.get(subjectName);
                Long[] durations = totalDurationPerAction.get(subjectName);
                Long duration = c.getDuration();

                //stirring for right stuff
                if (isRequestedSubjectAndAction(finalSubjects, action, c, subjectName) && isRequestedSubjectModifier(c, finalSubjects)) {
                    counts[finalSubjects.indexOf(c.getModifier().get())] += 1;
                    durations[finalSubjects.indexOf(c.getModifier().get())] += duration;
                } else if (isRequestedSubjectAndAction(finalSubjects, action, c, subjectName)  && c.getModifier()==null) {
                    counts[finalSubjects.indexOf(subjectName)] += 1;
                    durations[finalSubjects.indexOf(subjectName)] += duration;
                }
            }
        }
    }

    private boolean isRequestedSubjectAndAction(List<Subject> subjects, Action action, Coding c, String subjectName) {
        return subjects.contains(subjectName) && c.getAction().equals(action);
    }

    private boolean isRequestedSubjectModifier(Coding c, List<Subject> subjects) {
        return subjects.contains(((c.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) && subjects.contains(c.getModifier().get())));
    }

    private XSSFSheet generateExcelSheet(Map<String, Integer[]> countsPerAction, Action action, List<Subject> finalSubjects){

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet and set name like the action
        XSSFSheet sheet = workbook.createSheet(action.getName());

        //Iterate over data and write to sheet
        Set<String> keyset = countsPerAction.keySet();

        int rownum = 0;

        //set matrix title from left to right
        //TODO Set cell font color like subject color
        Row row =sheet.createRow(rownum++);
        for (Subject subject : finalSubjects){
            int cellNum = 1;
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(subject.getName());
        }

        //set matrix title from top to bottom and set values
        for (String key : keyset)
        {
            sheet.createRow(rownum++);
            Integer[] intArr = countsPerAction.get(key);
            int cellnum = 0;

            //title
            Cell title = row.createCell(cellnum++);
            title.setCellValue(key);

            //values
            for (Integer values : intArr)
            {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(values);
//                if(values instanceof String)
//                    cell.setCellValue((String)values);
//                else if(values instanceof Integer)
//                    cell.setCellValue((Integer)values);
            }
        }
        return null;
    }

    public void writeExcelSheetToDisk(XSSFWorkbook workbook, File savePath){
        try
        {
            log.debug("Start writing Excel to {}", savePath.getAbsolutePath());
            //Write the workbook to the file system
            FileOutputStream out = new FileOutputStream(savePath);
            workbook.write(out);
            out.close();
            log.debug("Excel successfully written to {}", savePath.getAbsolutePath());
        }
        catch (Exception e)
        {
            log.debug("Something has gone terribly wrong: {}", e.getMessage(),e);
            e.printStackTrace();
        }
    }


}
