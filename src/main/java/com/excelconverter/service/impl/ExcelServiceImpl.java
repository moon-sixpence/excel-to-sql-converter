package com.excelconverter.service.impl;

import com.excelconverter.dto.ConversionRequest;
import com.excelconverter.dto.ConversionResponse;
import com.excelconverter.service.ExcelService;
import com.excelconverter.util.ExcelReader;
import com.excelconverter.util.SqlGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public ConversionResponse convertToSql(ConversionRequest request) {
        try {
            MultipartFile file = request.getFile();

            // Validate file
            if (file.isEmpty()) {
                return ConversionResponse.builder()
                        .success(false)
                        .message("File is empty")
                        .build();
            }

            // Read Excel data
            List<List<String>> data = ExcelReader.readExcel(
                    file,
                    request.getSheetName(),
                    request.getStartRow(),
                    request.getEndRow()
            );

            // Generate SQL statements
            String sqlStatements = SqlGenerator.generateInsertStatements(
                    request.getTableName(),
                    data,
                    request.isIncludeHeaders()
            );

            return ConversionResponse.builder()
                    .success(true)
                    .message("Conversion successful")
                    .sqlStatements(sqlStatements)
                    .totalRows(data.size())
                    .tableName(request.getTableName())
                    .build();

        } catch (IOException e) {
            return ConversionResponse.builder()
                    .success(false)
                    .message("Error reading Excel file: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return ConversionResponse.builder()
                    .success(false)
                    .message("Error during conversion: " + e.getMessage())
                    .build();
        }
    }
}