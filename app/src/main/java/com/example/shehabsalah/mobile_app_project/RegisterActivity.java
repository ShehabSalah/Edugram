package com.example.shehabsalah.mobile_app_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterActivity extends Activity {
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner country, faculty, gender;
    private ProgressDialog pDialog;
    String name;
    String email;
    String password;
    String facultyy;
    String genderr;
    String countryy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        country = (Spinner) findViewById(R.id.cntry);
        String[] cnt = new String[]{"Country", "Egypt","KSA" , "Kuwait", "Jordan"
                ,"UAE","Bahrain","Tunisia","Algeria","Comoros","Djibouti","Sudan","Syria","Somalia"
                ,"Iraq","Oman","Palestine","Qatar","Lebanon","Libya","Morocco","Mauritania","Yemen"
                , "USA", "England", "Germany", "France", "Spain"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cnt);
        country.setAdapter(adapter1);

        faculty = (Spinner) findViewById(R.id.faculty);
        String[] fac = new String[]{"Faculty", "Computer Sciences", "Engineering", "Dentistry"
                , "Pharmacy", "Alsun", "Mass Communication", "Business"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fac);
        faculty.setAdapter(adapter2);

        gender = (Spinner) findViewById(R.id.gender);
        String[] gen = new String[]{"Gender", "Male", "Female"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gen);
        gender.setAdapter(adapter3);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name = inputFullName.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                facultyy = faculty.getSelectedItem().toString().trim();
                genderr = gender.getSelectedItem().toString().trim();
                countryy = country.getSelectedItem().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(email.contains("@") && email.contains(".com")) {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                RegisterActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            new Register().execute(new RegisterConnector());//registerUser(name, email, password,facultyy,genderr,countryy);
                        } else {
                            Toast.makeText(getApplicationContext(), "Network Not Availible!.\nPlease Connect to Internet and try again", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Please enter a valid Email!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        UserLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private class Register extends AsyncTask<RegisterConnector,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Registering ...");
            showDialog();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if((UserLoginActivity.user_id > 0 && UserLoginActivity.faculty_id > 0) || RegisterConnector.success){
                Log.i("idAndFac", "in2");
                while(UserLoginActivity.user_id == 0 && UserLoginActivity.faculty_id == 0){
                    try {
                        synchronized (this) {
                            wait(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                writeToFile1(String.valueOf(UserLoginActivity.user_id));
                writeToFile2(String.valueOf(UserLoginActivity.faculty_id));
                Toast.makeText(getApplicationContext(), "User successfully registered!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this,FullscreenActivity.class);
                hideDialog();
                startActivity(i);
                finish();
            }else if(UserLoginActivity.user_id < 0 && UserLoginActivity.faculty_id < 0){
                Toast.makeText(getApplicationContext(),"Registration Error!" , Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"User already existed!" , Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(RegisterConnector... params) {
            params[0].userRegister(name, email, password, facultyy, genderr, countryy,getApplicationContext());
            return RegisterConnector.success;
        }
    }
    private void writeToFile1(String user_id) {
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "EG");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "Config.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(user_id);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void writeToFile2(String Faculty_id) {
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "EG");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "Config2.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(Faculty_id);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
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
}
