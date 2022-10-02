package com.it.bean;

import lombok.Data;

@Data
public class JWTBean {
    private String type;
    private String signature;
    private String payLoad;
}
