package com.driver3.driverapp.ui;

import android.app.Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.driver3.driverapp.AccountCreateActivity;
import com.driver3.driverapp.Item;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.R;
import com.driver3.driverapp.Util;
import com.driver3.driverapp.ui.PreviousPurchaseFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PreviousPurchaseArrayAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> items;
    private final MainActivity ma;
    private final PreviousPurchaseFragment nf;
    public PreviousPurchaseArrayAdapter(Context context, ArrayList<Item> data, MainActivity ma, PreviousPurchaseFragment nf) {
        super(context, R.layout.history_list_view, data);
        // TODO Auto-generated constructor stub
        this.ma = ma;
        this.context=context;
        this.items = data;
        this.nf = nf;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.history_list_view, null,true);
        int points = 0;
        try {
            points = Integer.parseInt(ma.getUserInfo().getString("Points"));
        }
        catch (JSONException j){

        }
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        Button button = (Button) rowView.findViewById(R.id.refund);
        titleText.setText(items.get(position).getTitle());
        Picasso.get().load(items.get(position).getPictureURL()).into(imageView);
        price.setText(items.get(position).getPrice() + " Points");
        int finalPoints = points;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest";
                String fieldNames[] = {"DriverID", "ProductID", "Cost"};
                String types[] = {"S", "S", "N"};
                String values[] = {ma.getUserId(), items.get(position).getProductId(), String.valueOf(items.get(position).getPrice())};
                RequestQueue queue = Volley.newRequestQueue(getContext());
                JSONObject rb = new JSONObject();
                try {
                    rb = Util.jsonResult("Driver3MainTable1", fieldNames, types, values, "5");
                }
                catch(JSONException j){

                }
                Log.e("WORd", rb.toString());
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, rb,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                ma.setPoints(finalPoints + items.get(position).getPrice());
                                nf.removeProduct(position);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("KEVON", "KEVON");
                    }
                });

                queue.add(jsonObjReq);
            }
        });
        return rowView;

    };
}