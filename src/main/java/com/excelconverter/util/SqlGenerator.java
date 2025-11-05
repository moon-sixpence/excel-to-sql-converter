package com.excelconverter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlUtil;

public class SqlGenerator {

    public static String generateInsertStatements(String tableName, List<List<String>> data,
                                                  boolean includeHeaders) {
        if (data.isEmpty()) {
            return "-- No data to convert";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("-- Generated INSERT statements for table: ").append(tableName).append("\n\n");

        List<String> headers = null;
        List<List<String>> rows = data;

        if (includeHeaders && !data.isEmpty()) {
            headers = data.get(0);
            rows = data.size() > 1 ? data.subList(1, data.size()) : new ArrayList<>();
        }

        if (rows.isEmpty()) {
            return "-- No data rows found";
        }

        // Generate column names part
        String columnsPart = "";
        if (headers != null) {
            columnsPart = " (" + String.join(", ", headers) + ")";
        }

        if(CollUtil.isNotEmpty(rows)){
            sql.append("INSERT INTO ").append(tableName).append(columnsPart)
                    .append(" VALUES ");
        }
        // Generate INSERT statements
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            sql.append("(");

            String values = row.stream()
                    .map(SqlGenerator::formatValue)
                    .collect(Collectors.joining(", "));

            if((i+1) == rows.size()){
                sql.append(values).append(");");

            }else {

                sql.append(values).append("),\n");
            }
        }
        /*for (List<String> row : rows) {
            // sql.append("INSERT INTO ").append(tableName).append(columnsPart)
                    sql.append("(");

            String values = row.stream()
                    .map(SqlGenerator::formatValue)
                    .collect(Collectors.joining(", "));

            sql.append(values).append("),\n");
        }*/

        return sql.toString();
    }

    private static String formatValue(String value) {
        if (StrUtil.isBlank(value)) {
            return "NULL";
        }

        // Check if it's a number
        if (value.matches("-?\\d+(\\.\\d+)?")) {
            return value;
        }

        // Check if it's a boolean
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return value;
        }

        // Treat as string - escape single quotes and wrap in single quotes
        if (MysqlFunctionCheckerByList.isMysqlBuiltinFunction(value)) {
            return  value;
        }
        return "'" + value.replace("'", "''") + "'";
    }
}