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
    //要滑動view的各數
    @Override
    public int getCount() {
        //拿取噪音衛教的圖片
        TypedArray typedArray = mActivity.getResources().obtainTypedArray(R.array.health_viewpager_array);
        Log.d("HealthEducationAdapter", "getCount: " + typedArray.getIndexCount());
        return 7;
    }
    //判斷從instantiateItem返回来的View與當前的View是否對應。
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //從當前container中刪除指定位置（position）的View;
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    //做了兩件事，第一：將當前圖片添加到container中，第二：返回當前View(上面所說的object類型的id)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.layout_health_viewpager_item, null);
        ButterKnife.bind(this, view);

        TypedArray typedArray = mActivity.getResources().obtainTypedArray(R.array.health_viewpager_array);
        //載入圖片
        Glide.with(mActivity)//使用Glide的地方
                .load(typedArray.getResourceId(position, -1))//要載入的圖片
                .centerCrop()//以填满整個元件為目標,等比缩放,超過元件時將被裁剪
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//緩存圖片
                .into(viewPagerImage);//圖片放入的元件
        //把view加入container中
        container.addView(view);
        return view;
    }
}
