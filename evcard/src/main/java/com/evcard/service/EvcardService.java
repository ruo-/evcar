package com.evcard.service;

import com.alibaba.fastjson.JSONObject;
import com.evcard.common.Constant;
import com.evcard.common.MusicPlay;
import com.evcard.common.Util;
import com.evcard.model.EvcardEntity;
import com.evcard.model.ResultEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by longwu on 16/11/12.
 */
public class EvcardService {

    /**
     * 登陆服务
     * @param username
     * @param password
     * @return
     */
    public static String login(String username, String password) {
        System.out.println("==============开始登陆==============");
        //登陆
        String param = "{\"loginName\":" + "\"" + username + "\",\"password\":" + "\"" + password + "\"}";
        return Util.urlPost(Constant.LOGIN_URL, param);
    }

    /**
     *
     * @param code 位置码code
     * @param type 1:能租 2:不能租
     * @return
     */
    public static List query(String code,String type) {
        System.out.println("==============开始查询==============");
        String queryRet;
        String param = "{\"shopSeq\":" + code + ",\"canRent\":" + type + "}";
        queryRet = Util.urlPost(Constant.GET_VEHICLE_URL, param);
        String str = Util.convert(queryRet);
        return JSONObject.parseArray(str, EvcardEntity.class);
    }

    /**
     * 订车
     * @param token
     * @param idCard
     * @return
     */
    public static String order(String token,String idCard,EvcardEntity evcardEntity) {
        System.out.println("==============开始订车==============");
        //获取登陆token
        String vin = evcardEntity.getVin();
        String shopSeq = evcardEntity.getShopSeq();


        String orderParam = "{\n" +
                " \"token\":" + "\"" + token + "\",\n" +
                " \"authId\":" + "\"" + idCard + "\",\n" +
                " \"planpickupstoreseq\":" + "\"" + shopSeq + "\",\n" +
                "\"vin\":" + "\"" + vin + "\"}";

        String orderRet = Util.urlPost(Constant.ORDER_VEHICLE_URL, orderParam);
        System.out.println(orderRet);

        ResultEntity resultEntity = JSONObject.parseObject(orderRet, ResultEntity.class);
        if ("预订已受理,请等待系统自动确认！".equals(resultEntity.getMessage())) {
            MusicPlay myMusicPlay = null;
            try {
                myMusicPlay = new MusicPlay(new URL(Constant.MUSIC_URL));
                myMusicPlay.start();//播放一次
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return "==========预定成功!==========车辆信息:" +  evcardEntity.toString();
        } else {
            return resultEntity.getMessage() + "请1分钟后重试!";
        }
    }

}
