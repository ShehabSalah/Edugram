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
 * Created by Shehab Salah on 12/1/2015.
 */
class CustomAdapter extends BaseAdapter /*ArrayAdapter<JSONArray>*/ {
    private JSONArray postArray;
    private JSONArray likeArray;
    private Context activity;
    String post_id;
    ImageView likeContainer;
    private static  LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    ListCell cell;
    String[] links;
    String[] likes;
    private static final String imgDir = "http://www.platformhouse.com/edugram/";
    public CustomAdapter(Context context, JSONArray[] jsonArrays) throws JSONException {
        /*super(context, R.layout.post);*/
        this.postArray = jsonArrays[0];
        this.likeArray = jsonArrays[1];
        this.activity = context;
        links = new String[postArray.length()];
        JSONObject jsonObject2 = null;
        for(int i = 0; i < postArray.length(); i++){
            jsonObject2 = this.postArray.getJSONObject(i);
            String imageName = jsonObject2.getString("post_img");
            String urlForImage = imgDir + imageName;
            links[i] = urlForImage;
        }
        likes = new String[likeArray.length()];
        for (int i = 0; i < likeArray.length(); i++) {
            JSONObject jsonObject = this.likeArray.getJSONObject(i);
            likes[i] = jsonObject.getString("like_image");
        }
        imageLoader = new ImageLoader(activity);
    }
    public void setNewCustomAdapter(Context context, JSONArray[] jsonArrays) throws JSONException{
        this.postArray = jsonArrays[0];
        this.likeArray = jsonArrays[1];
        this.activity = context;
        links = new String[postArray.length()];
        JSONObject jsonObject2 = null;
        for(int i = 0; i < postArray.length(); i++){
            jsonObject2 = this.postArray.getJSONObject(i);
            String imageName = jsonObject2.getString("post_img");
            String urlForImage = imgDir + imageName;
            links[i] = urlForImage;

        }
        likes = new String[likeArray.length()];
        for (int i = 0; i < likeArray.length(); i++) {
            JSONObject jsonObject = this.likeArray.getJSONObject(i);
            likes[i] = jsonObject.getString("like_image");
        }
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        if(postArray != null)
            return postArray.length() == 0?0:postArray.length();
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
            row = inflater.inflate(R.layout.post,parent,false);
            cell = new ListCell();
            cell.postTxt = (TextView) row.findViewById(R.id.postTxt);
            /*cell.post_pic = (ImageView) convertView.findViewById(R.id.post_picture);*/
            cell.comments = (TextView) row.findViewById(R.id.comments);
            cell.likes = (TextView) row.findViewById(R.id.likeNumber);
            cell.post_pic = (ImageView) row.findViewById(R.id.post_picture);
            cell.like_active = (ImageView) row.findViewById(R.id.like);
            cell.username = (TextView) row.findViewById(R.id.user_name);
            cell.faculty = (TextView) row.findViewById(R.id.faculty);
            cell.profile_pic = (ImageView) row.findViewById(R.id.profile_picture);
            cell.delete_report = (ImageView) row.findViewById(R.id.delete_post);
            row.setTag(cell);
        }else{
            cell = (ListCell) row.getTag();
        }
        try{
            jsonObject = this.postArray.getJSONObject(position);
            Log.i("jsonString post_txt", jsonObject.getString("post_txt"));
            cell.postTxt.setText(jsonObject.getString("post_txt"));
            cell.comments.setText(jsonObject.getString("numof_comments") + " Comments");
            cell.likes.setText(jsonObject.getString("numof_likes") + " Likes");
            post_id = jsonObject.getString("id");
            cell.username.setText(jsonObject.getString("name"));
            cell.faculty.setText(jsonObject.getString("faculty_name"));
            Log.i("jsonString post_com", jsonObject.getString("numof_comments"));
            Log.i("jsonString post_likes", jsonObject.getString("numof_likes"));
            /*String imageName = jsonObject.getString("post_img");*/
            ImageView image = cell.post_pic;
            /*String urlForImage = imgDir + imageName;*/
            imageLoader.DisplayImage(links[position], image);
            ImageView profile = cell.profile_pic;
            imageLoader.DisplayImage(imgDir+jsonObject.getString("profile_img"),profile);
            // get the user posts that he likes
            ImageView like = cell.like_active;
            imageLoader.DisplayImage(imgDir+likes[position],like);
            ImageView delete = cell.delete_report;
            imageLoader.DisplayImage(imgDir + jsonObject.getString("delete_report"), delete);
            Log.i("deleteLink", imgDir + jsonObject.getString("delete_report"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        return row;
    }

    /*public CustomAdapter(Context context, String[] users) {
        super(context, R.layout.post, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View postView = convertView;
        if(convertView == null){
            LayoutInflater postLayout = LayoutInflater.from(getContext());
            postView = postLayout.inflate(R.layout.post, parent, false);
        }
        String singleUser = getItem(position);
        TextView userName = (TextView)postView.findViewById(R.id.user_name);
        userName.setText(singleUser);
        return postView;
    }*/
    private class ListCell{
        private TextView username;
        private TextView faculty;
        private TextView postTxt;
        private TextView likes;
        private TextView comments;
        private ImageView profile_pic;
        private ImageView post_pic;
        private ImageView like_active;
        private ImageView delete_report;
        private ImageView actual_imageView_in_profile;
    }
}