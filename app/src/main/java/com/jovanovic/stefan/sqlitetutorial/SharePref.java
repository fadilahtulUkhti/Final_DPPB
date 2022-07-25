package com.jovanovic.stefan.sqlitetutorial;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    SharedPreferences myPref;
    public SharePref(Context context){
        myPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = myPref.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    public Boolean loadNightModeState(){
        Boolean state = myPref.getBoolean("NightMode",false);
        return state;
    }
}
