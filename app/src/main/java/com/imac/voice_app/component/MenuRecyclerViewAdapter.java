package com.imac.voice_app.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imac.voice_app.R;
import com.imac.voice_app.core.Ruler;

/**
 * Created by isa on 2016/9/7.
 */
public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private Callback callback;
    private Ruler ruler;

    public MenuRecyclerViewAdapter(Context context) {
        this.context = context;
        ruler = new Ruler(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        item.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ruler.getH(20)));
//        item.requestLayout();

        final MyViewHolder viewHolder = new MyViewHolder(item);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClick(viewHolder.itemView);
                }
            }
        });
        return viewHolder;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(context.getResources().getStringArray(R.array.menu_text_array)[position]);
    }

    @Override
    public int getItemCount() {
        return context.getResources().getStringArray(R.array.menu_text_array).length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView text;
        public RelativeLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
//            layout = (RelativeLayout) itemView.findViewById(R.id.menu);
            icon = (ImageView) itemView.findViewById(R.id.menu_icon);
            text = (TextView) itemView.findViewById(R.id.menu_text);
        }
    }

    public interface Callback {
        void onItemClick(View view);
    }
}
