package com.driver3.driverapp.ui.home;

import android.app.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.driver3.driverapp.PointChange;
import com.driver3.driverapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PointsArrayAdapter extends ArrayAdapter<PointChange> {

    private final Context context;
    private ArrayList<PointChange> pcs;

    public PointsArrayAdapter(Context context, ArrayList<PointChange> pcs) {
        super(context, R.layout.points_list_view, pcs);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.pcs = pcs;
    }

    public View getView(int position,View view,ViewGroup parent) {
        Log.e("NO", "NO");
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.points_list_view, null,true);

        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView pointChange = (TextView) rowView.findViewById(R.id.pointChange);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        date.setText(pcs.get(position).getDate());
        pointChange.setText("Point Change: " + pcs.get(position).getPointsAdded());
        description.setText("Reason: " +  pcs.get(position).getReason());
        return rowView;

    };
}