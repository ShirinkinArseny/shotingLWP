package com.acidspacecompany.shotinglwp;

import android.util.Log;


public class BicycleDebugger {
    public static final boolean ON = true;
    public static void d(String tag, String message)
    {
        if (ON)
            Log.d(tag, message);
    }
    public static void i(String tag, String message){

        if (ON)
            Log.i(tag, message);
    }
    public static void e(String tag, String message){

        if (ON)
            Log.e(tag, message);
    }
    public static void v(String tag, String message){
        if (ON)
            Log.v(tag, message);
    }
    public static void w(String tag, String message){
        if (ON)
            Log.w(tag, message);
    }
}
