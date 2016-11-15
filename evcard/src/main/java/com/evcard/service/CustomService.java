package com.evcard.service;

import com.evcard.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longwu on 16/11/15.
 */
public class CustomService {
    private String key;
    private String name;
    private String pwd;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public static List<CustomService> getCustomeService() {
        List<CustomService> list = new ArrayList<CustomService>();

        CustomService lw = new CustomService();
        lw.setKey("lw");
        lw.setName(Constant.USER_NAME);
        lw.setPwd(Constant.PWD);

        CustomService zl = new CustomService();
        zl.setKey("zl");
        zl.setName("15921873125");
        zl.setPwd("159753555asd");

        CustomService xjm = new CustomService();
        xjm.setKey("xjm");
        xjm.setName("18721956624");
        xjm.setPwd("xiao3324119");

        CustomService hx = new CustomService();
        hx.setKey("hx");
        hx.setName("18516096864");
        hx.setPwd("hanxiao1988");

        list.add(lw);
        list.add(zl);
        list.add(xjm);
        list.add(hx);

        return list;


    }
}
