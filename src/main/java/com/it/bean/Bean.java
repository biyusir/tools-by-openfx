package com.it.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data@AllArgsConstructor@NoArgsConstructor
public class Bean implements Comparable{
    private String orgText;
    private Integer type;
    private Date createTime;
    private String transferText;


    @Override
    public int compareTo(Object o) {
        return (((Bean) o).getCreateTime()).compareTo(createTime);
    }
}
