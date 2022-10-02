package com.it.service.impl;

import com.it.service.ISqlService;
import com.it.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SqlServiceImpl implements ISqlService {

    @Override
    public String transfer(String originText) {
        String[] sql = originText.split("\n");
        String sqls;
        String params;
        if (StringUtils.contains(sql[0], "Parameters")) {
            sqls = sql[1].replace(sql[1].substring(0, sql[1].indexOf(">")), "");
            params = sql[0].replace(sql[0].substring(0, sql[0].indexOf(">")), "");
        } else {
            params = sql[1].replace(sql[1].substring(0, sql[1].indexOf(">")), "");
            sqls = sql[0].replace(sql[0].substring(0, sql[0].indexOf(">")), "");
        }


        int i = sqls.indexOf(":");
        int i1 = params.indexOf(":");
        sqls = sqls.replace(sqls.substring(0, i + 1), "");
        params = params.replace(params.substring(0, i1 + 1), "");


        return SqlUtils.parseSql(sqls, params);
    }
}
