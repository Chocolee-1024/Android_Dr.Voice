package com.imac.voice_app.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.imac.voice_app.core.PreferencesHelper;

/**
 * Created by isa on 2016/10/11.
 */
public class SharePreferencesManager extends PreferencesHelper {
    private static SharePreferencesManager sharePreferencesManager;

    private SharePreferencesManager(Context context) {
        super(context);
    }

    public static SharePreferencesManager getInstance(Context context) {
        if (sharePreferencesManager == null) {
            return sharePreferencesManager = new SharePreferencesManager(context);
        } else {
            return sharePreferencesManager;
        }
    }

    public void save(Type type, String key, Object vale) {
        super.save(type, key, vale);
    }

    public Object get(String key, Type type) {
        return super.get(key, type);
    }

    public void clearAll() {
        SharedPreferences store = getContext().getSharedPreferences(getClassName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = store.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public String getClassName() {
        return getContext().getPackageName();
    }
}
