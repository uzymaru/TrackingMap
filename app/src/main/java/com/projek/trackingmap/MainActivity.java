package com.projek.trackingmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projek.trackingmap.list.ExpansionPanelSampleActivityViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rellay1, rellay2;
    private EditText usernameedt, passwordedt;
    private Button login;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private static String URL_Login = "http://192.168.100.4/magang/aset-diskominfo/api/read.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        usernameedt = findViewById(R.id.usr);
        passwordedt = findViewById(R.id.pwd);
        login = findViewById(R.id.lgn);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rellay1.setVisibility(View.VISIBLE);
                rellay2.setVisibility(View.VISIBLE);
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usernameedt.getText().toString().trim();
                String pass = passwordedt.getText().toString().trim();

                if (!user.isEmpty() || !pass.isEmpty()){
                    Login(user, pass);
                } else {
                    usernameedt.setError("Silahkan masukkan username!");
                    passwordedt.setError("Silahkan masukkan Pawword!");
                }
            }
        });
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }

    private void Login(String usernameedt, String passwordedt) {
        stringRequest = new StringRequest(Request.Method.POST, URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")){
                                for (int i = 0; i< jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String usernamedt = object.getString("username").trim();

                                    Intent intent = new Intent(MainActivity.this, ExpansionPanelSampleActivityViewGroup.class);
                                    intent.putExtra("username", usernamedt);
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error???"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameedt);
                params.put("password", passwordedt);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
