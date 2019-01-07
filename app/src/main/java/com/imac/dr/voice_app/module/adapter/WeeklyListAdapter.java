package com.imac.dr.voice_app.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imac.dr.voice_app.R;
import com.imac.dr.voice_app.module.database.data.WeeklyDataStructure;

/**
 * Created by isa on 2017/4/24.
 */

public class WeeklyListAdapter extends RecyclerView.Adapter<WeeklyListAdapter.WeeklyAdapterViewHolder> {
    private Context mContext;
    private OnRecyclerViewTItemClick mOnRecyclerViewTItemClick;
    private WeeklyDataStructure[] mData;

    public WeeklyListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public WeeklyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new WeeklyAdapterViewHolder(inflater.inflate(R.layout.item_weekly_score, parent, false));
    }

    @Override
    public void onBindViewHolder(WeeklyAdapterViewHolder holder, int position) {
        holder.mTextView.setText(mData[position].getDate().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        else return mData.length;
    }

    public void setData(WeeklyDataStructure[] data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnRecyclerViewTItemClick(OnRecyclerViewTItemClick onRecyclerViewTItemClick) {
        mOnRecyclerViewTItemClick = onRecyclerViewTItemClick;
    }

    class WeeklyAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public WeeklyAdapterViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnRecyclerViewTItemClick)
                        mOnRecyclerViewTItemClick.onItemClick(mData[getAdapterPosition()].getSoundTopicPoint()
                                , mData[getAdapterPosition()].getWeeklyTopicPoint()
                                , mData[getAdapterPosition()].getDate()
                                , mData[getAdapterPosition()].getSoundTopic());
                }
            });
        }
    }

    public interface OnRecyclerViewTItemClick {
        void onItemClick(String soundData, String selfAssessmentData, String Date, String soundTopic);
    }
}
