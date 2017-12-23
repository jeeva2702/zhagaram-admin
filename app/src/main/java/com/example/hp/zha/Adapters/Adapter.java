package com.example.hp.zha.Adapters;

/**
 * Created by HP on 6/26/2017.
 */

public class Adapter {
    //public static String str1;

    String com;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNotcom() {
        return notcom;
    }

    public void setNotcom(String notcom) {
        this.notcom = notcom;
    }

    String notcom;
    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    //    String android_image_url;
//
//    public String getAndroid_image_url() {
//        return android_image_url;
//    }
//
//    public void setAndroid_image_url(String android_image_url) {
//        this.android_image_url = android_image_url;
//    }
    String desp;


    public String getLocc() {
        return Locc;
    }

    public void setLocc(String locc) {
        Locc = locc;
    }

    String Locc;
    String sd;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getEd() {
        return ed;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }

    String ed;

    public Adapter(){}

    public Adapter(String n, String f, String s, String e, String loc,String co,String notc)
    {

        name = n;
        desp=f;
        sd=s;
        ed=e;
        Locc=loc;
        com=co;
        notcom=notc;
        //android_image_url=e;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;





}
