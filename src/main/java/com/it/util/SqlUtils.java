package com.it.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SqlUtils {

    private static List<String> parseParams(String params) {
        List<String> objects = new ArrayList<>();

        String[] paramArrays = params.split(",");
        for (String param : paramArrays) {
            int lastIndexOf1 = param.lastIndexOf("(");
            int lastIndexOf = param.lastIndexOf(")");
            if (lastIndexOf1 == -1 || lastIndexOf == -1) {
                objects.add(param);
                continue;
            }
            String type = param.substring(lastIndexOf1 + 1, lastIndexOf);
            if (typeMap.containsKey(type)) {
                String data = param.substring(0,lastIndexOf1);
                if (StringUtils.equalsAnyIgnoreCase(data,"null")){
                    objects.add(null);
                    continue;
                }
                if (typeMap.get(type) == 1) {
                    objects.add("'" + data + "'");
                } else {
                    objects.add(data);
                }

            }
//        List<String> paramsArr = new ArrayList<>();
//        int len = params.length();
//        char[] val = params.toCharArray();
//        int start = 1;
//        int nextStart = 1;
//        char left = 40;
//        char right = 41;
//        for (int i = 1; i < len; i++) {
//            if (left == val[i]) {
//                start = i;
//            } else if (right == val[i] && start != 1) {
//                String type = params.substring(start + 1, i);
//                if (typeMap.containsKey(type)) {
//                    String data = params.substring(nextStart, start);
//                    if (typeMap.get(type) == 1) {
//                        paramsArr.add("'" + data + "'");
//                    } else {
//                        paramsArr.add(data);
//                    }
//                } else {
//                    log.warn("type_not_exist:{},{}", type, params);
//                }
//                nextStart = i + 3;
//                start = 1;
//            }
//        }
//        return paramsArr;

        }
        return objects;
    }

    private static Map<String, Integer> typeMap = new HashMap<String, Integer>() {
        private static final long serialVersionUID = -5772881989251971824L;

        {
            //(String) (Timestamp) (LocalDateTime) (Integer) (BigDecimal) (Float) (Double) (Boolean)(Long)(byte[])(byte)
            put("String", 1);
            put("Timestamp", 1);
            put("LocalDateTime", 1);
            put("BigDecimal", 1);
            put("Integer", 0);
            put("Byte", 0);
            put("Float", 0);
            put("Double", 0);
            put("Boolean", 0);
            put("Long", 0);
            put("byte[]", 0);
            put("byte", 0);
        }
    };


    public static String parseSql(String sql, String params) {
        log.info("sql===>"+sql);
        log.info("params===>"+params);
        List<String> paramsArr = parseParams(params);
        int firstCharIndex = sql.indexOf("?");
        if (firstCharIndex == -1) {
            return "";
        }
        List<Integer> sqlIndex = new ArrayList<>();
        sqlIndex.add(firstCharIndex);
        while (true) {
            int nextIndex = sql.indexOf("?", firstCharIndex + 1);
            if (nextIndex == -1) {
                break;
            }
            firstCharIndex = nextIndex;
            sqlIndex.add(firstCharIndex);
        }
        String tmpSql = "";
        int len = sqlIndex.size();
        if (len == paramsArr.size()) {
            for (int i = len - 1; i >= 0; i--) {
                if (tmpSql.length() < 1) {
                    tmpSql = sql.substring(0, sqlIndex.get(i)) + paramsArr.get(i) + sql.substring(sqlIndex.get(i) + 1);
                } else {
                    tmpSql = tmpSql.substring(0, sqlIndex.get(i)) + paramsArr.get(i) + tmpSql.substring(sqlIndex.get(i) + 1);
                }
            }
        } else {
            log.error("parseSql_err:{},{}", sql, params);
        }
        //printSql(tmpSql);
        return tmpSql;
    }

}
