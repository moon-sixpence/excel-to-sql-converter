package com.excelconverter.controller;

import com.excelconverter.dto.ConversionRequest;
import com.excelconverter.dto.ConversionResponse;
import com.excelconverter.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertExcelToSql(@ModelAttribute ConversionRequest request) {
        ConversionResponse response = excelService.convertToSql(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert/download")
    public ResponseEntity<byte[]> convertAndDownload(@ModelAttribute ConversionRequest request) {
        ConversionResponse response = excelService.convertToSql(request);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().build();
        }

        String filename = response.getTableName() + "_insert_statements.sql";
        byte[] content = response.getSqlStatements().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(content.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }
}