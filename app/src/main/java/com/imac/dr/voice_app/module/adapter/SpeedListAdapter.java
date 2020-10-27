package com.imac.dr.voice_app.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.database.data.SpeedDataStricture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by isa on 2017/4/26.
 */

public class SpeedListAdapter extends RecyclerView.Adapter<SpeedListAdapter.SpeedListAdapterViewHolder> {

    private Context mContext;
    private SpeedDataStricture[] mData;
    private OnRecyclerViewEvent mOnRecyclerViewEvent;

    public SpeedListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SpeedListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_speed_socure, parent, false);
        return new SpeedListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeedListAdapterViewHolder holder, int position) {
        holder.mItemText.setText(timeMillinFormat(mData[position].getStartTime()));
    }

    private String timeMillinFormat(String time) {
        long timeMillition = Long.valueOf(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
        return simpleDateFormat.format(new Date(timeMillition));
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        else return mData.length;
    }

    public void setData(SpeedDataStricture[] data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnRecyclerViewEvent(OnRecyclerViewEvent onRecyclerViewEvent) {
        mOnRecyclerViewEvent = onRecyclerViewEvent;
    }

    class SpeedListAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemText;

        public SpeedListAdapterViewHolder(View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnRecyclerViewEvent)
                        mOnRecyclerViewEvent.onItemClick(mData[getAdapterPosition()]);
                }
            });
        }
    }

    public interface OnRecyclerViewEvent {
        void onItemClick(SpeedDataStricture speedData);
    }
}
