package com.imac.dr.voice_app.view.healtheducation;

import android.app.Activity;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.imac.dr.voice_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthEducationAdapter extends PagerAdapter {

    @BindView(R.id.health_view_pager_image)
    ImageView viewPagerImage;
    private Activity mActivity;

    public HealthEducationAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        TypedArray typedArray = mActivity.getResources().obtainTypedArray(R.array.health_viewpager_array);
        Log.d("HealthEducationAdapter", "getCount: " + typedArray.getIndexCount());
        return 7;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.layout_health_viewpager_item, null);
        ButterKnife.bind(this, view);

        TypedArray typedArray = mActivity.getResources().obtainTypedArray(R.array.health_viewpager_array);
        Glide.with(mActivity)
                .load(typedArray.getResourceId(position, -1))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewPagerImage);
        container.addView(view);
        return view;
    }
}
