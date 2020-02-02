package com.example.arduinomobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class MainActivity extends AppCompatActivity {
    DB_local db = new DB_local(MainActivity.this, null, 1);
    String url_verify ="http://issamdata.alwaysdata.net/verify.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----------------------------
        PrimeRun p = new PrimeRun(5000);
        new Thread(p).start();
    }

    //-------------------- Thread verify if account exists
    class PrimeRun implements Runnable {
        long minPrime;
        PrimeRun(long minPrime) {
            this.minPrime = minPrime;
        }
        public void run() {
            try {
                Thread.sleep(minPrime);
                if(db.afficherTousUser().size() > 0) {
                    verify();
                }else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //---------------VerifyClient
    private void verify() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url_verify, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSONObject  jsonArray = new JSONArray(response);
                    JSONObject jsonObj = new JSONObject (response);
                    Log.d(String.valueOf(MainActivity.this),"responseMain: "+response.toString());
                    if(jsonObj.getString("code").equals("300")) {
                        Intent intent = new Intent(MainActivity.this, DataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(jsonObj.getString("codeEr").equals("301")) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", db.afficherTousUser().get(0).getUsername());
                return params;
            }
        }
                ;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
