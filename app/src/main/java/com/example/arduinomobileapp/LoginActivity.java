package com.example.arduinomobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DB_local db = new DB_local(LoginActivity.this, null, 1);
    EditText txt_username,txt_password;
    Button btn_login;
    String url_data ="http://issamdata.alwaysdata.net/log.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //-----------------------------
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
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
                            db.insererUser(txt_username.getText().toString(), txt_password.getText().toString(), "token");
                        }
                        txt_username.setText("");
                        txt_password.setText("");
                        Intent intent = new Intent(LoginActivity.this,DataActivity.class);
                        startActivity(intent);
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
}
