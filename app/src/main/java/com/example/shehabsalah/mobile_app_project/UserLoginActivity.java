package com.example.shehabsalah.mobile_app_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class UserLoginActivity extends Activity {

    private EditText inputEmail;
    private EditText inputPassword;
    Button btnLogin;
    Button btnLinkToRegister;
    String email;
    String password;
    public static int user_id;
    public static int faculty_id;
    ProgressBar loginprogressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        inputEmail = (EditText) findViewById(R.id.mail);
        inputPassword = (EditText) findViewById(R.id.password);
        user_id = 0;
        faculty_id = 0;
        loginprogressBar2 = (ProgressBar)findViewById(R.id.loginprogressBar2);
        loginprogressBar2.setVisibility(View.INVISIBLE);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        RelativeLayout relatlayout = (RelativeLayout)findViewById(R.id.relatlayout);
        relatlayout.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        // Check if user is already logged in or not
        // Login button Click Event
        new CheckIfUserExists().execute(1);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();


                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    ConnectivityManager connMgr = (ConnectivityManager)
                            UserLoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new Login().execute(new LoginConnector());
                    } else {
                        Toast.makeText(getApplicationContext(), "Network Not Availible!.\nPlease Connect to Internet and try again", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(UserLoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("no", null).show();
    }

    private class Login extends AsyncTask<LoginConnector,Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            inputEmail.setVisibility(View.INVISIBLE);
            inputPassword.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            btnLinkToRegister.setVisibility(View.INVISIBLE);
            loginprogressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(loginprogressBar2 != null)
                loginprogressBar2.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            inputEmail.setVisibility(View.VISIBLE);
            inputPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLinkToRegister.setVisibility(View.VISIBLE);
            loginprogressBar2.setVisibility(View.INVISIBLE);
            if((UserLoginActivity.user_id > 0 && UserLoginActivity.faculty_id > 0) || LoginConnector.success){
                Log.i("idAndFac","in2");
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
                Intent i = new Intent(UserLoginActivity.this,FullscreenActivity.class);
                startActivity(i);
                finish();
            }else if(user_id < 0 && faculty_id < 0){
                Toast.makeText(getApplicationContext(),"You are missing Email or Password!" , Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"ERROR: Invalid Email or Password!" , Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(LoginConnector... params) {
            params[0].checkLogin(email, password,getApplicationContext());
            return LoginConnector.success;
        }
    }
    private class CheckIfUserExists extends AsyncTask<Integer,Long,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Intent i = new Intent(UserLoginActivity.this,FullscreenActivity.class);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            return readFromFile2() && readFromFile();
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
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
        private Boolean readFromFile() {
            Boolean access = false;
            boolean cont = true;
            File root = new File(Environment.getExternalStorageDirectory(), "EG");
            if (!root.exists()) {
                root.mkdirs();
                cont = false;
            }
            if(cont){
                File file = new File(root,"Config.txt");
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                    }
                    br.close();
                    if(text.length() > 0) {
                        UserLoginActivity.user_id = Integer.parseInt(text.toString());
                        access = true;
                    }
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            }
            return access;
        }
    private Boolean readFromFile2() {
        Boolean access = false;
        boolean cont = true;
        File root = new File(Environment.getExternalStorageDirectory(), "EG");
        if (!root.exists()) {
            root.mkdirs();
            cont = false;
        }
        if(cont){
            File file = new File(root,"Config2.txt");
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
                if(text.length() > 0) {
                    UserLoginActivity.faculty_id = Integer.parseInt(text.toString());
                    access = true;
                }
            }
            catch (IOException e) {
                //You'll need to add proper error handling here
            }
        }
        return access;
    }
}