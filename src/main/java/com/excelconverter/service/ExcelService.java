package com.excelconverter.service;

import com.excelconverter.dto.ConversionRequest;
import com.excelconverter.dto.ConversionResponse;

public interface ExcelService {
    ConversionResponse convertToSql(ConversionRequest request);
}