package com.excelconverter.dto;


import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ConversionResponse {
    private boolean success;
    private String message;
    private String sqlStatements;
    private int totalRows;
    private String tableName;
}