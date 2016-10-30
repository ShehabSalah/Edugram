package com.example.shehabsalah.mobile_app_project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by Shehab on 1/9/2016.
 */
public class LoginConnector {
    public static boolean success;
    public void checkLogin(final String email, final String password, final Context context) {
        success = false;
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://www.platformhouse.com/edugram/login.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(!response.contains("null") && !response.equals("You are missing Email or Password")){
                    String[] separated = response.split(" ");
                    if(separated.length > 0){
                        UserLoginActivity.user_id = Integer.parseInt(separated[0]);
                        UserLoginActivity.faculty_id = Integer.parseInt(separated[1]);
                        Log.i("idAndFac","in1");
                    }
                    // Inserting row in users table
                    // Launch main activity
                    Log.i("idAndFac","id:"+separated[0]+"fac:"+separated[1]);
                    success = true;
                }else if(response.equals("You are missing Email or Password")){
                    Log.i("idAndFac", "You are missing Email or Password!");
                    UserLoginActivity.user_id = -1;
                    UserLoginActivity.faculty_id = -1;
                    success = false;
                }else if(response.equals("null")){
                    Log.i("idAndFac", "This Email is not exist!");
                    success = false;
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("idAndFac", "error");
                success = false;
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        try {
            synchronized (this) {
                wait(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
