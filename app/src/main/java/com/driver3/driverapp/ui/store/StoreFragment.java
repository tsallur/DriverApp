package com.driver3.driverapp.ui.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.driver3.driverapp.Item;
import com.driver3.driverapp.MainActivity;
import com.driver3.driverapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreFragment extends Fragment {


    private ListView listview;
    private TextView title;
    private Button btn_prev;
    private Button btn_next;
    private TextView pointTextView;
    private ArrayList<String> data;
    ArrayAdapter<Item> sd;

    private int pageCount ;

    /**
     * Using this increment value we can move the listview items
     */
    private int increment = 0;
    private int points = 0;
    /**
     * Here set the values, how the ListView to be display
     *
     * Be sure that you must set like this
     *
     * TOTAL_LIST_ITEMS > NUM_ITEMS_PAGE
     */
    public String userId;
    public int NUM_ITEMS_PAGE   = 4;
    View root;
    MainActivity ma;
    private ArrayList<Item> items = new ArrayList<Item>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ma = (MainActivity) getActivity();
        root = inflater.inflate(R.layout.fragment_store, container, false);
        listview = (ListView)root.findViewById(R.id.list);
        btn_prev	 = (Button)root.findViewById(R.id.prev);
        btn_next	 = (Button)root.findViewById(R.id.next);
        title	 = (TextView)root.findViewById(R.id.title);
        pointTextView = (TextView) root.findViewById(R.id.points);
        try {
            String pointsString = ma.getUserInfo().getString("Points");
            points = Integer.parseInt(pointsString);
        }catch(JSONException j){

        }
        pointTextView.setText("Your Current Points: " + points);
        btn_prev.setEnabled(false);
        userId = ma.getUserId();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://0h0vv7lnm7.execute-api.us-east-1.amazonaws.com/default/LambdaBackendTest?query=8&ID=" + userId;
        Log.e("TEST", "TESt");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      try{
                          JSONObject json  = new JSONObject(response);
                          JSONArray arr = json.getJSONArray("Items");
                          for(int i = 0; i < arr.length(); i++){
                              Log.e("WOW", "WOW");
                              String productId = arr.getJSONObject(i).getString("ProductID");
                              int price = arr.getJSONObject(i).getInt("Price");
                              String title = arr.getJSONObject(i).getString("Title");
                              String description = arr.getJSONObject(i).getString("Description");
                              String pictureURL = arr.getJSONObject(i).getString("PictureURL");
                              items.add(new Item(productId, description, pictureURL, price, title));
                              Log.e("WHERE", "THERE");
                          }
                          int itemCount = items.size();
                          int val = itemCount % NUM_ITEMS_PAGE;
                          val = val == 0 ? 0 : 1;
                          pageCount = itemCount / NUM_ITEMS_PAGE + val;
                          loadList(0);

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
// Request a string response from the provided URL.


        /**
         * this block is for checking the number of pages
         * ====================================================
         */

        /**
         * =====================================================
         */

        /**
         * The ArrayList data contains all the list items
         */

        Log.v("HEY", "HEY");
        btn_next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                increment++;
                loadList(increment);
                CheckEnable();
                btn_prev.setEnabled(true);
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                increment--;
                loadList(increment);
                CheckEnable();
                btn_next.setEnabled(true);
            }
        });
        return root;
    }

    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        Log.e("TEMP", "TEMP");
        if(increment+1 == pageCount)
        {
            btn_next.setEnabled(false);
        }
        else if(increment == 0)
        {
            btn_prev.setEnabled(false);
        }
        else
        {
            Log.e("HMMM", "HMMMMM");
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }

    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(int number)
    {
        Log.d("WOAH", "WOAH");
        ArrayList<Item> sort = new ArrayList<Item>();
        title.setText("Page "+(number+1)+" of "+pageCount);

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<items.size())
            {
                sort.add(items.get(i));
            }
            else
            {
                break;
            }
        }

        sd = new StoreArrayAdapter(getContext(), sort, ma, this);
        listview.setAdapter(sd);
    }

    public void changePoints(){
        try {
            String pointsString = ma.getUserInfo().getString("Points");
            points = Integer.parseInt(pointsString);
        }catch(JSONException j){

        }
        pointTextView.setText("Your Current Points: " + points);
    }
}