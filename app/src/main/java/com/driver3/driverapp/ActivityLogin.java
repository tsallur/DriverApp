package com.driver3.driverapp;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends Activity {
    String awsKey = "ASIAS52LCPUPG6ABAO5K";
    String awsSecretKey = "4m4fP+H6fhU/iHCEbpMqLuyVfJoO07HRrFX8MWaT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
        setContentView(R.layout.login_activity);
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        ImageButton iv = (ImageButton) findViewById(R.id.amazon);
        Activity a = this;
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amplify.Auth.signInWithSocialWebUI(
                        AuthProvider.amazon(),
                        a,
                        result -> getInfo(result.toString()),
                        error ->  Amplify.Auth.signInWithSocialWebUI(
                                AuthProvider.amazon(),
                                a,
                                result -> getInfo(result.toString()),
                                error2 -> Log.e("AuthQuickstart", error2.toString())
                        )
                );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data);
        }
    }

    protected void getInfo(String result){
        BasicAWSCredentials bac = new BasicAWSCredentials(awsKey, awsSecretKey);
        Amplify.Auth.fetchAuthSession(test -> Log.i("Test", test.toString()), e -> Log.i("Error", "Error"));
        Amplify.Auth.fetchUserAttributes(
                (attributes) -> {
                    try {
                        JSONArray ar = new JSONArray(attributes.get(1).getValue());
                        String userId = ((JSONObject) ar.get(0)).getString("userId");
                        Log.i("AuthDemo", userId);
                        RequestQueue queue = Volley.newRequestQueue(this);
                        String url ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=1&ID=" + userId;

// Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        try {
                                            Log.e("AJFAFA", response);
                                            JSONObject rspJson = new JSONObject(response);
                                            if(rspJson.getInt("Count") == 0){
                                                try {
                                                    Intent k = new Intent(ActivityLogin.this, AccountCreateActivity.class);
                                                    k.putExtra("userId", userId);
                                                    startActivity(k);
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else{
                                                JSONObject userInfo = rspJson.getJSONArray("Items").getJSONObject(0);
                                                try {
                                                    Intent k = new Intent(ActivityLogin.this, MainActivity.class);
                                                    k.putExtra("userId", userId);
                                                    k.putExtra("userInfo", userInfo.toString());
                                                    startActivity(k);
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        catch(JSONException j){
                                            Log.e("JSON", j.toString());
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(stringRequest);
                    }
                    catch (JSONException j) {

                    }


// Add the request to the RequestQueue.

                    },
                (error) -> {Log.e("AuthDemo", "Failed to fetch user attributes.", error);
                }
        );
    }
}
