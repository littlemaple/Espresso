package com.rainbow.blue.espresso.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbow.blue.espresso.R;
import com.rainbow.blue.espresso.bean.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2015/9/11.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Message> dataSet = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public List<Message> getDataSet() {
        return this.dataSet;
    }

    public void refresh(List<Message> dataSet) {
        if (dataSet == null)
            return;
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (dataSet.size() == 0) {
            Toast.makeText(context, "no data any more", Toast.LENGTH_SHORT).show();
            return;
        }
        dataSet.remove(position);
        notifyDataSetChanged();
    }

    public void add(Message message) {
        dataSet.add(0, message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(dataSet.get(position).getImage()).into(holder.imageView);
        holder.tv.setText(dataSet.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

    }
}
