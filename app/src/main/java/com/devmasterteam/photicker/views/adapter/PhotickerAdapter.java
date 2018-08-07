package com.devmasterteam.photicker.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.devmasterteam.photicker.R;
import com.devmasterteam.photicker.views.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotickerAdapter extends RecyclerView.Adapter<PhotickerAdapter.ViewHolder>{


    List<Integer> imageList;
    private OnItemAdapter onItemAdapter;

    public PhotickerAdapter(List<Integer> imageList) {
        this.imageList = imageList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer drawable = imageList.get(position);

        Glide.with(holder.itemView).load(drawable).into(holder.imageViewAdapter);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setOnItemAdapter(OnItemAdapter onItemAdapter) {
        this.onItemAdapter = onItemAdapter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = ViewHolder.class.getName();
        public @BindView(R.id.imageViewAdapter) ImageView imageViewAdapter;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageViewAdapter.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v != null){
                Log.i(TAG, "onCLick " + getAdapterPosition());
                onItemAdapter.itemClick(getAdapterPosition());
            }
        }

    }

    public interface OnItemAdapter{

        void itemClick(int position);

    }

}
