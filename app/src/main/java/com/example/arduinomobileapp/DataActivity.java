package com.example.arduinomobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {
    DB_local db = new DB_local(DataActivity.this, null, 1);
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String url_data = "http://issamdata.alwaysdata.net/select.php";
    String url_api_data = "https://api.thingspeak.com/channels/1001656/fields/1.json?api_key=Y8RRGNGOEZ1X9EBP&fbclid=IwAR2OAMaI5z6DXhqw07gDmgHgdvgJMYXhmNauxUZnWwICRzvWC379KstaQBY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        //--------------------------All products
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //------------------------------------------
        //getData();
        getapiData();
        //------------------------------------------
        Log.d(String.valueOf(DataActivity.this),"Tokeeen: "+db.afficherTousUser().get(0).getToken());
    }

    public void getData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(DataActivity.this),"responsee: "+response.toString());
                try {
                    ArrayList<Information> ListSncf = new ArrayList<Information>();
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i=0;i<jsonarray.length();i++){
                        JSONObject jsonObject = jsonarray.getJSONObject(i);
                        Information in = new Information();
                        in.setDate(jsonObject.getString("date"));
                        in.setHome(jsonObject.getString("home"));
                        ListSncf.add(in);
                    }
                    adapter = new recyclerAdapter(ListSncf, DataActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.d(String.valueOf(DataActivity.this),"jsonArray: "+jsonarray.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(String.valueOf(DataActivity.this),"error: "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("idlogin", db.afficherTousUser().get(0).getIdmac().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getapiData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_api_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(DataActivity.this),"responsee: "+response.toString());
                try {
                    ArrayList<Information> ListSncf = new ArrayList<Information>();
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray ja_data = jsonObj.getJSONArray("feeds");
                    for(int i=0;i<ja_data.length();i++){
                        JSONObject jsonObject = ja_data.getJSONObject(i);
                        Information in = new Information();
                        in.setDate(jsonObject.getString("created_at"));
                        in.setHome(jsonObject.getString("field1"));
                        ListSncf.add(in);
                    }

                    adapter = new recyclerAdapter(ListSncf, DataActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(String.valueOf(DataActivity.this),"error: "+error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
