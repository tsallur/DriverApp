package com.driver3.driverapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AccountCreateActivity extends Activity {

    Button confirm;
    EditText address;
    EditText firstName;
    EditText lastName;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create_activity);
        confirm = (Button) findViewById(R.id.createAccount);
        address = (EditText) findViewById(R.id.address);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = getIntent().getStringExtra("userId");
                String add = address.getText().toString();
                String fn = firstName.getText().toString();
                String ln = lastName.getText().toString();
                String fieldNames[] = {"ID", "AccountType", "FirstName", "LastName", "Address", "SponsorID", "Status", "Points"};
                String values[] = {userId, "Driver", fn, ln, add, "0", "Active", "0"};
                String types[] = {"S", "S", "S", "S", "S", "S", "S", "N"};
                JSONObject rb = new JSONObject();
                try {
                    rb = Util.jsonResult("Driver3MainTable1", fieldNames, types, values, "1");
                } catch (JSONException j) {

                }
                RequestQueue queue = Volley.newRequestQueue(AccountCreateActivity.this);
                String url = "https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest";
                Log.i("TEST", rb.toString());
// Request a string response from the provided URL.
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, rb,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("MAX", "MAX");
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
    }

}
