package com.example.shehabsalah.mobile_app_project;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shehab Salah on 12/5/2015.
 */
public class ApiConnector {
    boolean updateProfileSuccess;
    public JSONArray[] getPost(int post_id, int user_id) {
        JSONArray[] onePost = new JSONArray[2];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getonepost.php?post_id=" + post_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            onePost[0] = jsonObject.getJSONArray("post");
            Log.i("jsonString", jsontxt);
            if (onePost[0] == null)
                Log.i("post0", "post is null");
            connection.disconnect();

            /*Start Edit new way to create like posts*/
            url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getposts.php?user=" + user_id + "&post_id=" + post_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);
            jsontxt = buffer.toString();
            jsonObject = new JSONObject(jsontxt);
            onePost[1] = jsonObject.getJSONArray("likes");
            connection.disconnect();
            /*End Edit new way to create like posts*/
            Log.i("jsonString", jsontxt);
            return onePost;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }
    public Boolean setComment(final int post_id, final String Comment_txt, Context context, final int user_id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/jsonfile_set_comment.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("post_id", post_id + "");
                parameters.put("comment", Comment_txt);
                parameters.put("user_id", user_id + "");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        try {
            synchronized (this) {
                wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public JSONArray getAllComments(int post_id) {
        JSONArray oneComment = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://www.platformhouse.com/edugram/getjson_file_comments.php?post_id=" + post_id + "&user_id=" + FullscreenActivity.user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);
            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            oneComment = jsonObject.getJSONArray("comments");
            Log.i("jsonString", jsontxt);
            if (oneComment == null)
                Log.i("post0", "post is null");
            connection.disconnect();
            return oneComment;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public JSONArray[] getAllPost(int Faculty, int user_id, int limit) {
        JSONArray[] posts = new JSONArray[2];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getposts.php?faculty_id=" + Faculty + "&limit=" + limit + "&user_id=" + FullscreenActivity.user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            posts[0] = jsonObject.getJSONArray("posts");
            if (posts[0] == null)
                Log.i("post0", "post is null");
            connection.disconnect();

            /*Start Edit new way to create like posts*/
            url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getposts.php?user_id=" + user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            jsontxt = buffer.toString();
            jsonObject = new JSONObject(jsontxt);
            posts[1] = jsonObject.getJSONArray("likes");
            connection.disconnect();

            /*End Edit new way to create like posts*/
            Log.i("jsonString", jsontxt);
            return posts;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public JSONArray[] getMyPost(int Faculty, int user_id, int limit) {
        JSONArray[] posts = new JSONArray[2];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getmyposts.php?faculty_id=" + Faculty + "&limit=" + limit + "&user_id=" + FullscreenActivity.user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            posts[0] = jsonObject.getJSONArray("posts");
            if (posts[0] == null)
                Log.i("post0", "post is null");
            connection.disconnect();

            /*Start Edit new way to create like posts*/
            url = new URL("http://www.platformhouse.com/edugram/jsonfile_edugram_getmyposts.php?user_id=" + user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            jsontxt = buffer.toString();
            jsonObject = new JSONObject(jsontxt);
            posts[1] = jsonObject.getJSONArray("likes");
            connection.disconnect();

            /*End Edit new way to create like posts*/
            Log.i("jsonString", jsontxt);
            return posts;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Boolean deleteComment(final int comment_id, Context context, final int user_id, final int post_id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/jsonfile_delete_comment.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("comment", comment_id + "");
                parameters.put("post_id", post_id + "");
                parameters.put("user_id", user_id + "");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public Boolean deletePost(Context context, final int user_id, final int post_id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/jsonfile_delete_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("post_id", post_id + "");
                parameters.put("user_id", user_id + "");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        try {
            synchronized (this) {
                wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public Boolean checkNotifications(final int user_id) {
        String jsontxt = "";
        try {
            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_getnotifinum.php?user_id=" + user_id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            jsontxt = buffer.toString();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.i("notifications", jsontxt);
        return jsontxt.equals("true");
    }
    public JSONArray getAllNotifications(final int user_id, int limit) {
        JSONArray oneNotification = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_getall_notifiactions.php?user_id=" + user_id + "&limit=" + limit);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);
            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            oneNotification = jsonObject.getJSONArray("notifications");
            Log.i("jsonString", jsontxt);
            connection.disconnect();
            return oneNotification;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Boolean updateNotifications(final int user_id, Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/jsonfile_updatenotifications.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user_id", user_id + "");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        return true;
    }
    public Boolean updateNotificationSeen(final int user_id, Context context, final int post_id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/jsonfile_notification_seen.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user_id", user_id + "");
                Log.i("postseenid",""+post_id);
                parameters.put("post_id", post_id + "");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        return true;
    }
    public JSONArray getMyInfo(int user_id){
        JSONArray myInfo = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_myinfo.php?user_id=" + user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);
            String jsontxt = buffer.toString();
            JSONObject jsonObject = new JSONObject(jsontxt);
            myInfo = jsonObject.getJSONArray("user");
            Log.i("jsonString", jsontxt);
            connection.disconnect();
            return myInfo;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Boolean updateProfileInfo(final int id, final String name, final String email, final int gender,
                                     final int country, Context context){
        updateProfileSuccess = false;
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/update_myinfo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("success"));
                        updateProfileSuccess = true;
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error Response", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("user_id", id + "");
                parameters.put("name", name);
                parameters.put("email", email);
                parameters.put("gender", gender+"");
                parameters.put("country", country+"");
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        /*TO BE CONTINUE*/
        //................................................
        //................................................
        //................................................
        try {
            synchronized (this) {
                wait(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return updateProfileSuccess;
    }
    public String getMyPic(final int user_id, final Context context){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL("http://www.platformhouse.com/edugram/jsonfile_myprofilepic.php?user_id=" + user_id);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            String jsontxt = buffer.toString();
            connection.disconnect();
            return jsontxt;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}