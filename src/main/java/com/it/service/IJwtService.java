package com.it.service;

import com.it.bean.JWTBean;

import java.util.Map;

public interface IJwtService {

    String create(String sign, Map<String,Object> claims,String signSign);

    JWTBean decode(String token);
}
