package com.example.shehabsalah.mobile_app_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shehab on 1/9/2016.
 */
public class UploadPostToServer {
    String Post_id;
    public Boolean setNewPost(int user_id,int faculty_id, Bitmap Image,final String post_txt,Context context){
        String ImageData = encodeTobase64(Image);
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("image", ImageData));
        params.add(new BasicNameValuePair("CustomerID", user_id+""));
        params.add(new BasicNameValuePair("FacultyID", faculty_id+""));
        if(uploadImageToserver(params)){
            StringRequest request = new StringRequest(Request.Method.POST, "http://www.platformhouse.com/edugram/set_new_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Error Response", response);
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
                    parameters.put("txt", post_txt);
                    parameters.put("post_id", Post_id);
                    return parameters;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);
            try {
                synchronized (this) {
                    wait(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
    public static String encodeTobase64(Bitmap image) {
        System.gc();
        if (image == null)return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8;
    }

    public Boolean uploadImageToserver(List<NameValuePair> params) {

        // URL for getting all customers
        String url = "http://www.platformhouse.com/edugram/set_new_post.php";
        HttpEntity httpEntity = null;

        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);
            Post_id = entityResponse;
            Log.i("Entity Response  : ", entityResponse);
            return true;

        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Her


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
