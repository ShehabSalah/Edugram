package com.example.shehabsalah.mobile_app_project;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SettingsActivity extends Activity {
    EditText edit_name;
    EditText edit_email;
    Spinner spinner;
    Spinner spinner2;
    ImageView close_image;
    ImageView save_image;
    Button logout_button;
    String name;
    String Email;
    int gender;
    int country;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        close_image = (ImageView)findViewById(R.id.close_image);
        save_image = (ImageView)findViewById(R.id.save_image);
        logout_button = (Button)findViewById(R.id.logout_button);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        close_image.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logout_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        String[] gen = new String[]{"Gender", "Male", "Female"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gen);
        spinner.setAdapter(adapter3);
        String[] cnt = new String[]{"Country", "Egypt","KSA" , "Kuwait", "Jordan"
                ,"UAE","Bahrain","Tunisia","Algeria","Comoros","Djibouti","Sudan","Syria","Somalia"
                ,"Iraq","Oman","Palestine","Qatar","Lebanon","Libya","Morocco","Mauritania","Yemen"
                , "USA", "England", "Germany", "France", "Spain"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cnt);
        spinner2.setAdapter(adapter1);
        String[] fac = new String[]{"Faculty", "Computer Sciences", "Engineering", "Dentistry"
                , "Pharmacy", "Alsun", "Mass Communication", "Business"};
        edit_name.setText(" ");
        edit_email.setText(" ");
        save_image.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                name = edit_name.getText().toString();
                Email = edit_email.getText().toString();
                gender = spinner.getSelectedItemPosition();
                country = spinner2.getSelectedItemPosition();
                if(name.length() < 4){
                    Toast.makeText(getApplicationContext(), "Please Enter a valid name!.", Toast.LENGTH_LONG).show();
                }else if(!Email.contains("@") && !Email.contains(".com")){
                    Toast.makeText(getApplicationContext(), "Please Enter a valid Email!.", Toast.LENGTH_LONG).show();
                }else if(gender == 0){
                    Toast.makeText(getApplicationContext(), "Please select your gender!.", Toast.LENGTH_LONG).show();
                }else if(country == 0){
                    Toast.makeText(getApplicationContext(), "Please select your country!.", Toast.LENGTH_LONG).show();
                }else{
                    ConnectivityManager connMgr = (ConnectivityManager)
                            SettingsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new UpdateMyInfo().execute(new ApiConnector());
                    } else {
                        Toast.makeText(getApplicationContext(), "Network Not Availible!.\nPlease Connect to Internet and try again", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                SettingsActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetMyInfo().execute(new ApiConnector());
        } else {
            Toast.makeText(getApplicationContext(), "Network Not Availible!.\nPlease Connect to Internet and try again", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void logOut(){
        File root = new File(Environment.getExternalStorageDirectory(), "EG");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "Config.txt");
        File gpxfile2 = new File(root, "Config2.txt");
        gpxfile.delete();
        gpxfile2.delete();
        finish();
    }
    private class GetMyInfo extends AsyncTask<ApiConnector,Long,JSONArray>{
        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Loading...");
            showDialog();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray == null){
                finish();
            }else{
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    edit_name.setText(jsonObject.getString("name"));
                    edit_email.setText(jsonObject.getString("email"));
                    String gender = jsonObject.getString("user_gender");
                    String Country = jsonObject.getString("user_country");
                    spinner.setSelection(Integer.parseInt(gender));
                    spinner2.setSelection(Integer.parseInt(Country));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideDialog();
            }
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getMyInfo(FullscreenActivity.user_id);
        }
    }
    private class UpdateMyInfo extends AsyncTask<ApiConnector,Long,Boolean>{
        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Updating...");
            showDialog();
        }

        @Override
        protected void onPostExecute(Boolean booleans) {
            if(booleans){
                Toast.makeText(getApplicationContext(), "Profile Updated Successuflly!", Toast.LENGTH_LONG).show();
                hideDialog();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Unable To Update Your Profile!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].updateProfileInfo(FullscreenActivity.user_id,name,Email,gender,country,getApplicationContext());
        }
    }
}
