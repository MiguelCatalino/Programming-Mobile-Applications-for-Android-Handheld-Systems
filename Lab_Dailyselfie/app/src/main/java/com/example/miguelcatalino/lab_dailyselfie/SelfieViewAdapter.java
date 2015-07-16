package com.example.miguelcatalino.lab_dailyselfie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by miguelcatalino on 7/11/15.
 */
public class SelfieViewAdapter extends BaseAdapter {
    private ArrayList<ImageRecord> list = new ArrayList<ImageRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SelfieViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        ViewHolder holder;
        ImageRecord current = list.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.activity_list, parent, false);
            holder.selfie = (ImageView) newView.findViewById(R.id.i_selfie);
            holder.name = (TextView) newView.findViewById(R.id.i_name);
            holder.time = (TextView) newView.findViewById(R.id.i_time);

            newView.setTag(holder);
        } else {
            holder = (ViewHolder) newView.getTag();
        }
        holder.selfie.setImageBitmap(current.getSelfie());
        holder.name.setText(current.getName());
        holder.time.setText(current.getTime());

        return newView;
    }

    static class ViewHolder {

        ImageView selfie;
        TextView name;
        TextView time;

    }

    public void add(ImageRecord item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<ImageRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }

}

