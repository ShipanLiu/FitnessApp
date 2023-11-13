/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.a07.R;
import com.example.a07.entity.SportEntity;

import java.util.List;

public class SportArchivAdapter extends BaseAdapter {

    // current context
    private Context mContext;
    // data list
    private List<SportEntity> sportList;

    public SportArchivAdapter(Context mContext, List<SportEntity> sportList) {
        this.mContext = mContext;
        this.sportList = sportList;
    }

    @Override
    public int getCount() {
        return sportList.size();
    }

    @Override
    public Object getItem(int position) {
        return sportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            // 获取布局文件item_cart.xml的根视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sport_item, null);
            holder.tv_name = convertView.findViewById(R.id.tv_sport_name);
            holder.tv_timeAndDateStamp = convertView.findViewById(R.id.tv_sport_timeStamp);
            holder.tv_duration = convertView.findViewById(R.id.tv_sport_duration);
            holder.tv_moodScore = convertView.findViewById(R.id.tv_moodScore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set the background color based on position
        if (position % 2 == 0) {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
        }

        SportEntity sportEntity = sportList.get(position);
        holder.tv_name.setText(sportEntity.getName());
        holder.tv_timeAndDateStamp.setText(sportEntity.getTimeAndDateStamp());
        holder.tv_duration.setText(sportEntity.getDuration());
        holder.tv_moodScore.setText(Integer.toString(sportEntity.getMoodScore()));
        return convertView;
    }

    public final class ViewHolder {
        public TextView tv_name;
        public TextView tv_timeAndDateStamp;
        public TextView tv_duration;
        public TextView tv_moodScore;
    }
}
