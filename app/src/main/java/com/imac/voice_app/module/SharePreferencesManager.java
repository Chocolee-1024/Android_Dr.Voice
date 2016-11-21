package com.imac.voice_app.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.imac.voice_app.core.PreferencesHelper;
import com.imac.voice_app.module.database.SqliteManager;

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
            synchronized (SqliteManager.class) {
                if (sharePreferencesManager == null) {
                    sharePreferencesManager = new SharePreferencesManager(context);
                }
            }
        }
        return sharePreferencesManager;
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
