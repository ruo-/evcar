package com.evcard.service;

import com.alibaba.fastjson.JSONObject;
import com.evcard.model.EvcardEntity;
import com.evcard.model.LoginEntity;
import com.evcard.model.ResultEntity;
import com.evcard.util.MusicPlay;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.janino.UnicodeUnescapeReader;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by longwu on 16/10/20.
 */
public class Evcard {

    private static final String APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static final String LOGIN_URL = "http://api.evcard.online:8082/isv2/evcardapp?service=login";

    private static final String GET_VEHICLE_URL = "http://www.evcardchina.com/api/proxy/getVehicleInfoList";

    private static final String ORDER_VEHICLE_URL = "http://api.evcard.online:8082/isv2/evcardapp?service=orderVehicle";

    private static final String MUSIC_URL = "http://xunlei.sc.chinaz.com/files/download/sound1/201404/4331.wav";

    public static void main(String[] args) throws InterruptedException, IOException {

        //741  天地软件园
        //897 徐泾社区活动中心
        //933 徐泾政府
        Scanner input=new Scanner(System.in);
        System.out.print("请输入地址code:");
        String locationCode = input.next();

        System.out.print("请输入用户名:");
        String name = input.next();

        System.out.print("请输入密码:");
        String pwd = input.next();

        while(true){
            String queryRet;
            String queryParam = "{\"shopSeq\":" + locationCode + ",\"canRent\":1}";
            try {
                queryRet = URLPost(GET_VEHICLE_URL, queryParam);
                String str = convert(queryRet);
                List<EvcardEntity> evcardEntities = JSONObject.parseArray(str, EvcardEntity.class);
                if (evcardEntities.isEmpty()) {
                    System.out.println(getDateTime());
                    System.out.println("暂时没有车,5秒后重新查询");
                    System.out.println();
                    TimeUnit.SECONDS.sleep(5);
                    continue;
                }

                //登陆
                String loginParam = "{\"loginName\":" + "\"" + name + "\",\"password\":" + "\"" + pwd + "\"}";
                String loginRet = URLPost(LOGIN_URL, loginParam);
                System.out.println(loginRet);
                LoginEntity loginEntity = JSONObject.parseObject(loginRet, LoginEntity.class);
                //获取登陆token
                String token = loginEntity.getToken();
                String idCard = loginEntity.getAuthId();
                System.out.println(getDateTime());
                System.out.println("#############可以预定了!!!#############");
                //排序
                Collections.sort(evcardEntities);
                for (EvcardEntity evcardEntity : evcardEntities) {
                    System.out.println(evcardEntity);
                }
                System.out.println();
                EvcardEntity evcardEntity = evcardEntities.get(0);
                String vin = evcardEntity.getVin();
                String shopSeq = evcardEntity.getShopSeq();


                String orderParam = "{\n" +
                        " \"token\":" + "\"" + token + "\",\n" +
                        " \"authId\":" + "\"" + idCard + "\",\n" +
                        " \"planpickupstoreseq\":" + "\"" + shopSeq + "\",\n" +
                        "\"vin\":" + "\"" + vin + "\"}";

                String orderRet = URLPost(ORDER_VEHICLE_URL, orderParam);
                System.out.println(orderRet);

                ResultEntity resultEntity = JSONObject.parseObject(orderRet, ResultEntity.class);
                if ("预订已受理,请等待系统自动确认！".equals(resultEntity.getMessage())) {
                    System.out.println("预定成功!");
                    MusicPlay myMusicPlay = new MusicPlay(new URL(MUSIC_URL));
                    myMusicPlay.start();//播放一次
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            TimeUnit.MINUTES.sleep(1);
        }
    }


    public static String convert(String str) throws IOException {
        StringReader sr = new StringReader(str);
        UnicodeUnescapeReader uur = new UnicodeUnescapeReader(sr);

        StringBuffer buf = new StringBuffer();
        for(int c = uur.read(); c != -1; c = uur.read())
        {
            buf.append((char)c);
        }
        return buf.toString();
    }



    public static String URLPost(String url, String json) throws IOException {
        // 将JSON进行UTF-8编码,以便传输中文
        //        String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

        StringEntity se = new StringEntity(json);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        HttpResponse response = httpClient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        System.out.println("postCode= " + code);
        InputStream input = null;//输入流
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        StringBuffer sb = null;
        String line = null;
        // 若状态值为200，则ok
        if (code == HttpStatus.SC_OK) {
            //从服务器获得输入流
            input = response.getEntity().getContent();
            isr = new InputStreamReader(input);
            buffer = new BufferedReader(isr,10*1024);

            sb = new StringBuffer();
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();

    }

    public static String getDateTime() {  // 这是UNSAP要求的格式
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpledateformat.format(new Date());
    }





}
