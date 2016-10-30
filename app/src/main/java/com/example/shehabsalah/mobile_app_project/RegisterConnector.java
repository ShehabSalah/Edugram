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
 * Created by Shehab on 1/10/2016.
 */
public class RegisterConnector {
    public static boolean success;
    public void userRegister(final String name, final String email,final String password
            ,final String faculty,final String gender,final String country,final Context context) {
        success = false;
        StringRequest request = new StringRequest(Request.Method.POST,
                "http://www.platformhouse.com/edugram/register.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(!response.contains("null") && !response.contains("ex")){
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
                }else if(response.contains("ex")){
                    UserLoginActivity.user_id = 0;
                    UserLoginActivity.faculty_id = 0;
                }else{
                    Log.i("idAndFac", "Registration Error");
                    UserLoginActivity.user_id = -1;
                    UserLoginActivity.faculty_id = -1;
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
                int countries = 0;
                //switch countries
                switch(country){
                    case "Egypt":
                        countries = 1;
                        break;
                    case "KSA":
                        countries = 2;
                        break;
                    case "Kuwait":
                        countries = 3;
                        break;
                    case "Jordan":
                        countries = 4;
                        break;
                    case "UAE":
                        countries = 5;
                        break;
                    case "Bahrain":
                        countries = 6;
                        break;
                    case "Tunisia":
                        countries = 7;
                        break;
                    case "Algeria":
                        countries = 8;
                        break;
                    case "Comoros":
                        countries = 9;
                        break;
                    case "Djibouti":
                        countries = 10;
                        break;
                    case "Sudan":
                        countries = 11;
                        break;
                    case "Syria":
                        countries = 12;
                        break;
                    case "Somalia":
                        countries = 13;
                        break;
                    case "Iraq":
                        countries = 14;
                        break;
                    case "Oman":
                        countries = 15;
                        break;
                    case "Palestine":
                        countries = 16;
                        break;
                    case "Qatar":
                        countries = 17;
                        break;
                    case "Lebanon":
                        countries = 18;
                        break;
                    case "Libya":
                        countries = 19;
                        break;
                    case "Morocco":
                        countries = 20;
                        break;
                    case "Mauritania":
                        countries = 21;
                        break;
                    case "Yemen":
                        countries = 22;
                        break;
                    case "USA":
                        countries = 23;
                        break;
                    case "England":
                        countries = 24;
                        break;
                    case "Germany":
                        countries = 25;
                        break;
                    case "France":
                        countries = 26;
                        break;
                    case "Spain":
                        countries = 27;
                        break;

                }
                //switch on faculty
                int faculty_id = 0;
                switch(faculty){
                    case "Computer Sciences":
                        faculty_id = 1;
                        break;
                    case "Engineering":
                        faculty_id = 2;
                        break;
                    case "Dentistry":
                        faculty_id = 3;
                        break;
                    case "Pharmacy":
                        faculty_id = 4;
                        break;
                    case "Alsun":
                        faculty_id = 5;
                        break;
                    case "Mass Communication":
                        faculty_id = 6;
                        break;
                    case "Business":
                        faculty_id = 7;
                        break;
                }
                //switch on Gender
                int gender_id = 0;
                switch(gender){
                    case "Male":
                        gender_id = 1;
                        break;
                    case "Female":
                        gender_id = 2;
                        break;
                }
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("faculty", faculty_id+"");
                params.put("gender", gender_id+"");
                params.put("country", countries+"");
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
