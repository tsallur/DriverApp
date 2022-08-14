package com.driver3.driverapp.ui.store;

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
import com.driver3.driverapp.Item;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.R;
import com.driver3.driverapp.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class StoreArrayAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> items;
    private final MainActivity ma;
    private final StoreFragment nf;
    public StoreArrayAdapter(Context context, ArrayList<Item> data, MainActivity ma, StoreFragment nf) {
        super(context, R.layout.store_list_view, data);
        // TODO Auto-generated constructor stub
        this.ma = ma;
        this.context=context;
        this.items = data;
        this.nf = nf;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View rowView=inflater.inflate(R.layout.store_list_view, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        Button button = (Button) rowView.findViewById(R.id.purchase);
        titleText.setText(items.get(position).getTitle());
        Picasso.get().load(items.get(position).getPictureURL()).into(imageView);
        description.setText(items.get(position).getDescription());
        price.setText(items.get(position).getPrice() + " Points");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int points = 0;
                String userId = "";
                try{
                    points = Integer.parseInt(ma.getUserInfo().getString("Points"));
                    userId = ma.getUserId();
                }catch(JSONException j){

                }
                if (points >= items.get(position).getPrice()) {
                    String[] keys = {"DriverID", "ProductID", "Cost"};
                    String values[] = {userId, items.get(position).getProductId(), String.valueOf(items.get(position).getPrice())};
                    String types[] = {"S", "S", "N"};
                    JSONObject rb = new JSONObject();
                    try {
                        rb = Util.jsonResult("DriverPurchases", keys, types, values, "3");
                    } catch (JSONException j) {

                    }
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = "https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest";
                    // Request a string response from the provided URL.
                    int finalPoints = points;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, rb,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    ma.setPoints(finalPoints - items.get(position).getPrice());
                                    nf.changePoints();
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("KEVON", "KEVON");
                        }
                    });

                    queue.add(jsonObjReq);
                }
            }
        });
        return rowView;

    };
}