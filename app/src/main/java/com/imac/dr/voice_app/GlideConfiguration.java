package com.imac.dr.voice_app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;


//將 Glide RGB565編碼轉為ARGB_8888，解決圖片變綠問題
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }
 
    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
