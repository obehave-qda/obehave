package org.obehave.service;

import com.google.common.collect.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.obehave.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelWriter implements ExportDataWriter {

    private static final Logger log = LoggerFactory.getLogger(ExcelWriter.class);
    private File path;

    public ExcelWriter(File path) {
        this.path = path;
    }

    @Override
    public void write(List<ExportData> data) throws ServiceException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        data.stream().forEach(s -> generateExcelSheet(s,workbook));
        writeExcelSheetToDisk(workbook, path);

    }

    private XSSFSheet generateExcelSheet(ExportData data, XSSFWorkbook workbook) {

        Table<String,String,Number> dataTable = data.getData();
        //Create a blank sheet and set name like the action
        XSSFSheet sheet = workbook.createSheet(data.getType()+" "+data.getCaption());

        //Iterate over data and write to sheet


        int rowNum = 0;
        int cellTitle = 0;
        //set matrix title from left to right
        Row rowTitle = sheet.createRow(rowNum++);
        Cell cellBlank = rowTitle.createCell(cellTitle++);
        cellBlank.setCellValue(" ");

        for (String s : dataTable.columnKeySet()) {

            Cell cell = rowTitle.createCell(cellTitle++);
            cell.setCellValue(s);
        }

        //set matrix title from top to bottom and set

        Map<String, Map<String,Number>>  rowMap = dataTable.rowMap();

        for (Map.Entry<String, Map<String,Number>> outer : rowMap.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            Cell title = row.createCell(cellNum++);
            title.setCellValue(outer.getKey());

            for (Map.Entry<String, Number> inner : outer.getValue().entrySet()) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                if (inner.getValue() != null){
                    cell.setCellValue(inner.getValue().doubleValue());
                } else
                    cell.setCellValue(0d);
            }
        }


        return sheet;
    }

    private void writeExcelSheetToDisk(XSSFWorkbook workbook, File savePath) throws ServiceException {

        String fileSeparator = System.getProperty("file.separator");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String blank = " ";

        try {
            log.debug("Start writing Excel to {}", savePath.getAbsolutePath());
            //Write the workbook to the file system
            FileOutputStream out = new FileOutputStream(savePath + fileSeparator + dateFormat.format(new Date()) + blank + "Export.xlsx");
            workbook.write(out);
            out.close();
            log.debug("Excel successfully written to {}", savePath.getAbsolutePath());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
