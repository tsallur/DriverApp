package com.driver3.driverapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.Volley;
import com.driver3.driverapp.AccountCreateActivity;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.R;
import com.driver3.driverapp.Util;
import com.driver3.driverapp.ui.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsFragment extends Fragment {

    EditText address;
    Button delete;
    Button updateAddress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        address = (EditText) root.findViewById(R.id.address);
        updateAddress = (Button) root.findViewById(R.id.update_address);
        delete = (Button) root.findViewById(R.id.delete_account);
        MainActivity activity = (MainActivity) getActivity();
        JSONObject userInfo = activity.getUserInfo();
        String add = "";
        try {
            add = userInfo.getString("Address");
        }
        catch(JSONException j){

        }
        address.setText(add);
        // Post 6 and AccountType and ID
        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = activity.getUserId();
                String newAddress = address.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                // Post
                String url = "https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest";
                String fieldNames[] = {"ID", "AccountType", "Address"};
                String types[] = {"S", "S", "S"};
                String values[] = {userId, "Driver", newAddress};
                JSONObject rb = new JSONObject();
                try {
                    rb = Util.jsonResult("Driver3MainTable1", fieldNames, types, values, "1");
                }
                catch(JSONException j){

                }
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                        url, rb,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                activity.setAddress(newAddress);
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest";
                String fieldNames[] = {"AccountType", "ID"};
                String types[] = {"S", "S"};
                String values[] = {"Driver", activity.getUserId()};
                RequestQueue queue = Volley.newRequestQueue(getContext());
                JSONObject rb = new JSONObject();
                try {
                    rb = Util.jsonResult("Driver3MainTable1", fieldNames, types, values, "6");
                }
                catch(JSONException j){

                }
                Log.e("WORd", rb.toString());
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, rb,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

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
        return root;
    }

}
