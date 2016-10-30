package com.example.shehabsalah.mobile_app_project;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class newPostActivity extends Activity {
    ImageButton internet;
    ImageView notification;
    ImageView notification_active;
    ImageView profileButton;
    ImageView cameraButton;
    ImageView homeButton;
    EditText new_post_write;
    ImageView new_post_image;
    Button new_post_button;
    Bitmap bitmap;
    String Post_txt;
    ProgressBar progressBar;
    public static boolean profilePicUdated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        internet = (ImageButton)findViewById(R.id.Internet);
        internet.setImageResource(R.mipmap.no_internet);
        internet.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setProgress(0);
        notification = (ImageView)findViewById(R.id.notification);
        notification.setVisibility(View.INVISIBLE);
        notification_active = (ImageView)findViewById(R.id.notification_active);
        cameraButton = (ImageView)findViewById(R.id.cameraButton);
        cameraButton.setImageResource(R.mipmap.camera3);
        Button RotateNegative = (Button)findViewById(R.id.RotateNegative);
        Button RotatePositive = (Button)findViewById(R.id.RotatePositive);
        profileButton = (ImageView)findViewById(R.id.profileButton);
        homeButton = (ImageView)findViewById(R.id.homeButton);
        homeButton.setImageResource(R.mipmap.home);
        homeButton.setBackgroundColor(Color.BLACK);
        new_post_write = (EditText)findViewById(R.id.new_post_write);
        new_post_image = (ImageView)findViewById(R.id.new_post_image);
        new_post_button = (Button)findViewById(R.id.new_post_button);
        if(FullscreenActivity.new_image_post != null){
            bitmap = FullscreenActivity.new_image_post;
            new_post_image.setImageBitmap(bitmap);
            FullscreenActivity.new_image_post = null;
        }else if(ProfileActivity.new_image_post != null){
            bitmap = ProfileActivity.new_image_post;
            new_post_image.setImageBitmap(bitmap);
            ProfileActivity.new_image_post = null;
        }else if(Notifications.new_image_post != null){
            bitmap = Notifications.new_image_post;
            new_post_image.setImageBitmap(bitmap);
            Notifications.new_image_post = null;
        }else if(FullscreenActivity.new_image_post == null || Notifications.new_image_post == null ||
                ProfileActivity.new_image_post != null){
            String path = "sdcard/EG/cam_image.jpg";
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = ((BitmapDrawable)Drawable.createFromPath(path)).getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            new_post_image.setImageBitmap(bitmap);

        }
        if(bitmap.getWidth() > 1000 && bitmap.getHeight() > 1000){
            if(((bitmap.getWidth()/2) - 300) < 680)
                bitmap = Bitmap.createScaledBitmap(bitmap,680, bitmap.getHeight(),true);
            if((bitmap.getHeight()/2) - 308 < 680)
                bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(), 680,true);
            if(((bitmap.getWidth()/2) - 300) > 680)
                bitmap = Bitmap.createScaledBitmap(bitmap,((bitmap.getWidth()/2) - 300), bitmap.getHeight(),true);
            if((bitmap.getHeight()/2) - 308 > 680)
                bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(), (bitmap.getHeight()/2) - 308,true);
        }

        //***********************************************************************************//
        //if user want to change the profile pic
        if(ProfileActivity.changeProfilePicRequset){
            new_post_write.setVisibility(View.INVISIBLE);
            new_post_button.setText("Upload my new profile picture");
        }
        //***********************************************************************************//



        Log.i("image_size","height: "+bitmap.getHeight()+" width: "+bitmap.getWidth());
        RotatePositive.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                new_post_image.setImageBitmap(bitmap);
            }
        });
        RotateNegative.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                new_post_image.setImageBitmap(bitmap);
            }
        });
        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeButton.setImageResource(R.mipmap.home2);
                        int Delay = 200;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                homeButton.setImageResource(R.mipmap.home);
                            }
                        }, Delay);
                        Intent showDetails = new Intent(newPostActivity.this.getApplicationContext(), FullscreenActivity.class);
                        newPostActivity.this.startActivity(showDetails);
                    }
                }
        );
        profileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileButton.setImageResource(R.mipmap.profile2);
                        profileButton.setBackgroundColor(Color.BLACK);
                        int Delay = 200;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                profileButton.setImageResource(R.mipmap.profile);
                                profileButton.setBackgroundColor(242424);
                            }
                        }, Delay);
                        Intent showDetails = new Intent(newPostActivity.this.getApplicationContext(), ProfileActivity.class);
                        newPostActivity.this.startActivity(showDetails);
                    }
                }
        );
        notification_active.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notification_active.setImageResource(R.mipmap.notification_active_onclick);
                        int Delay = 200;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notification_active.setImageResource(R.mipmap.notification_active);
                            }
                        }, Delay);
                        Intent showDetails = new Intent(newPostActivity.this.getApplicationContext(), Notifications.class);
                        newPostActivity.this.startActivity(showDetails);
                    }
                }
        );
        notification.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notification.setImageResource(R.mipmap.notification_onclick);
                        int Delay = 200;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notification.setImageResource(R.mipmap.notification);
                            }
                        }, Delay);
                        Intent showDetails = new Intent(newPostActivity.this.getApplicationContext(), Notifications.class);
                        newPostActivity.this.startActivity(showDetails);
                    }
                }
        );
        internet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                newPostActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                        } else {
                            internet.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
        new_post_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ProfileActivity.changeProfilePicRequset){
                    new UpdateProfilePicture().execute(new UpdateProfilePic());
                }else{
                    Post_txt = new_post_write.getText().toString();
                    if(Post_txt.length() == 0)
                        Post_txt = " ";
                    Log.i("newposttxt",Post_txt);
                    new SetNewPost().execute(new UploadPostToServer());
                }
            }
        });
        String ImageX = "sdcard/DCIM/Camera/*";
    }

    @Override
    protected void onResume() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            internet.setVisibility(View.INVISIBLE);
            new CheckNotifications().execute(new ApiConnector());
        }else{
            internet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    private class UpdateProfilePicture extends AsyncTask<UpdateProfilePic,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            new_post_button.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(progressBar != null)
                progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Toast.makeText(newPostActivity.this, "Success!", Toast.LENGTH_LONG).show();
                ProfileActivity.changeProfilePicRequset = false;
                profilePicUdated = true;
                /*pDialog.dismiss();*/
                finish();
            }else{
                Toast.makeText(newPostActivity.this, "Unable to upload your picture!", Toast.LENGTH_LONG).show();
               /* pDialog.dismiss();*/
            }
            new_post_button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Boolean doInBackground(UpdateProfilePic... params) {
            return params[0].updatePP(FullscreenActivity.user_id, bitmap,getApplicationContext());
        }
    }
    private class SetNewPost extends AsyncTask<UploadPostToServer,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            /*pDialog = new ProgressDialog(newPostActivity.this);
            pDialog.setMessage("Uploading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
            progressBar.setVisibility(View.VISIBLE);
            new_post_button.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(progressBar != null)
                progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Toast.makeText(newPostActivity.this, "Success!", Toast.LENGTH_LONG).show();
                /*pDialog.dismiss();*/
                finish();
            }else{
                Toast.makeText(newPostActivity.this, "Unable to upload your post!", Toast.LENGTH_LONG).show();
               /* pDialog.dismiss();*/
            }
            new_post_button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Boolean doInBackground(UploadPostToServer... params) {
            return params[0].setNewPost(FullscreenActivity.user_id, FullscreenActivity.faculty_id,bitmap,Post_txt,getApplicationContext());
        }
    }
    private class CheckNotifications extends AsyncTask<ApiConnector,Long,Boolean> {
        @Override
        protected void onPreExecute() {
            notification_active.setVisibility(View.INVISIBLE);
            notification.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
                notification_active.setVisibility(View.VISIBLE);
            else
                notification.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].checkNotifications(FullscreenActivity.user_id);
        }
    }

}
