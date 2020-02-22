package com.example.arduinomobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DB_local db = new DB_local(LoginActivity.this, null, 1);
    EditText txt_username,txt_password;
    String frb_token="";
    Button btn_login;
    String url_data ="http://issamdata.alwaysdata.net/log.php";
    String url_tokenUpdate ="http://issamdata.alwaysdata.net/loadToken.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //-----------------------------
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        //-----------------------------
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        frb_token = task.getResult().getToken();
                    }
                });
        //-----------------------------
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFunction();
            }
        });
    }

    public void loginFunction (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this,"response: "+response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jo = new JSONObject(response);
                    if(jo.getString("code").equals("300")){
                        if(db.afficherTousUser().size()== 0) {
                            loadtoken();
                            db.insererUser(jo.getString("idlogin").toString(),txt_username.getText().toString(), txt_password.getText().toString(), frb_token);
                        }
                        txt_username.setText("");
                        txt_password.setText("");
                        Intent intent = new Intent(LoginActivity.this,DataActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (jo.getString("code").equals("301")){
                        txt_username.setText("");
                        txt_password.setText("");
                        Toast.makeText(LoginActivity.this,"Erreur, nom du compte ou mot de passe incorrect !",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(String.valueOf(LoginActivity.this),"error: "+error.getMessage());
                Toast.makeText(LoginActivity.this,"Erreur: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("username", txt_username.getText().toString());
                params.put("password", txt_password.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void loadtoken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url_tokenUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject (response);
                    if(jsonObj.getString("code").equals("300")) {
                        Log.d(String.valueOf(LoginActivity.this),"responseToken: "+response.toString());
                    }
                    else if(jsonObj.getString("codeEr").equals("301")) {
                        Log.d(String.valueOf(LoginActivity.this),"erreurToken: "+response.toString());
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
                params.put("username", txt_username.getText().toString());
                params.put("token", frb_token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
