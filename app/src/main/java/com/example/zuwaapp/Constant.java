package com.example.zuwaapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Constant {
    public static final String IP = "1.117.67.217";
//    public static final String IP = "192.168.31.22";
    public static final String USER_URL ="http://"+IP+":8080/zuwa/user/";
    public static final String PRODUCT_URL ="http://"+IP+":8080/zuwa/product/";
    public static final String COLLECT_URL ="http://"+IP+":8080/zuwa/collect/";
    public static final String RENT_URL = "http://"+IP+":8080/zuwa/rent/";
    public static final String ORDERTYPE_URL = "http://"+IP+":8080/zuwa/orderType/";
    public static final String PRODUCT_PHOTO = "http://"+IP+":8080/zuwa/zuwaPhoto/";
    public static final String USER_PHOTO = "http://"+IP+":8080/zuwa/zuwaPhoto/";
    public static final int REQUEST_IMAGE = 200;
    public static final int LOGIN = 1;
    public static final int MODIFY = 2;
    public static final int DELETE = 3;
    public static final int ADD_PRODUCT = 4;
    public static final int FIND_ALL = 5;
    public static final int FIND_USER_BY_PHONENUMBER = 6;
    public static final int  FIND_PRODUCT_BY_PHONENUMBER = 7;
    public static final int FIND_RENT_BY_PHONENUMBER = 8;
    public static final int DELETE_PRODUCT = 9;
    public static final int FIND_PRODUCT_BY_PRODUCTTYPE = 10;
    public static final int FIND_PRODUCT_BY_ID = 11;
    public  static final int ADD_BY_PHONENUMBER = 12;
    public static final int ADD_COLLECT = 13;
    public static final int DELETE_COLLECT = 14;
    public static final int FIND_COLLECT = 15;
    public static final int FIND_COLLECT_BY_PHONENUMBER = 16;
    public static final int SET_COLOR = 17;
    public static final int ADD_Rent = 18;
    public static final int DELETE_RENT_BY_PHONENUMBER = 19;
    public static final int DELETE_RENT = 20;
    public static final int ADD_ORDERTYPE = 21;
    public static final int FIND_ORDER = 22;

    public static final int REFRESH = 23;
    public static final int LOAD_MORE = 24;


    // 没有加final？？？？？
    public static String PHONENUMBER = "16631168888";
//    public static String PHONENUMBER = "15128895429";


    public static void setPHONENUMBER(String PHONENUMBER) {
        Constant.PHONENUMBER = PHONENUMBER;
    }


    //这里，emmm，只可意会不可言传。啊哈哈哈
    public static List getRandomThreeInfoList(List list, int count) {
        List olist = new ArrayList<>();
        if (list.size() <= count) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0 ;i<count;i++){
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }

}
