package com.example.menu.ui.mainHome.training;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Training;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Training> list;
    private OnItemClickListener mOnItemClickListener;

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    ListAdapter(Context context, ArrayList<Training> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.item_training, parent, false);
        return new ViewHolder(inflater);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trainingTitle.setText(list.get(position).getTrainingTitle());
        holder.publishTime.setText(list.get(position).getPublishTime());
        holder.clickNum.setText(list.get(position).getTrainingClickNum() + "点击");
        Glide.with(context)
                .load(list.get(position).getTrainingImagePath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.load_error_24dp)
                .crossFade()
                .into(holder.trainingImage);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, list);
                holder.clickNum.setText(Integer.parseInt(holder.clickNum.getText().toString().replace("点击", "")) + 1 + "点击");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainingTitle, publishTime, clickNum;
        ImageView trainingImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainingTitle = itemView.findViewById(R.id.Training_Title);
            publishTime = itemView.findViewById(R.id.publish_time);
            clickNum = itemView.findViewById(R.id.click_num);
            trainingImage = itemView.findViewById(R.id.training_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<Training> list);
        void onItemLongClick(View view, int position);
    }
}
