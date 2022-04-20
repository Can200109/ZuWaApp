package com.example.zuwaapp;

public class Constant {
    public static final String IP = "10.7.88.152";
    public static final String USER_URL ="http://"+IP+":8080/zuwa/user/";
    public static final String PRODUCT_URL ="http://"+IP+":8080/zuwa/product/";
    public static final String COLLECT_URL ="http://"+IP+":8080/zuwa/collect/";
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
    public static final int FIND_RENT = 8;
    public static final int DELETE_PRODUCT = 9;
    public static final int FIND_PRODUCT_BY_PRODUCTTYPE = 10;
    public static final int FIND_PRODUCT_BY_ID = 11;
    public  static final int ADD_BY_PHONENUMBER = 12;
    public static final int ADD_COLLECT = 13;
    public static final int DELETE_COLLECT = 14;
    public static final int FIND_COLLECT = 15;
    public static final int FIND_COLLECT_BY_PHONENUMBER = 16;
    public static final int SET_COLOR = 17;


    public static String PHONENUMBER = "15128895429";


    public static void setPHONENUMBER(String PHONENUMBER) {
        Constant.PHONENUMBER = PHONENUMBER;
    }


}
