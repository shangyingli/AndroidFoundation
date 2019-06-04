package com.example.picasodemo.adapter;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.picasodemo.R;
import com.example.picasodemo.bean.CatBean;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class NormalRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchAdapter {

    public final static String TAG = NormalRecycleAdapter.class.getSimpleName();
    private List<CatBean> mData;
    private Context context;


    public NormalRecycleAdapter(Context context, List<CatBean> data) {
        this.context = context;
        this.mData = data;
    }

    public void addData(int position) {
        CatBean catBean = mData.get(0);
        catBean.setName("Insert one");
        mData.add(position, catBean);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        Log.d(TAG, "COUNT : " + mData.size());
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Log.d(TAG, "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.view_cats_item, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "POSITION : " + position);
        NormalViewHolder viewHolder = (NormalViewHolder) holder;
        //使用picasso加载图片
        Picasso.with(context)
                .load(mData.get(position).getImageUrl())
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imageView);
        viewHolder.catNameView.setText(mData.get(position).getName());
        viewHolder.catDescView.setText(mData.get(position).getDesc());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public void onItemDragged(int from, int to) {
        Collections.swap(mData, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemSwiped(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView catNameView;
        private TextView catDescView;
        public LinearLayout mCatLayout;
        public LinearLayout mDeleteLayout;
        public NormalViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "NormalViewHolder");
            imageView = itemView.findViewById(R.id.cat_img);
            catNameView = itemView.findViewById(R.id.cat_name);
            catDescView = itemView.findViewById(R.id.cat_desc);
            mDeleteLayout = (LinearLayout)itemView.findViewById(R.id.ll_delete);
            mCatLayout = (LinearLayout) itemView.findViewById(R.id.cat_layout);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
