package com.home.autrob;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by IMFCORP\alok.acharya on 12/12/16.
 */

public class SettingManager {


    public static final String SPINNER_KEY = "SPINNER_KEY";
    public static final String SWITCH_KEY = "SWITCH_KEY";
    public static final int ON = 1;
    public static final int OFF = 0;
    private final String GOT_IT_KEY = "got_it";
    private static final String PREFERENCE_KEY = "preference";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SettingManager(Context mContext){
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    private static SettingManager _instance = null;

    public static SettingManager getInstance(Context _context){
        if(_instance==null){
            return  new SettingManager(_context);
        }
        return _instance;
    }

    public boolean getSave(){
        return mSharedPreferences.getBoolean(GOT_IT_KEY, false);
    }

    public void setSave(boolean status){
        mEditor.putBoolean(GOT_IT_KEY,status);
        mEditor.commit();
    }



    public static void putHashMapIntoSharedPref(Context ctx,HashMap<Integer,Integer> testHashMap,String key){
        //create test hashmap
        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(testHashMap);

        //save in shared prefs
        SharedPreferences prefs = ctx.getSharedPreferences("test", MODE_PRIVATE);
        prefs.edit().putString(key, hashMapString).apply();

    }

    public static HashMap<Integer,Integer> getHashMapfromSharedPref(Context ctx,String key){
        SharedPreferences prefs = ctx.getSharedPreferences("test", MODE_PRIVATE);
        Gson gson = new Gson();
        //get from shared prefs
        String storedHashMapString =prefs.getString(key, "oopsDintWork");
        java.lang.reflect.Type type = new TypeToken<HashMap<Integer,Integer>>(){}.getType();
        if(!storedHashMapString.equalsIgnoreCase("oopsDintWork")) {
            HashMap<Integer, Integer> testHashMap2 = gson.fromJson(storedHashMapString, type);
            return testHashMap2;
        }else {
            return null;
        }
    }

}
