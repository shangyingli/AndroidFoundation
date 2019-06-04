package com.example.contentproviderdemo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contentproviderdemo.R;
import com.example.contentproviderdemo.bean.FreezeBean;

import java.util.List;
import java.util.zip.Inflater;

public class FreezeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FreezeBean> freezeBeans;
    private OnItemClickListener itemClickListener;

    public FreezeAdapter(List<FreezeBean> freezeBeans) {
        this.freezeBeans = freezeBeans;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public class FreezeViewHolder extends RecyclerView.ViewHolder {

        private ImageView appIconView;
        private TextView appNameView;
        private ImageView notifyIcon;

        public FreezeViewHolder(View itemView) {
            super(itemView);
            appIconView = itemView.findViewById(R.id.app_icon);
            appNameView = itemView.findViewById(R.id.app_name);
            notifyIcon = itemView.findViewById(R.id.notify_icon);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.view_freeze, parent, false);
        return new FreezeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final FreezeViewHolder viewHolder = (FreezeViewHolder) holder;
        Drawable appIcon = freezeBeans.get(position).getAppIcon();
        String appName = freezeBeans.get(position).getAppName();
        viewHolder.appIconView.setImageDrawable(appIcon);
//        viewHolder.appIconView.setOnClickListener(); //对单个控件设置监听
        viewHolder.appNameView.setText(appName);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(viewHolder.notifyIcon);
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return freezeBeans.size();
    }
}
