package com.example.shehabsalah.mobile_app_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.util.List;

/**
 * Created by Shehab on 1/11/2016.
 */
public class UpdateProfilePic {
    public Boolean updatePP(int user_id, Bitmap Image,Context context){
        String ImageData = encodeTobase64(Image);
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("image", ImageData));
        params.add(new BasicNameValuePair("CustomerID", user_id+""));
        if(uploadImageToserver(params)){
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
        String url = "http://www.platformhouse.com/edugram/update_profile_pic.php";
        HttpEntity httpEntity = null;

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            String entityResponse = EntityUtils.toString(httpEntity);
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
