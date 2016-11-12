package com.evcard;

import com.alibaba.fastjson.JSONObject;
import com.evcard.common.Util;
import com.evcard.model.EvcardEntity;
import com.evcard.model.LoginEntity;
import com.evcard.service.EvcardService;
import com.evcard.service.QueryAllShopService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by longwu on 16/10/20.
 * //741  天地软件园
 * //897 徐泾社区活动中心
 * //933 徐泾政府
 */

public class Main {


    public static void main(String[] args) throws InterruptedException, IOException {

        Scanner input = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("==============欢迎使用EVCARD自动订车功能==============");
            System.out.println("查询地址对应code,请按1;查询code对应的车子情况请按2;订车请按3;");
            String select = input.next();
            if ("1".equals(select)) {
                System.out.println("==============开始地址对应code==============");
                System.out.print("请输入地址:");
                String address = input.next();
                System.out.println(QueryAllShopService.getShopCodeByAddress(address));
                System.out.println();
            } else if ("2".equals(select)) {
                String code;
                String type;
                do {
                    System.out.println("==============开始某点查询车辆信息==============");
                    System.out.print("请输入地址code:");
                    code = input.next();
                    System.out.print("查询该点所有能租车辆请按1;查询该点所有车辆请按0:");
                    type = input.next();
                    List<EvcardEntity> evcardEntities = EvcardService.query(code, type);
                    if (evcardEntities.isEmpty()) {
                        System.out.println(Util.getDateTime());
                        System.out.println("很抱歉,该点暂时没有符合查询条件的车辆,5秒后重新为您查询!");
                        System.out.println();
                        new Thread(new Runnable() {
                            public void run() {
                                for (int i = 0; i < 5; i++) {
                                    System.out.print(i + 1 + "秒; ");
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                        TimeUnit.SECONDS.sleep(5);
                        continue;
                    }
                    System.out.println("该点车辆信息如下:");
                    //排序
                    Collections.sort(evcardEntities);
                    for (EvcardEntity evcardEntity : evcardEntities) {
                        System.out.println(evcardEntity);
                    }
                    System.out.println();
                    new Thread(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 5; i++) {
                                System.out.print(i + 1 + "秒; ");
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    TimeUnit.SECONDS.sleep(5);
                    continue;

                } while ("0".equals(type));

                while (true) {
                    List<EvcardEntity> evcardEntities = EvcardService.query(code, type);
                    if (evcardEntities.isEmpty()) {
                        System.out.println(Util.getDateTime());
                        System.out.println("很抱歉,该点暂时没有符合查询条件的车辆,5秒后重新为您查询!");
                        System.out.println();
                        new Thread(new Runnable() {
                            public void run() {
                                for (int i = 0; i < 5; i++) {
                                    System.out.print(i + 1 + "秒; ");
                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                        TimeUnit.SECONDS.sleep(5);
                        continue;
                    }
                    System.out.println("该点车辆信息如下:");
                    //排序
                    Collections.sort(evcardEntities);
                    for (EvcardEntity evcardEntity : evcardEntities) {
                        System.out.println(evcardEntity);
                    }
                    System.out.println();
                    new Thread(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 5; i++) {
                                System.out.print(i + 1 + "秒; ");
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    TimeUnit.SECONDS.sleep(5);
                }
            } else if ("3".equals(select)) {
                System.out.println("==============开始订车==============");
                do {
                    System.out.print("请输入用户名:");
                    String name = input.next();

                    System.out.print("请输入密码:");
                    String pwd = input.next();
                    //先校验用户名密码
                    String login = EvcardService.login(name, pwd);
                    if (login.contains("用户名或密码错误！")) {
                        System.out.println("==============用户名或密码错误！请重新输入用户名和密码==============");
                    } else {
                        System.out.println("==============登陆成功！==============");
                        System.out.print("请输入地址code:");
                        String code = input.next();
                        while (true) {
                            List<EvcardEntity> evcardEntities = EvcardService.query(code, "1");
                            if (evcardEntities.isEmpty()) {
                                System.out.println(Util.getDateTime());
                                System.out.println("很抱歉,该点暂时没有符合查询条件的车辆,5秒后重新为您查询!");
                                System.out.println();
                                new Thread(new Runnable() {
                                    public void run() {
                                        for (int i = 0; i < 5; i++) {
                                            System.out.print(i + 1 + "秒; ");
                                            try {
                                                TimeUnit.SECONDS.sleep(1);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                                TimeUnit.SECONDS.sleep(5);

                                continue;
                            }
                            System.out.println("该点车辆信息如下:");
                            //排序
                            Collections.sort(evcardEntities);
                            for (EvcardEntity evcardEntity : evcardEntities) {
                                System.out.println(evcardEntity);
                            }
                            LoginEntity loginEntity = JSONObject.parseObject(login, LoginEntity.class);
                            EvcardService.order(loginEntity.getToken(), loginEntity.getAuthId(), evcardEntities.get(0));
                        }
                    }

                } while (true);

            }
        }


    }


}


