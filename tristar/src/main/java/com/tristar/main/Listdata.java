package com.tristar.main;

import java.util.ArrayList;

/**
 * Created by cbe-tecmubt-016 on 7/9/17.
 */

class Listdata {
    String detail1,detail2,detail3,detail4;
//    private ArrayList<Child> Items;

    public Listdata(String detail1,String detail2,String detail3,String detail4)
    {
        this.detail1 = detail1;
        this.detail2 = detail2;
        this.detail3 = detail3;
        this.detail4 = detail4;
    }

    public String getDetail1(){
        return detail1;
    }

    public String getDetail2(){
        return detail2;
    }

    public String getDetail3(){
        return detail3;
    }

    public String getDetail4(){
        return detail4;
    }
/*
    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> items) {
        Items = items;
    }*/
}
