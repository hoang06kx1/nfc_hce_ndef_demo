package com.hoang.nfcdemoapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/31/14.
 */
public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "shared_stsc_pref";
    private Context appContext;

    public SharedPrefManager(Context context) {
        this.appContext = context;
    }

    private SharedPreferences getSharedPref() {
        if (appContext != null) {
            return appContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        }
        return null;
    }

    private SharedPreferences getSharedPref(String prefName) {
        if (appContext != null) {
            return appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        }
        return null;
    }

    public void putString(String keyName, String value) {
        SharedPreferences preferences = getSharedPref();
        if (preferences != null) {
            preferences.edit().putString(keyName, value).commit();
        }
    }

    public void putAllValues(Map<String, String> maps, boolean isClear) {
        SharedPreferences preferences = getSharedPref();
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (isClear)
                editor.clear();
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }
            editor.commit();
        }
    }

    public void putString(String prefName, String keyName, String value) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().putString(keyName, value).commit();
        }
    }

    public String getString(String keyName, String defaultValue) {
        SharedPreferences preferences = getSharedPref();
        String returnedValue = defaultValue;
        if (preferences != null) {
            returnedValue = preferences.getString(keyName, defaultValue);
        }
        return returnedValue;
    }

    public String getString(String prefName, String keyName, String defaultValue) {
        SharedPreferences preferences = getSharedPref(prefName);
        String returnedValue = defaultValue;
        if (preferences != null) {
            returnedValue = preferences.getString(keyName, defaultValue);
        }
        return returnedValue;
    }

    public void removeString(String prefName, String keyName) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().remove(keyName).commit();
        }
    }

    public boolean getBoolean(String keyName, boolean defaultValue) {
        SharedPreferences preferences = getSharedPref();
        boolean result = defaultValue;
        if (preferences != null) {
            result = preferences.getBoolean(keyName, defaultValue);
        }
        return result;
    }

    public boolean getBoolean(String prefName, String keyName, boolean defaultValue) {
        SharedPreferences preferences = getSharedPref(prefName);
        boolean result = defaultValue;
        if (preferences != null) {
            result = preferences.getBoolean(keyName, defaultValue);
        }
        return result;
    }

    public void putBoolean(String keyName, boolean value) {
        SharedPreferences preferences = getSharedPref();
        if (preferences != null) {
            preferences.edit().putBoolean(keyName, value).commit();
        }
    }

    public void putBoolean(String prefName, String keyName, boolean value) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().putBoolean(keyName, value).commit();
        }
    }

    public int getInt(String prefName, String keyName, int defaultValue) {
        SharedPreferences preferences = getSharedPref(prefName);
        int result = defaultValue;
        if (preferences != null) {
            result = preferences.getInt(keyName, defaultValue);
        }
        return result;
    }

    public void putInt(String prefName, String keyName, int value) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().putInt(keyName, value).commit();
        }
    }

    public Map<String, ?> getAllValues(String prefName) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            return preferences.getAll();
        }
        return null;
    }

    public Map<String, ?> getAllValues() {
        SharedPreferences preferences = getSharedPref();
        if (preferences != null) {
            return preferences.getAll();
        }
        return null;
    }

    public void putAllValues(String prefName, Map<String, String> maps, boolean isClear) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (isClear)
                editor.clear();
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }
            editor.commit();
        }
    }

    public void clearAllValues(String prefName) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().clear().commit();
        }
    }

    public void removeValue(String prefName, String key) {
        SharedPreferences preferences = getSharedPref(prefName);
        if (preferences != null) {
            preferences.edit().remove(key).commit();
        }
    }

    public static class ShotStateStore {

        private static final String PREFS_SHOWCASE_INTERNAL = "showcase_internal";
        private static final String INVALID_SHOT_ID = "invalid";
        private final Context context;
        String shotId = INVALID_SHOT_ID;

        public ShotStateStore(Context context) {
            this.context = context;
        }

        public boolean hasShot() {
            return isSingleShot() && context
                    .getSharedPreferences(PREFS_SHOWCASE_INTERNAL, Context.MODE_PRIVATE)
                    .getBoolean("hasShot" + shotId, false);
        }

        boolean isSingleShot() {
            return !shotId.equals(INVALID_SHOT_ID);
        }

        public void setSingleShot(String shotId) {
            this.shotId = shotId;
        }

        public void storeShot() {
            if (isSingleShot()) {
                SharedPreferences internal = context.getSharedPreferences(PREFS_SHOWCASE_INTERNAL, Context.MODE_PRIVATE);
                internal.edit().putBoolean("hasShot" + shotId, true).apply();
            }
        }

    }
}
