package com.evcard.common;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.janino.UnicodeUnescapeReader;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by longwu on 16/11/12.
 */
public class Util {

    public static String urlPost(String url, String json) {
        // 将JSON进行UTF-8编码,以便传输中文
        //        String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON);

            StringEntity se = new StringEntity(json);
            se.setContentType(Constant.CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON));
            httpPost.setEntity(se);
            HttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
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
                buffer = new BufferedReader(isr, 10 * 1024);

                sb = new StringBuffer();
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
            }
            return sb.toString();

        } catch (IOException e) {
            System.out.println(e);
        }
        return "";

    }

    public static String convert(String str) {
        try {
            StringReader sr = new StringReader(str);
            UnicodeUnescapeReader uur = new UnicodeUnescapeReader(sr);

            StringBuffer buf = new StringBuffer();
            for (int c = uur.read(); c != -1; c = uur.read()) {
                buf.append((char) c);
            }
            return buf.toString();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "";
    }

    public static String getDateTime() {  // 这是UNSAP要求的格式
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpledateformat.format(new Date());
    }
}
