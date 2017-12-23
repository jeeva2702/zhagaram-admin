package com.example.hp.zha.Adapters;

/**
 * Created by HP on 11/15/2017.
 */

public class wallAdapter {
    public wallAdapter()
    {

    }
    String titl;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getTitl() {
        return titl;
    }

    public void setTitl(String titl) {
        this.titl = titl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public wallAdapter(String title, String url, String date,String time,String type){
        this.titl=title;
        this.url=url;
        this.date=date;
        this.time=time;
        this.type=type;

    }
}
