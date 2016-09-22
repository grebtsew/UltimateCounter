package com.grebtsew.app.ultimategamecounter.Library;

import android.content.Context;
import android.preference.PreferenceManager;

import com.grebtsew.app.ultimategamecounter.Structures.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Daniel Westberg on 2016-08-12.
 */
public class SharePreferences {

    ArrayList<Player> arrayOfUsers = new ArrayList<Player>();
    public static final String PLAYER_LIST = "PLAYER_LIST";
    public static final String MULTIPLY_SETTING = "MULTIPLY_SETTING";
    public static final String SHAKE_SETTING = "SHAKE_SETTING";
    public static final String SHAKE_UP_DOWN_SETTING = "SHAKE_UP_DOWN_SETTING";
    public static final String TURN_SETTING = "TURN_SETTING";
    public static final String PLAYER_FOCUS = "PLAYER_FOCUS";
    public static final String WANTED_SCORE_SETTING = "WANTED_SCORE_SETTING";

    Context context;


    public static ArrayList<Player> GetPlayerList(Context activity) {
        // get From memory
        String str = PreferenceManager.getDefaultSharedPreferences(activity).getString(PLAYER_LIST, "defaultValue"); //sharedPref.getString(PLAYER_LIST, "[{\"name\":\"Default\",\"scores\":[],\"score\":0,\"position\":0}]");

        // DeConvert
        if (str != "defaultValue") {
            Type type = new TypeToken<ArrayList<Player>>() {
            }.getType();
            ArrayList<Player> restoreData = new Gson().fromJson(str, type);
            return restoreData;
        }
        return null;
    }

    public static void SavePlayerList(ArrayList<Player> player_list, Context activity) {
        //Convert
        String dataStr = new Gson().toJson(player_list);

        //Save
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(PLAYER_LIST, dataStr).apply();
    }

    public static String GetSetting(String setting, Context activity) {
        // get From memory
        switch (setting) {
            case "MULTIPLY":
                return PreferenceManager.getDefaultSharedPreferences(activity).getString(MULTIPLY_SETTING, "null");
            case "SHAKE":
                return PreferenceManager.getDefaultSharedPreferences(activity).getString(SHAKE_SETTING, "null");
            case "SHAKE_UP_DOWN":
                return PreferenceManager.getDefaultSharedPreferences(activity).getString(SHAKE_UP_DOWN_SETTING, "null");
            case "TURN":
                return PreferenceManager.getDefaultSharedPreferences(activity).getString(TURN_SETTING, "null");
            case "WANTED_SCORE":
                return PreferenceManager.getDefaultSharedPreferences(activity).getString(WANTED_SCORE_SETTING, "null");
        }
        return null;
    }

    public static void SaveFocusedPlayer(String value, Context activity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(PLAYER_FOCUS, value).apply();
    }

    public static String GetFocusedPlayer(Context activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(PLAYER_FOCUS, "defaultValue");
    }

    public static void SaveSetting(String setting, String value, Context activity) {

        switch (setting) {
            case "MULTIPLY":
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MULTIPLY_SETTING, value).apply();
                break;
            case "SHAKE":
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(SHAKE_SETTING, value).apply();
                break;
            case "SHAKE_UP_DOWN":
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(SHAKE_UP_DOWN_SETTING, value).apply();
                break;
            case "TURN":
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(TURN_SETTING, value).apply();
                break;
            case "WANTED_SCORE":
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(WANTED_SCORE_SETTING, value).apply();
                break;
        }
    }

    public static void SaveAllSettings(String multiply, String shake, String shake_up_down, String turn, String wantedscore, Context activity) {

        // Multiply Setting Save
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MULTIPLY_SETTING, multiply).apply();
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(SHAKE_SETTING, shake).apply();
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(SHAKE_UP_DOWN_SETTING, shake_up_down).apply();
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(TURN_SETTING, turn).apply();
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(WANTED_SCORE_SETTING, wantedscore).apply();
    }


}
