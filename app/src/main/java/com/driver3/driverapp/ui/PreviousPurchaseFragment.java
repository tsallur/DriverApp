package com.driver3.driverapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver3.driverapp.AccountCreateActivity;
import com.driver3.driverapp.Item;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.R;
import com.driver3.driverapp.Util;
import com.driver3.driverapp.ui.SettingsFragment;
import com.driver3.driverapp.ui.home.PointsArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreviousPurchaseFragment extends Fragment {

    private ListView listview;
    MainActivity ma;
    private String userId;
    private PreviousPurchaseArrayAdapter ppf;
    private ArrayList<Item> values = new ArrayList<Item>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ma = (MainActivity) getActivity();
        View root = inflater.inflate(R.layout.fragment_previous_purchase, container, false);
        listview = (ListView)root.findViewById(R.id.list);
        userId = ma.getUserId();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=9&DriverID=" + userId;
        Log.e("TEST", "TESt");
        ppf = new PreviousPurchaseArrayAdapter(getContext(), values, ma, this);
        listview.setAdapter(ppf);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("Items");
                            for(int i = 0; i < arr.length(); i++){
                                String status = arr.getJSONObject(i).getString("Status");
                                if(status.equals("In Progress")) {
                                    String productID = arr.getJSONObject(i).getString("ProductID");
                                    String url ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=10&DriverID=" + userId + "&ProductID=" + productID;
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try{
                                                        Log.e("RETURN", response);
                                                        JSONObject productInfo = new JSONObject(response).getJSONArray("Items").getJSONObject(0);
                                                        String productId = productInfo.getString("ProductID");
                                                        int price = Integer.parseInt(productInfo.getString("Price"));
                                                        String title = productInfo.getString("Title");
                                                        String description = productInfo.getString("Description");
                                                        String pictureURL = productInfo.getString("PictureURL");
                                                        values.add(new Item(productId, description, pictureURL, price, title));
                                                        ppf.notifyDataSetChanged();

                                                    }catch(JSONException j){
                                                        Log.e("TEST", j.toString());
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("nOK", "Onk");
                                        }
                                    });
                                    queue.add(stringRequest2);
                                }
                            }

                        }catch(JSONException j){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("nOK", "Onk");
            }
        });
        queue.add(stringRequest);
        return root;
    }

    public void removeProduct(int position){
        values.remove(position);
        ppf.notifyDataSetChanged();
    }

}
