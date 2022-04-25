package com.example.minevera;

public class ProductObject {
    public String id;
    public String name;
    public String date;
    public String difference;

    public ProductObject(String id, String name, String date, String difference){
        this.id = id;
        this. name = name;
        this.date = date;
        this.difference = difference;
    }
    public String getId(){
        return this.id;
    }

}
