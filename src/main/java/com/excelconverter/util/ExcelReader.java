package com.excelconverter.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<List<String>> readExcel(MultipartFile file, String sheetName,
                                               int startRow, int endRow) throws IOException {
        Workbook workbook = createWorkbook(file);
        Sheet sheet = getSheet(workbook, sheetName);

        List<List<String>> data = new ArrayList<>();

        int firstRow = startRow;
        int lastRow = endRow == -1 ? sheet.getLastRowNum() : Math.min(endRow, sheet.getLastRowNum());

        int columnSize = 0;
        for (int i = firstRow; i <= lastRow; i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                if(i== 0){
                    columnSize = row.getLastCellNum();
                }
                List<String> rowData = new ArrayList<>();
                for(int cellIndex=0; cellIndex<columnSize;cellIndex++){
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.add(getCellValueAsString(cell));
                }

                data.add(rowData);
            }
        }

        workbook.close();
        return data;
    }

    private static Workbook createWorkbook(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else if (fileName.endsWith(".xls")) {
            return new HSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported.");
        }
    }

    private static Sheet getSheet(Workbook workbook, String sheetName) {
        if (sheetName != null && !sheetName.isEmpty()) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
            }
            return sheet;
        }
        return workbook.getSheetAt(0);
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}