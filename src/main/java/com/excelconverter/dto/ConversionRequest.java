package com.excelconverter.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ConversionRequest {
    @NotNull(message = "Excel file is required")
    private MultipartFile file;

    @NotBlank(message = "Table name is required")
    private String tableName;

    private boolean includeHeaders = true;

    private String sheetName;

    private int startRow = 0;

    private int endRow = -1;
}