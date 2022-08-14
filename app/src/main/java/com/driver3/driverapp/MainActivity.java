package com.driver3.driverapp;

import android.os.Bundle;

import com.driver3.driverapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    JSONObject userInfo;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        userId = getIntent().getStringExtra("userId");
        try {
            userInfo = new JSONObject(getIntent().getStringExtra("userInfo"));
        }
        catch(JSONException j){

        }
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }
    public void setAddress(String newAddress) {
        try {
            userInfo.put("Address", newAddress);
        }
        catch(JSONException j){

        }
    }
    public void setPoints(int points){
        try {
            userInfo.put("Points", points);
        }catch(JSONException j){

        }
    }
    public String getUserId() {
        return userId;
    }
}