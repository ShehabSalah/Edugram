package com.example.shehabsalah.mobile_app_project;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaActionSound;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private int post_id;
    public ImageLoader imageLoader;
    public PostImageLoader postImageLoader;
    CommentCustomAdapter adapter = null;
    ImageView profile_pic;
    TextView username;
    TextView faculty;
    TextView post_txt;
    ImageView post_pic;
    ImageView Like;
    TextView likeNumber_main;
    TextView comments_main;
    ImageButton internet_main;
    ImageView notification;
    ImageView notification_active;
    ImageView setOnLikeUnlikeClick;
    int number_of_likes;
    boolean like_active = false;
    Handler hand = new Handler() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            ViewGroup view = (ViewGroup)findViewById(R.id.post_layout);
            TransitionManager.beginDelayedTransition(view);
            setOnLikeUnlikeClick.setVisibility(View.VISIBLE);
        }
    };
    Handler handl2 = new Handler() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            ViewGroup view = (ViewGroup)findViewById(R.id.post_layout);
            TransitionManager.beginDelayedTransition(view);
            setOnLikeUnlikeClick.setVisibility(View.INVISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign Variables to the Layout
        profile_pic = (ImageView)findViewById(R.id.profile_picture_main);
        username = (TextView)findViewById(R.id.user_name_main);
        faculty = (TextView)findViewById(R.id.faculty_main);
        post_txt = (TextView)findViewById(R.id.postTxt_main);
        post_pic = (ImageView)findViewById(R.id.post_picture_main);
        Like = (ImageView)findViewById(R.id.like_main);
        likeNumber_main = (TextView)findViewById(R.id.likeNumber_main);
        comments_main = (TextView)findViewById(R.id.comments_main);
        internet_main = (ImageButton)findViewById(R.id.internet_main);
        post_pic.setImageResource(R.mipmap.load_image);
        setOnLikeUnlikeClick = (ImageView) findViewById(R.id.setOnLikeUnlikeClick);
        setOnLikeUnlikeClick.setVisibility(View.INVISIBLE);
        //assign image to ImageButton
        internet_main.setImageResource(R.mipmap.no_internet);
        internet_main.setVisibility(View.INVISIBLE);
        notification_active = (ImageView)findViewById(R.id.notification_active);
        notification = (ImageView) findViewById(R.id.notification);
        notification.setVisibility(View.INVISIBLE);
        //if there is id passed then continue, if not return to the previews activity
        this.post_id = getIntent().getIntExtra("post_id",-1);
        if(post_id > 0){
            //check internet connection
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                internet_main.setVisibility(View.INVISIBLE);
                new GetAllPosts().execute(new ApiConnector());
            }else{
                internet_main.setVisibility(View.VISIBLE);
                username.setVisibility(View.INVISIBLE);
                faculty.setVisibility(View.INVISIBLE);
                post_txt.setVisibility(View.INVISIBLE);
                post_pic.setVisibility(View.INVISIBLE);
                Like.setVisibility(View.INVISIBLE);
                likeNumber_main.setVisibility(View.INVISIBLE);
                comments_main.setVisibility(View.INVISIBLE);
                profile_pic.setVisibility(View.INVISIBLE);
            }
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
                            Intent showDetails = new Intent(MainActivity.this.getApplicationContext(), Notifications.class);
                            MainActivity.this.startActivity(showDetails);
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
                            Intent showDetails = new Intent(MainActivity.this.getApplicationContext(), Notifications.class);
                            MainActivity.this.startActivity(showDetails);
                        }
                    }
            );
            // If Internet Connection is not exist and the Button of refresh (VISIBLE)
            // set on click listener to see if the user try to reconnect or not
            internet_main.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager connMgr1 = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo1 = connMgr1.getActiveNetworkInfo();
                    if (networkInfo1 != null && networkInfo1.isConnected()) {
                        internet_main.setVisibility(View.INVISIBLE);
                        username.setVisibility(View.VISIBLE);
                        faculty.setVisibility(View.VISIBLE);
                        post_txt.setVisibility(View.VISIBLE);
                        post_pic.setVisibility(View.VISIBLE);
                        Like.setVisibility(View.VISIBLE);
                        likeNumber_main.setVisibility(View.VISIBLE);
                        comments_main.setVisibility(View.VISIBLE);
                        profile_pic.setVisibility(View.VISIBLE);
                        new GetAllPosts().execute(new ApiConnector());
                    } else {
                        internet_main.setVisibility(View.VISIBLE);
                        username.setVisibility(View.INVISIBLE);
                        faculty.setVisibility(View.INVISIBLE);
                        post_txt.setVisibility(View.INVISIBLE);
                        post_pic.setVisibility(View.INVISIBLE);
                        Like.setVisibility(View.INVISIBLE);
                        likeNumber_main.setVisibility(View.INVISIBLE);
                        comments_main.setVisibility(View.INVISIBLE);
                        profile_pic.setVisibility(View.INVISIBLE);
                    }
                }
            });
            comments_main.setOnClickListener(
                    new TextView.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            ConnectivityManager connMgr = (ConnectivityManager)
                                    MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                internet_main.setVisibility(View.INVISIBLE);
                                Intent showDetails = new Intent(MainActivity.this.getApplicationContext(), Comments_list.class);
                                showDetails.putExtra("post_id", post_id);
                                MainActivity.this.startActivity(showDetails);
                            } else {
                                internet_main.setVisibility(View.VISIBLE);
                            }
                        }
                    }
            );
            Like.setOnClickListener(new ImageView.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (like_active) {
                        number_of_likes--;
                        Like.setImageResource(R.mipmap.like);
                        likeNumber_main.setText(number_of_likes + " Likes");
                        like_active = false;
                        FullscreenActivity.adapter.imageLoader.clearCache();
                        new SetRemoveLikeToDB().execute("http://www.platformhouse.com/edugram/unlike_post.php?post_id=" + post_id + "&user_id=" + FullscreenActivity.user_id);
                    } else {
                        number_of_likes++;
                        Like.setImageResource(R.mipmap.like_active);
                        likeNumber_main.setText(number_of_likes + " Likes");
                        like_active = true;
                        FullscreenActivity.adapter.imageLoader.clearCache();
                        new SetRemoveLikeToDB().execute("http://www.platformhouse.com/edugram/like_post.php?post_id=" + post_id + "&user_id=" + FullscreenActivity.user_id);
                    }
                }
            });
            post_pic.setOnLongClickListener(
                    new ImageView.OnLongClickListener(){
                        @Override
                        public boolean onLongClick(View v) {
                            if(like_active){
                                number_of_likes--;
                                Like.setImageResource(R.mipmap.like);
                                likeNumber_main.setText(number_of_likes + " Likes");
                                like_active = false;
                                setOnLikeUnlikeClick.setImageResource(R.mipmap.like);
                                FullscreenActivity.adapter.imageLoader.clearCache();
                                Thread appThread = new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        long futureTime = System.currentTimeMillis() + 500;
                                        while(System.currentTimeMillis() < futureTime) {
                                            synchronized (this) {
                                                try {
                                                    wait(futureTime - System.currentTimeMillis());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        hand.sendEmptyMessage(0);
                                    }
                                });
                                appThread.start();
                                Thread appThread2 = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        long futureTime = System.currentTimeMillis() + 1000;
                                        while(System.currentTimeMillis() < futureTime) {
                                            synchronized (this) {
                                                try {
                                                    wait(futureTime - System.currentTimeMillis());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        handl2.sendEmptyMessage(0);

                                    }
                                });
                                appThread2.start();
                                new SetRemoveLikeToDB().execute("http://www.platformhouse.com/edugram/unlike_post.php?post_id=" + post_id + "&user_id=" + FullscreenActivity.user_id);
                            } else {
                                number_of_likes++;
                                Like.setImageResource(R.mipmap.like_active);
                                likeNumber_main.setText(number_of_likes + " Likes");
                                like_active = true;
                                FullscreenActivity.adapter.imageLoader.clearCache();
                                setOnLikeUnlikeClick.setImageResource(R.mipmap.like_active);
                                Thread appThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        long futureTime = System.currentTimeMillis() + 500;
                                        while(System.currentTimeMillis() < futureTime) {
                                            synchronized (this) {
                                                try {
                                                    wait(futureTime - System.currentTimeMillis());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        hand.sendEmptyMessage(0);
                                    }});
                                appThread.start();
                                Thread appThread2 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        long futureTime = System.currentTimeMillis() + 1000;
                                        while(System.currentTimeMillis() < futureTime) {
                                            synchronized (this) {
                                                try {
                                                    wait(futureTime - System.currentTimeMillis());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        handl2.sendEmptyMessage(0);
                                    }});
                                appThread2.start();
                                new SetRemoveLikeToDB().execute("http://www.platformhouse.com/edugram/like_post.php?post_id="+post_id+"&user_id="+FullscreenActivity.user_id);
                            }
                            return true;
                        }});
        }else{
            finish();
        }

    }
    @Override
    protected void onResume() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            internet_main.setVisibility(View.INVISIBLE);
            new CheckNotifications().execute(new ApiConnector());
            new GetAllPosts().execute(new ApiConnector());
        }else{
            internet_main.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
    private class CheckNotifications extends AsyncTask<ApiConnector,Long,Boolean>{
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
    private class GetAllPosts extends AsyncTask<ApiConnector,Long,JSONArray[]> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected JSONArray[] doInBackground(ApiConnector... params) {
            return params[0].getPost(post_id,FullscreenActivity.user_id);
        }
        @Override
        protected void onPostExecute(JSONArray[] jsonArrays) {
           if(pDialog != null)
               pDialog.dismiss();
            if(jsonArrays != null)
                setData(jsonArrays);
            else finish();
        }
    }
    private void setData(JSONArray[] jsonArray){
        try{
            if(jsonArray != null){
                JSONObject jsonObject = jsonArray[0].getJSONObject(0);
                Log.i("jsonString_OnePost", jsonObject.getString("post_txt"));
                post_txt.setText(jsonObject.getString("post_txt"));
                comments_main.setText(jsonObject.getString("numof_comments") + " Comments");
                likeNumber_main.setText(jsonObject.getString("numof_likes") + " Likes");
                number_of_likes = jsonObject.getInt("numof_likes");
                username.setText(jsonObject.getString("name"));
                faculty.setText(jsonObject.getString("faculty_name"));
            /*imageLoader.DisplayImage("http://www.platformhouse.com/edugram/" + jsonObject.getString("post_img"), image);*/
                imageLoader = new ImageLoader(getApplicationContext());
                postImageLoader = new PostImageLoader(getApplicationContext());
                ImageView image = this.profile_pic;
                imageLoader.DisplayImage("http://www.platformhouse.com/edugram/" + jsonObject.getString("profile_img"), image);
                image = this.post_pic;
                postImageLoader.DisplayImage("http://www.platformhouse.com/edugram/" + jsonObject.getString("post_img"), image);
                if(jsonArray[1].length() > 0){
                    jsonObject = jsonArray[1].getJSONObject(0);
                    Log.i("Like", "It seems that he like it: ");
                    image = this.Like;
                    like_active = jsonObject.getString("like_image").equals("like_active.png");
                    imageLoader.DisplayImage("http://www.platformhouse.com/edugram/"+jsonObject.getString("like_image"), image);
                }
            }else{
                Toast.makeText(MainActivity.this, "This post not exist!", Toast.LENGTH_LONG).show();
                finish();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    private class SetRemoveLikeToDB extends AsyncTask<String,Long,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader reader;
            String jsontxt = "false";
            try{
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream  = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                    buffer.append(line);

                jsontxt = buffer.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsontxt.equals("true");
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean){
                if(like_active){
                    number_of_likes--;
                    Like.setImageResource(R.mipmap.like);
                }else{
                    number_of_likes++;
                    Like.setImageResource(R.mipmap.like_active);
                }
                likeNumber_main.setText(number_of_likes + " Likes");
            }
        }
    }
}
