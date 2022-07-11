package com.example.zuwaapp.entity;

public class Title {

    private String name;
    //isSelect用来在适配器进行item判断，并设置get set方法
    private boolean isSelect;

    public Title(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
