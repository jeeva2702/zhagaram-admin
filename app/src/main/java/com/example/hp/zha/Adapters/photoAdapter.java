package com.example.hp.zha.Adapters;

/**
 * Created by HP on 11/16/2017.
 */

public class photoAdapter {
    String title;

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    String surl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public photoAdapter()
    {

    }
    public photoAdapter(String title,String surl){
        this.title=title;
        this.surl=surl;
    }
}
