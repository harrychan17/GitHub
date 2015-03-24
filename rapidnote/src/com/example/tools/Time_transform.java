package com.example.tools;

/**
 * Created by colin on 2015/3/18.
 */
public class Time_transform {
    public static int toSecond(String time) {
        int minute = Integer.valueOf(time.substring(0, time.indexOf(":")));
        int second = Integer.valueOf(time.substring(time.indexOf(":")+1, time.length()));
        return minute * 60 + second;
    }

    public static String toMinute_And_Second(int second) {
        int minute=second/60;
        second=second%60;
        if(second<10)
        return new String(minute+":0"+second);
        else
        return new String(minute+":"+second);
    }

}
