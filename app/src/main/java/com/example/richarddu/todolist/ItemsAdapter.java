package com.example.richarddu.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Richard Du on 5/7/2017.
 */
public class ItemsAdapter extends ArrayAdapter<ToDoItem> {
    public ItemsAdapter(Context context, ArrayList<ToDoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvToDoItem = (TextView) convertView.findViewById(R.id.tvToDoItem);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvToDoItem.setText(item.text);
        switch(item.priority) {
            case 0: tvPriority.setText("Low");
                    tvPriority.setTextColor(Color.GREEN);
                break;
            case 1: tvPriority.setText("Medium");
                    tvPriority.setTextColor(Color.YELLOW);
                break;
            case 2: tvPriority.setText("High");
                    tvPriority.setTextColor(Color.RED);
                break;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
