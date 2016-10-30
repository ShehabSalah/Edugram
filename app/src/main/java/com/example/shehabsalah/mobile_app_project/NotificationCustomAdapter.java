package com.example.shehabsalah.mobile_app_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shehab on 12/24/2015.
 */
public class NotificationCustomAdapter extends BaseAdapter {
    private JSONArray notificationArray;
    private Context activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private static final String imgDir = "http://www.platformhouse.com/edugram/";
    ListCell cell;
    public NotificationCustomAdapter(Context context, JSONArray jsonArray){
        /*super(context, R.layout.post);*/
        this.notificationArray = jsonArray;
        this.activity = context;
        imageLoader = new ImageLoader(activity);
    }
    public void setNewNotificationCustomAdapter(Context context, JSONArray jsonArray){
        this.notificationArray = jsonArray;
        this.activity = context;
        imageLoader = new ImageLoader(activity);
    }
    @Override
    public int getCount() {
        if(notificationArray != null)
            return notificationArray.length() > 0?notificationArray.length():0;
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject jsonObject = null;
        View row = convertView;

        if (row == null){
            inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.notification_list,parent,false);
            cell = new ListCell();
            cell.username = (TextView) row.findViewById(R.id.Notification_username);
            cell.notification_txt = (TextView) row.findViewById(R.id.Notification_area);
            cell.profile_pic = (ImageView) row.findViewById(R.id.Notification_userpic);
            cell.seen = (ImageView) row.findViewById(R.id.seen_done);
            row.setTag(cell);
        }else{
            cell = (ListCell) row.getTag();
        }
        try{
            jsonObject = this.notificationArray.getJSONObject(position);
            cell.username.setText(jsonObject.getString("name"));
            cell.notification_txt.setText(jsonObject.getString("notification_txt"));
            ImageView profile = cell.profile_pic;
            imageLoader.DisplayImage(imgDir + jsonObject.getString("profile_img"),profile);
            ImageView seen = cell.seen;
            imageLoader.DisplayImage(imgDir+jsonObject.getString("seen"),seen);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return row;
    }

    private class ListCell{
        private TextView username;
        private TextView notification_txt;
        private ImageView profile_pic;
        private ImageView seen;
    }
}
