package com.imac.voice_app.module;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by isa on 2016/9/8.
 */
public class FontManager {
    public static final String LIGHT = "light";
    public static final String MEDIUM = "medium";
    public static final String NORMAL = "normal";
    public static final String REGULAR = "regular";

    public static void setFont(Context context, String type, @NonNull TextView... views) {
        Typeface result;
        switch (type) {
            case "light":
                result = Typeface.createFromAsset(context.getAssets(), "font/SourceHanSansTWHK-Light.otf");
                break;
            case "medium":
                result = Typeface.createFromAsset(context.getAssets(), "font/SourceHanSansTWHK-Medium.otf");
                break;
            case "normal":
                result = Typeface.createFromAsset(context.getAssets(), "font/SourceHanSansTWHK-Normal.otf");
                break;
            case "regular":
                result = Typeface.createFromAsset(context.getAssets(), "font/SourceHanSansTWHK-Regular.otf");
                break;
            default:
                result = Typeface.createFromAsset(context.getAssets(), "font/SourceHanSansTWHK-Normal.otf");
                break;
        }
        for (TextView view : views) {
            view.setTypeface(result);
        }
    }
}
