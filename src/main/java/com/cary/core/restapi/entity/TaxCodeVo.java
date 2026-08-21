package com.cary.core.restapi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaxCodeVo implements Serializable {
    private String hwhlwmc;
    private String hyjh;
    private String qyrq;
    private String sfbzsbz;
    private String sfhzx;
    private List<String> slvList;
    private String sm;
    private String spfwjc;
    private String sphfwssflhbbm;
    private String spsjbm;
    private String zsljh;
    private List<String> zslList;
    private String zt;
    private String zzscezsbj;
    private String zzsslhzzsl;
    private List<String> zzstsglList;
}