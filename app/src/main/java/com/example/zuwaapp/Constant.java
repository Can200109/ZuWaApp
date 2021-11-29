package com.example.zuwaapp;

public class Constant {
    阿瓦达多无
    public static final String USER_URL ="http://192.168.137.1:8080/zuwa/user/";
    public static final String PRODUCT_URL ="http://192.168.137.1:8080/zuwa/product/";
    public static final String COLLECT_URL ="http://192.168.137.1:8080/user/collect/";
    public static final String PRODUCT_PHOTO = "http://192.168.137.1:8080/zuwa/productPhoto/";
    public static final int REQUEST_IMAGE = 200;
    public static final int LOGIN = 1;
    public static final int MODIFY = 2;
    public static final int DELETE = 3;
    public static final int ADD_PRODUCT = 4;
    public static final int FIND_ALL = 5;
    public static final int FIND_USER_BY_PHONENUMBER = 6;
    public static final int  FIND_PRODUCT_BY_PHONENUMBER = 7;
    public static final int FIND_RENT = 8;
    public static final int DELETE_PRODUCT = 9;
    public static final int FIND_PRODUCT_BY_PRODUCTTYPE = 10;
    public static final int FIND_PRODUCT_BY_ID = 11;
    public  static final int ADD_BY_PHONENUMBER = 12;
    public static final int ADD_COLLECT = 13;
    public static final int DELETE_COLLECT = 14;
    public static final int FIND_COLLECT = 15;

    public static String PHONENUMBER;


    public static void setPHONENUMBER(String PHONENUMBER) {
        Constant.PHONENUMBER = PHONENUMBER;
    }


}
