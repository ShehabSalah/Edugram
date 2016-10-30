package com.example.shehabsalah.mobile_app_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shehab Salah on 12/17/2015.
 */
public class CommentCustomAdapter extends BaseAdapter {
    private JSONArray commentArray;
    private Context activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    ListCell cell;
    private static final String imgDir = "http://www.platformhouse.com/edugram/";
    public CommentCustomAdapter(Context context, JSONArray jsonArrays) throws JSONException {
        /*super(context, R.layout.post);*/
        this.commentArray = jsonArrays;
        this.activity = context;
        imageLoader = new ImageLoader(activity);
    }
    public void setNewCustomAdapter(Context context, JSONArray jsonArrays) throws JSONException{
        this.commentArray = jsonArrays;
        this.activity = context;
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        if(commentArray != null)
            return commentArray.length() == 0?0:commentArray.length();
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
            row = inflater.inflate(R.layout.comment,parent,false);
            cell = new ListCell();
            cell.comment = (TextView) row.findViewById(R.id.Comment_area);
            cell.username = (TextView) row.findViewById(R.id.comment_username);
            cell.profile_pic = (ImageView) row.findViewById(R.id.Comment_userpic);
            cell.delete_pic = (ImageView) row.findViewById(R.id.delete_comment);
            row.setTag(cell);

        }else{
            cell = (ListCell) row.getTag();
        }

        try{
            jsonObject = this.commentArray.getJSONObject(position);
            Log.i("jsonString comment_txt", jsonObject.getString("comment"));
            cell.comment.setText(jsonObject.getString("comment"));
            cell.username.setText(jsonObject.getString("name"));
            ImageView profile = cell.profile_pic;
            imageLoader.DisplayImage(imgDir + jsonObject.getString("profile_img"), profile);
            ImageView delete = cell.delete_pic;
            imageLoader.DisplayImage(imgDir + jsonObject.getString("report_delete"), delete);
            Log.i("deleteLink", imgDir + jsonObject.getString("report_delete"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return row;
    }
    private class ListCell{
        private TextView username;
        private TextView comment;
        private ImageView profile_pic;
        private ImageView delete_pic;
    }
}
