package com.example.shehabsalah.mobile_app_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ProfileActivity extends Activity {
    ImageView notification;
    ImageView notification_active;
    ImageView cameraButton;
    ImageView profileButton;
    ImageView homeButton;
    ImageButton internet;
    JSONArray jsonArray;
    ImageView backimage;
    public static ImageView profile_image_in_profile;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int REQUEST_IMAGE_CAPTURE = 1313;
    int deletePosition;
    int post_id;
    boolean goPosition;
    AlertDialog alertDialog;
    AlertDialog alertMenuDialog;
    public static int user_id;
    public static int faculty_id;
    public static CustomAdapter adapter;
    private ListView post;
    public static int limit;
    int old_limit;
    String pictureDirectoryPath;
    public static Bitmap new_image_post;
    public static boolean changeProfilePicRequset;
    Button dummy_button;
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        internet = (ImageButton) findViewById(R.id.Internet);
        internet.setImageResource(R.mipmap.no_internet);
        internet.setVisibility(View.INVISIBLE);
        notification = (ImageView) findViewById(R.id.notification);
        backimage = (ImageView)findViewById(R.id.backimage);
        dummy_button = (Button)findViewById(R.id.dummy_button);
        profile_image_in_profile = (ImageView)findViewById(R.id.profile_image_in_profile);
        profile_image_in_profile.setImageResource(R.mipmap.profile_picture);
        limit = 10;
        changeProfilePicRequset = false;
        old_limit = 0;
        deletePosition = 0;
        faculty_id = UserLoginActivity.faculty_id;
        user_id = UserLoginActivity.user_id;
        goPosition = false;
        notification_active = (ImageView) findViewById(R.id.notification_active);
        notification_active.setVisibility(View.INVISIBLE);
        cameraButton = (ImageView) findViewById(R.id.cameraButton);
        profileButton = (ImageView) findViewById(R.id.profileButton);
        profileButton.setBackgroundColor(Color.BLACK);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetMyProfilePicture().execute(new ApiConnector());
            internet.setVisibility(View.INVISIBLE);
        } else {
            internet.setVisibility(View.VISIBLE);
        }
        homeButton = (ImageView) findViewById(R.id.homeButton);
        dummy_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent showDetails = new Intent(ProfileActivity.this.getApplicationContext(), SettingsActivity.class);
                ProfileActivity.this.startActivity(showDetails);
            }
        });
        profile_image_in_profile.setOnClickListener( new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                profile_image_in_profile.setVisibility(View.INVISIBLE);
                int Delay = 100;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile_image_in_profile.setVisibility(View.VISIBLE);
                    }
                }, Delay);
                changeProfilePicRequset = true;
                showMenuDialog();
            }
        });
        cameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraButton.setImageResource(R.mipmap.camera2);
                        cameraButton.setBackgroundColor(Color.BLACK);
                        int Delay = 200;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cameraButton.setImageResource(R.mipmap.camera);
                                cameraButton.setBackgroundColor(242424);
                            }
                        }, Delay);
                        changeProfilePicRequset = false;
                        showMenuDialog();
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
                        Intent showDetails = new Intent(ProfileActivity.this.getApplicationContext(), Notifications.class);
                        ProfileActivity.this.startActivity(showDetails);
                    }
                }
        );
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
                        Intent showDetails = new Intent(ProfileActivity.this.getApplicationContext(), FullscreenActivity.class);
                        ProfileActivity.this.startActivity(showDetails);
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
                        Intent showDetails = new Intent(ProfileActivity.this.getApplicationContext(), Notifications.class);
                        ProfileActivity.this.startActivity(showDetails);
                    }
                }
        );

        //Get Object from list view resources
        this.post = (ListView) findViewById(R.id.listView2);

        // If Internet Connection is not exist and the Button of refresh (VISIBLE)
        // set on click listener to see if the user try to reconnect or not
        internet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                ProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                            new GetMyPosts().execute(new ApiConnector());
                        } else {
                            internet.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        // If the user try to get the specific post to interact with it
        post.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject customerClicked = jsonArray.getJSONObject(position);
                    Intent showDetails = new Intent(ProfileActivity.this.getApplicationContext(), MainActivity.class);
                    showDetails.putExtra("post_id", customerClicked.getInt("id"));
                    ProfileActivity.this.startActivity(showDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        post.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("comment_id", jsonArray.length() + "");
                deletePosition = position;
                showDialog();
                return true;
            }
        });

        post.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("ApiConnector is Called", "" + firstVisibleItem);
                if (old_limit != firstVisibleItem) {
                    if (firstVisibleItem >= limit - 2) {
                        old_limit = firstVisibleItem;
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                            limit += 10;
                            Log.i("ApiConnector is Called", "" + firstVisibleItem);
                            new GetMyPosts().execute(new ApiConnector());
                        } else {
                            internet.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
    }
    private void showMenuDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        if(changeProfilePicRequset)
            alert.setTitle("Change Profile Picture");
        else
            alert.setTitle("New Post");
        alert.setItems(R.array.camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Browse
                    // invoke the image gallery using an implict intent.
                    Intent photoPick = new Intent(Intent.ACTION_PICK);
                    // where do we want to find the data?
                    File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    pictureDirectoryPath = pictureDirectory.getPath();
                    // finally, get a URI representation
                    Uri data = Uri.parse(pictureDirectoryPath);

                    // set the data and type.  Get all image types.
                    photoPick.setDataAndType(data, "image/*");

                    // we will invoke this activity, and get something back from it.
                    startActivityForResult(photoPick, IMAGE_GALLERY_REQUEST);
                } else {
                    launchCamera();
                }
            }
        });
        alertMenuDialog = alert.create();
        alertMenuDialog.show();
    }
    // launch the Camera
    public void launchCamera() {
        // we intent to tack a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // make the qualty of the picture is high
        File file = getFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        // tack a picture and send it to onActivityResult
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD Card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    new_image_post = BitmapFactory.decodeStream(inputStream);

                    Intent i = new Intent(ProfileActivity.this, newPostActivity.class);
                    startActivity(i);
                    // show the image to the user


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Intent i = new Intent(ProfileActivity.this, newPostActivity.class);
                startActivity(i);
            }
        }
    }
    private File getFile() {
        File folder = new File("sdcard/EG");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return new File(folder, "cam_image.jpg");
    }

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setTitle("Delete/Report Post");
        alert.setMessage("Do you want to delete/report this Post?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go and delete or report this comment
                Toast.makeText(ProfileActivity.this, "This post has been deleted/reported", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject customerClicked = jsonArray.getJSONObject(deletePosition);
                    post_id = customerClicked.getInt("id");
                    Log.i("comment_id", "" + customerClicked.getInt("id"));
                    new DeleteReport().execute(new ApiConnector());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        // Remove adapter refference from list
        post.setAdapter(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        new CheckIfUserExists().execute(1);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        limit = 10;
        old_limit = 0;
        if (networkInfo != null && networkInfo.isConnected()) {
            internet.setVisibility(View.INVISIBLE);
            if(newPostActivity.profilePicUdated){
                newPostActivity.profilePicUdated = false;
                new GetMyProfilePicture().execute(new ApiConnector());
            }

            new CheckNotifications().execute(new ApiConnector());
            new GetMyPosts().execute(new ApiConnector());
        } else {
            internet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void setListAdapter(JSONArray[] jsonArrays) throws JSONException {
        if(jsonArrays != null){
            this.jsonArray = jsonArrays[0];
            if (old_limit == 0) {
                adapter = new CustomAdapter(this, jsonArrays);
                backimage.setVisibility(View.INVISIBLE);
                this.post.setAdapter(adapter);
            } else {
                adapter.setNewCustomAdapter(this, jsonArrays);
                backimage.setVisibility(View.INVISIBLE);
                this.post.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                this.post.setSelection(limit - 12);
            }
        }
    }
    private class DeleteReport extends AsyncTask<ApiConnector, Long, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                new GetMyPosts().execute(new ApiConnector());
                adapter.notifyDataSetChanged();
                goPosition = true;
                Log.i("deletePosition", "" + deletePosition);
            }
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].deletePost(getApplicationContext(), FullscreenActivity.user_id, post_id);
        }
    }

    private class CheckNotifications extends AsyncTask<ApiConnector, Long, Boolean> {
        @Override
        protected void onPreExecute() {
            notification_active.setVisibility(View.INVISIBLE);
            notification.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                notification_active.setVisibility(View.VISIBLE);
            else
                notification.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].checkNotifications(user_id);
        }
    }
    private class GetMyPosts extends AsyncTask<ApiConnector, Long, JSONArray[]> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            backimage.setVisibility(View.VISIBLE);
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if (old_limit == 0 || !goPosition)
                pDialog.show();
        }

        @Override
        protected JSONArray[] doInBackground(ApiConnector... params) {
            return params[0].getMyPost(faculty_id,user_id,limit);
        }

        @Override
        protected void onPostExecute(JSONArray[] jsonArrays) {
            try {
                pDialog.dismiss();
                setListAdapter(jsonArrays);
                if (goPosition) {
                    goPosition = false;
                    post.setSelection(deletePosition);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class CheckIfUserExists extends AsyncTask<Integer,Long,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean){
                Intent i = new Intent(ProfileActivity.this,UserLoginActivity.class);
                startActivity(i);
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            return readFromFile2() && readFromFile();
        }
    }
    private class GetMyProfilePicture extends AsyncTask<ApiConnector,Long,String>{
        @Override
        protected void onPostExecute(String profilePath) {
            if (profilePath == null)
                Toast.makeText(getApplicationContext(), "Error: when trying to get your profile picture", Toast.LENGTH_LONG).show();
            else{
                imageLoader = new ImageLoader(getApplicationContext());
                ImageView actualProfilePic = ProfileActivity.profile_image_in_profile;
                Log.i("imagePath",profilePath);
                imageLoader.DisplayImage("http://www.platformhouse.com/edugram/"+profilePath,actualProfilePic);
            }
        }

        @Override
        protected String doInBackground(ApiConnector... params) {
            return params[0].getMyPic(FullscreenActivity.user_id,getApplicationContext());
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
