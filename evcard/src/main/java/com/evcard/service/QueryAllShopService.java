package com.evcard.service;

import com.alibaba.fastjson.JSONObject;
import com.evcard.model.ShopSeqEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by longwu on 16/11/11.
 */
public class QueryAllShopService {

    public static List getShopCodeByAddress(String address){
        String shopStrs = testERPProductJson(QueryAllShopService.class.getClassLoader().getResourceAsStream("shop.txt"));

        List<ShopSeqEntity> res = new ArrayList<ShopSeqEntity>();
        List<ShopSeqEntity> evcardEntities = JSONObject.parseArray(shopStrs, ShopSeqEntity.class);

        for (ShopSeqEntity evcardEntity : evcardEntities) {
            if (evcardEntity.getAddress().contains(address) || evcardEntity.getShopName().contains(address)) {
                res.add(evcardEntity);
            }
        }
        return Arrays.asList(res);
    }

    public static String testERPProductJson(InputStream in) {


        /**
         * 以字符为单位读取文件，常用于读文本，数字等类型的文件
         */
        Reader reader = null;


        try {
            // 一次读多个字符
            char[] tempchars = new char[30];
            StringBuffer sb = new StringBuffer();
            int charread = 0;
            reader = new InputStreamReader(in, "UTF-8");
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    sb.append(tempchars);
                    // System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            //System.out.print(tempchars[i]);
                            sb.append(tempchars[i]);
                        }
                    }
                }
            }
            return sb.toString();


        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "";


    }
}
