package com.example.hp.zha.Adapters;

/**
 * Created by HP on 11/18/2017.
 */

public class scheduleAdapter {
    public scheduleAdapter()
    {

    }
    String time, descrp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescrp() {
        return descrp;
    }

    public void setDescrp(String descrp) {
        this.descrp = descrp;
    }
    public scheduleAdapter(String t, String de){
        this.descrp=de;
        this.time=t;
    }
}
