package org.obehave.service;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.*;
import org.obehave.model.modifier.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Stefan Lamprecht
 *         TODO refactor
 */
public class MatrixExporter implements Exporter {
    private static final Logger log = LoggerFactory.getLogger(MatrixExporter.class);
    private final ExportDataWriter exportDataWriter;
    private final boolean summarize;

    public MatrixExporter(ExportDataWriter exportDataWriter, boolean summarize) {
        this.exportDataWriter = exportDataWriter;
        this.summarize = summarize;
    }

    @Override
    public void export(List<Observation> observations, List<Subject> subjects, List<Node> actionNodes) throws ServiceException {
        List<ExportData> exportDatas = new ArrayList<>();

        if (summarize) {
            for (Map.Entry<String, Table<String, String, Number>> e : aggregate(observations, subjects, actionNodes).entrySet()) {
                exportDatas.add(new ExportData<>(e.getKey(), "Summary", e.getValue()));
            }
        } else {
            for (Node n : actionNodes) {
                for (Map.Entry<String, Table<String, String, Number>> e : aggregate(observations, subjects, Collections.singletonList(n)).entrySet()) {
                    exportDatas.add(new ExportData<>(e.getKey(), n.getDisplayString(), e.getValue()));
                }
            }
        }

        exportDataWriter.write(exportDatas);
    }

    private Map<String, Table<String, String, Number>> aggregate(List<Observation> observations, List<Subject> subjects, List<Node> actions) throws ServiceException {
        final Map<String, Table<String, String, Number>> dataMap = new HashMap<>();
        final List<String> subjectCaptions = subjects.stream().map(Subject::toString).collect(Collectors.toList());

        final Table<String, String, Number> countTable = ArrayTable.create(subjectCaptions, subjectCaptions);
        final Table<String, String, Number> durationTable = ArrayTable.create(subjectCaptions, subjectCaptions);

        dataMap.put("Count", countTable);
        dataMap.put("Duration", durationTable);

        final List<Action> allValidActions = getAllActions(actions);

        for (Observation o : observations) {
            for (Coding c : o.getCodings()) {
                if (subjects.contains(c.getSubject()) && allValidActions.contains(c.getAction()) && isSubjectModifierInList(c, subjects)) {
                    final String subjectName = c.getSubject().getName();

                    String modifierSubjectName = subjectName;
                    if (c.getModifier() != null) {
                        modifierSubjectName = ((Subject) c.getModifier().get()).getName();
                    }

                    final Number countValue = countTable.get(subjectName, modifierSubjectName);
                    countTable.put(subjectName, modifierSubjectName, countValue.longValue() + 1);

                    if (!c.isOpen()) {
                        final Number durationValue = durationTable.get(subjectName, modifierSubjectName);
                        durationTable.put(subjectName, modifierSubjectName, durationValue.doubleValue() + c.getDuration());
                    }
                }
            }
        }

        return dataMap;
    }

    private List<Action> getAllActions(List<Node> actionNodes) {
        List<Action> actions = new ArrayList<>();

        for (Node actionNode : actionNodes) {
            if (actionNode.getDataType() == Action.class) {
                if (actionNode.getData() != null) {
                    // if actionNode contains an Action, add it to list
                    actions.add((Action) actionNode.getData());
                } else {
                    // if it's an action group, recursively resolve the actions of it's children
                    actions.addAll(getAllActions(actionNode.getChildren()));
                }
            }
        }

        return actions;
    }

    private void export(List<Subject> subjects, String actionName, Map<String, ArrayList<Long>> countsPerAction, Map<String, ArrayList<Long>> totalDurationPerAction) throws ServiceException {
        // ExportData erstellen


        // datawriter aufrufen
        XSSFWorkbook workbook = new XSSFWorkbook();
        generateExcelSheet(workbook, countsPerAction, subjects, "totalCounts");
        generateExcelSheet(workbook, totalDurationPerAction, subjects, "totalDuration");
        writeExcelSheetToDisk(workbook, actionName);
    }

    private boolean isSubjectModifierInList(Coding c, List<Subject> subjects) {
        return (c.getModifier().getType() == Modifier.Type.SUBJECT_MODIFIER) && subjects.contains(c.getModifier().get());
    }

    private void generateExcelSheet(XSSFWorkbook wb, Map<String, ArrayList<Long>> data, List<Subject> finalSubjects, String type) {

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
    }

    private void writeExcelSheetToDisk(XSSFWorkbook workbook, String title) throws ServiceException {

        String fileSeparator = System.getProperty("file.separator");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        String blank = " ";

        try {
            log.debug("Start writing Excel to {}", savePath.getAbsolutePath());
            //Write the workbook to the file system
            FileOutputStream out = new FileOutputStream(savePath + fileSeparator + dateFormat.format(new Date()) + blank + "Export" + blank + title + ".xlsx");
            workbook.write(out);
            out.close();
            log.debug("Excel successfully written to {}", savePath.getAbsolutePath());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


}
