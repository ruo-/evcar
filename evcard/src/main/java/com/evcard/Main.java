package com.evcard;

import com.alibaba.fastjson.JSONObject;
import com.evcard.common.Util;
import com.evcard.model.EvcardEntity;
import com.evcard.model.LoginEntity;
import com.evcard.service.CustomService;
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
        StringBuilder sucMsg = new StringBuilder();
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("==============欢迎使用EVCARD自动订车功能==============");
            System.out.println("1、【查询code】\n" +
                    "2、【查询car】\n" +
                    "3、【订车】\n" +
                    "4、【取消】");
            String select = input.next();
            if ("1".equals(select)) {
                System.out.println("==============开始地址对应code==============");
                System.out.print("【请输入地址:】");
                String address = input.next();
                System.out.println(QueryAllShopService.getShopCodeByAddress(address));
                System.out.println();
            } else if ("2".equals(select)) {
                String code;
                String type;
                do {
                    System.out.println("==============开始某点查询车辆信息==============");
                    System.out.print("【请输入地址code:】");
                    code = input.next();
                    System.out.print("1、【查询可租车辆】\n" +
                                    "2、【查询不可租车辆】");
                    type = input.next();
                } while ("0".equals(type));

                while (true) {
                    List<EvcardEntity> evcardEntities = EvcardService.query(code, type);
                    if (evcardEntities.isEmpty()) {
                        noCar();
                        //等待5秒继续查询
                        TimeUnit.SECONDS.sleep(5);
                    } else {
                        hasCar(evcardEntities);
                        //等待5秒继续查询
                        TimeUnit.SECONDS.sleep(5);
                    }
                }
            } else if ("3".equals(select)) {
                System.out.println("==============开始订车==============");
                String name, pwd = "", login;
                do {
                    System.out.print("【请输入用户名:】");
                    name = input.next();
                    //定制化服务,超管不需要输入密码
                    List<CustomService> list = CustomService.getCustomeService();
                    boolean custom = false;
                    for (CustomService customService : list) {
                        if (customService.getKey().equals(name)) {
                            name = customService.getName();
                            pwd = customService.getPwd();
                            custom = true;
                        }
                    }
                    if (!custom) {
                        System.out.print("【请输入密码:】");
                        pwd = input.next();
                    }

                    //先校验用户名密码
                    login = EvcardService.login(name, pwd);
                    if (login.contains("用户名或密码错误！")) {
                        System.out.println("==============用户名或密码错误！请重新输入用户名和密码==============");
                    } else {
                        System.out.println("==============登陆成功==============");
                        System.out.print("【请输入地址code:】");
                        String code = input.next();
                        System.out.print("1、【按照车牌号预定】\n" +
                                        "2、【自动预定】");
                        String order = input.next();
                        String carNo = "";
                        if ("1".equals(order)) {
                            List<EvcardEntity> evcardEntities = EvcardService.query(code, "1");
                            if (evcardEntities.isEmpty()) {
                                System.out.println("暂无可预定车辆!!!");
                            } else {
                                System.out.println("以下车辆可以预定:");
                                //排序
                                Collections.sort(evcardEntities);
                                for (EvcardEntity evcardEntity : evcardEntities) {
                                    System.out.println(evcardEntity);
                                }
                            }
                            System.out.print("【请输入要预定的车牌号:】");
                            carNo = input.next();
                        }


                        while (true) {
                            List<EvcardEntity> evcardEntities = EvcardService.query(code, "1");
                            if (evcardEntities.isEmpty()) {
                                if (sucMsg.length() > 0) {
                                    System.out.println("您已经预定了车辆");
                                    System.out.println(sucMsg);
                                } else {
                                    noCar();
                                }
                                //等待5秒继续查询
                                TimeUnit.SECONDS.sleep(5);
                                continue;
                            }
                            System.out.println("该点车辆信息如下:");
                            //排序
                            Collections.sort(evcardEntities);
                            EvcardEntity orderEntity = null;
                            for (EvcardEntity evcardEntity : evcardEntities) {
                                if (carNo.trim().toUpperCase().equals(evcardEntity.getVehicleNo().toUpperCase())) {
                                    orderEntity = evcardEntity;
                                }
                                System.out.println(evcardEntity);
                            }
                            LoginEntity loginEntity = JSONObject.parseObject(login, LoginEntity.class);
                            orderEntity = orderEntity == null ? evcardEntities.get(0) : orderEntity;

                            String result = EvcardService.order(loginEntity.getToken(), loginEntity.getAuthId(), orderEntity, sucMsg);
                            if (result.contains("登录后进行该操作")) {
                                System.out.println("用户被踢出,重新登录!!!");
                                login = EvcardService.login(name, pwd);
                            } else {
                                System.out.println("==========预定成功!==========");
                                System.out.println(sucMsg);
                                new Thread(new Runnable() {
                                    public void run() {
                                        for (int i = 0; i < 60; i++) {
                                            if (i % 20 == 0) {
                                                System.out.println();
                                            }
                                            System.out.print(i + 1 + "秒; ");
                                            try {
                                                TimeUnit.SECONDS.sleep(1);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();
                                TimeUnit.MINUTES.sleep(1);
                                System.out.println();

                            }
                        }
                    }

                } while (true);

            } else if ("4".equals(select)) {
                String name, pwd="", login;
                System.out.print("【请输入用户名:】");
                name = input.next();

                //定制化服务,超管不需要输入密码
                List<CustomService> list = CustomService.getCustomeService();
                boolean custom = false;
                for (CustomService customService : list) {
                    if (customService.getKey().equals(name)) {
                        name = customService.getName();
                        pwd = customService.getPwd();
                        custom = true;
                    }
                }
                if (!custom) {
                    System.out.print("【请输入密码:】");
                    pwd = input.next();
                }

                //先校验用户名密码
                login = EvcardService.login(name, pwd);
                if (login.contains("用户名或密码错误！")) {
                    System.out.println("==============用户名或密码错误！请重新输入用户名和密码==============");
                } else {
                    System.out.println("==============登陆成功==============");
                    LoginEntity loginEntity = JSONObject.parseObject(login, LoginEntity.class);
                    System.out.println("【请输入orderSeq:】");
                    String orderSeq = input.next();
                    //取消预定
                    EvcardService.cancleOrder(loginEntity.getToken(), loginEntity.getAuthId(),orderSeq);
                }

            }

        }
    }

    private static void noCar() {
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
        System.out.println();

    }

    private static void hasCar(List<EvcardEntity> evcardEntities) {
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
        System.out.println();
    }



}


