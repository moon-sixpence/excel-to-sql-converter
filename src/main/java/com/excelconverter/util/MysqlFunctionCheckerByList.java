package com.excelconverter.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 预定义 MySQL 内置函数清单，快速判断（无数据库依赖）
 */
public class MysqlFunctionCheckerByList {
    // 注意：以下是 MySQL 5.7/8.0 通用核心内置函数（可按版本扩展/删减）
    // 完整清单可从 MySQL 官方文档提取：https://dev.mysql.com/doc/refman/8.0/en/functions.html
    private static final Set<String> MYSQL_BUILTIN_FUNCTIONS = new HashSet<>(Arrays.asList(
            // 字符串函数
            "CONCAT", "CONCAT_WS", "SUBSTRING", "LENGTH", "CHAR_LENGTH", "TRIM", "LTRIM", "RTRIM",
            "UPPER", "LOWER", "REPLACE", "INSTR", "LEFT", "RIGHT", "LPAD", "RPAD", "SUBSTRING_INDEX",
            // 数值函数
            "ABS", "ROUND", "FLOOR", "CEIL", "CEILING", "SUM", "AVG", "MAX", "MIN", "COUNT",
            "MOD", "POWER", "SQRT", "RAND", "INT", "CAST", "CONVERT",
            // 日期时间函数
            "NOW", "CURDATE", "SYSDATE","CURTIME", "DATE", "TIME", "YEAR", "MONTH", "DAY", "HOUR",
            "MINUTE", "SECOND", "DATE_FORMAT", "STR_TO_DATE", "TIMESTAMPDIFF", "DATEDIFF",
            // 逻辑/空值函数
            "IF", "IFNULL", "NULLIF", "CASE", "COALESCE",
            // 其他常用函数
            "INET_ATON", "INET_NTOA", "MD5", "SHA1", "SHA2", "UUID",
            // MySQL 8.0+ 新增函数（若适配 5.7，可删除以下行）
            "JSON_TABLE", "JSON_VALUE", "JSON_QUERY", "ST_X", "ST_Y", "REGEXP_INSTR"
    ));

    /**
     * 判断字符串是否为 MySQL 内置函数
     * @param functionExpression 要判断的字符串
     * @return true=是内置函数，false=否
     */
    public static boolean isMysqlBuiltinFunction(String functionExpression) {
        if (functionExpression == null || functionExpression.isEmpty()) {
            return false;
        }

        // 去除函数名后的括号和参数
        String functionName = functionExpression;
        int parenIndex = functionExpression.indexOf('(');
        if (parenIndex != -1) {
            functionName = functionExpression.substring(0, parenIndex);
        }

        // 去除可能存在的空格并转为大写
        functionName = functionName.trim().toUpperCase();

        return MYSQL_BUILTIN_FUNCTIONS.contains(functionName);
    }

    // 测试方法
    public static void main(String[] args) {
        System.out.println(isMysqlBuiltinFunction("now"));          // true
        System.out.println(isMysqlBuiltinFunction("DATE_FORMAT"));  // true
        System.out.println(isMysqlBuiltinFunction("CONCAT"));       // true
        System.out.println(isMysqlBuiltinFunction("sysdate()"));    // false
        System.out.println(isMysqlBuiltinFunction("GROUP"));        // false（关键字）
        System.out.println(isMysqlBuiltinFunction("JSON_TABLE"));   // true（8.0+）
    }
}