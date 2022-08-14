package com.driver3.driverapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.PointChange;
import com.driver3.driverapp.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView pointChangesListView;
    private ListView goalsListView;
    private TextView name;
    private TextView points;
    private TextView sponsor;
    private boolean showGoals;
    private TextView header;
    private Button goals;
    private HomeViewModel homeViewModel;
    ArrayAdapter<PointChange> sd;
    private static final String SET_LABEL = "Monthly Performance";
    private ArrayList<String> months = new ArrayList<>();
    private HorizontalBarChart chart;
    ArrayList<PointChange> values = new ArrayList<PointChange>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        chart = root.findViewById(R.id.horizontal_bar_chart);
        name = root.findViewById(R.id.name);
        header = root.findViewById(R.id.header);
        points = root.findViewById(R.id.points);
        pointChangesListView = root.findViewById(R.id.point_changes_listview);
        goalsListView = root.findViewById(R.id.goals_listview);
        showGoals = false;
        sponsor = root.findViewById(R.id.sponsor);
        goals = root.findViewById(R.id.goals);
        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                if(showGoals == false){
                    pointChangesListView.setVisibility(View.GONE);
                    goalsListView.setVisibility(View.VISIBLE);
                    showGoals = true;
                    header.setText("Goals");
                    goals.setText("View Point Changes");
                    // This when show goals clicked
                }
                else{
                    goalsListView.setVisibility(View.GONE);
                    pointChangesListView.setVisibility(View.VISIBLE);
                    showGoals = false;
                    header.setText("Point Changes");
                    goals.setText("View Goals");
                    // This when show point changes is clicked
                }
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            }
        });
        MainActivity activity = (MainActivity) getActivity();
        JSONObject userInfo = activity.getUserInfo();
        String sponsorID = "";
        try {
            name.setText(userInfo.getString("FirstName") + " " + userInfo.getString("LastName"));
            points.setText("My Current Points: " + userInfo.getString("Points"));
            sponsorID = userInfo.getString("SponsorID");
        }
        catch(JSONException j){

        }
        sd = new PointsArrayAdapter(getContext(), values);
        pointChangesListView.setAdapter(sd);
        String userId = activity.getUserId();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url2 ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=5&SponsorID=" + sponsorID;
        // Request a string response from the provided URL.
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject json  = new JSONObject(response);
                            JSONArray arr = json.getJSONArray("Items");
                            String sponsorName = arr.getJSONObject(0).getString("Name");
                            sponsor.setText("My Current Sponsor: " + sponsorName);
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
        queue.add(stringRequest2);

        String url ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=3&DriverID=" + userId;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            Log.e("AJFAFA", response);
                            JSONObject json  = new JSONObject(response);
                            JSONArray arr = json.getJSONArray("Items");
                            for(int i = 0; i < arr.length(); i++){
                                String pointsAdded = arr.getJSONObject(i).getString("PointsAdded");
                                String date = arr.getJSONObject(i).getString("TimeAdded").substring(0, 10);
                                String reason = arr.getJSONObject(i).getString("Reason");
                                values.add(new PointChange(pointsAdded, reason, date));
                            }
                            BarData data = createChartData();
                            configureChartAppearance();
                            prepareChartData(data);
                            sd.notifyDataSetChanged();
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
        return root;
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months.get((int) value);
            }
        });
    }

    private BarData createChartData() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int value1 = 0;
        int value2 = 0;
        int value3 = 0;
        for(int i = 0; i < values.size(); i++){
            if(Integer.parseInt(values.get(i).getDate().substring(5, 7)) == month){
                value1 += Integer.parseInt(values.get(i).getPointsAdded());
            }
            else if(Integer.parseInt(values.get(i).getDate().substring(5, 7)) == month - 1){
                value2 += Integer.parseInt(values.get(i).getPointsAdded());
            }
            else if(Integer.parseInt(values.get(i).getDate().substring(5, 7)) == month - 2){
                value3 += Integer.parseInt(values.get(i).getPointsAdded());
            }
        }
        months.add(getMonth(month));
        months.add(getMonth(month - 1));
        months.add(getMonth(month - 2));
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, value1));
        values.add(new BarEntry(1, value2));
        values.add(new BarEntry(2, value3));

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

    private String getMonth(int month) {
        if (month == 1) {
            return "Jan";
        } else if (month == 2) {
            return "Feb";
        } else if (month == 3) {
            return "Mar";
        } else if (month == 4) {
            return "Apr";
        } else if (month == 5) {
            return "May";
        } else if (month == 6) {
            return "June";
        } else if (month == 7) {
            return "July";
        } else if (month == 8) {
            return "Aug";
        } else if (month == 9) {
            return "Sep";
        } else if (month == 10) {
            return "Oct";
        } else if (month == 11) {
            return "Nov";
        } else if (month == 12) {
            return "Dec";
        }
        else{
            return "";
        }
    }
}