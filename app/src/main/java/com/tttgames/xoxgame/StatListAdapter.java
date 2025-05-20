package com.tttgames.xoxgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StatListAdapter extends ArrayAdapter<String> {

    public StatListAdapter(Context context, List<String> stats) {
        super(context, 0, stats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String stat = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView tvStat = convertView.findViewById(android.R.id.text1);
        tvStat.setText(stat);

        return convertView;
    }
}
