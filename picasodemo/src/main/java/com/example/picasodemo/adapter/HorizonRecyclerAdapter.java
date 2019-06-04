package com.example.picasodemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.picasodemo.R;
import com.example.picasodemo.bean.CatBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static String TAG = HorizonRecyclerAdapter.class.getSimpleName();
    private List<CatBean> mData;
    private Context context;


    public HorizonRecyclerAdapter(Context context, List<CatBean> data) {
        this.context = context;
        this.mData = data;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClicked(OnItemClickListener onItemClicked) {
        this.onItemClickListener = onItemClicked;
    }

    class LocalViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public LocalViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.view_image_item, parent, false);
        return new LocalViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LocalViewHolder viewHolder = (LocalViewHolder) holder;
        //使用picasso加载图片
        Picasso.with(context)
                .load(mData.get(position).getImageUrl())
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imageView);
        viewHolder.textView.setText(mData.get(position).getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
