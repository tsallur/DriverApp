package com.driver3.driverapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.driver3.driverapp.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.navigation.ui.NavigationView;

public class GoalsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.goals_fragment, container, false);
        Log.e("TEST", "TEST");
        return root;
    }
}
