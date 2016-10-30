package com.example.shehabsalah.mobile_app_project;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Notifications extends Activity {

    ImageView cameraButton;
    ImageView profileButton;
    ImageView homeButton;
    ListView notificationList;
    ImageButton internet;
    JSONArray jsonArray;
    int post_id;
    NotificationCustomAdapter adapter;
    public static int limit;
    int old_limit;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int REQUEST_IMAGE_CAPTURE = 1313;
    String pictureDirectoryPath;
    AlertDialog alertMenuDialog;

    public static Bitmap new_image_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        internet = (ImageButton)findViewById(R.id.NInternet);
        internet.setImageResource(R.mipmap.no_internet);
        internet.setVisibility(View.INVISIBLE);
        cameraButton = (ImageView)findViewById(R.id.NcameraButton);
        profileButton = (ImageView)findViewById(R.id.NprofileButton);
        homeButton = (ImageView)findViewById(R.id.NhomeButton);
        homeButton.setBackgroundColor(Color.BLACK);
        notificationList = (ListView)findViewById(R.id.notificationList);
        limit = 20;
        old_limit = 0;
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
                        Intent showDetails = new Intent(Notifications.this.getApplicationContext(), ProfileActivity.class);
                        Notifications.this.startActivity(showDetails);
                    }
                }
        );
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
                        ProfileActivity.changeProfilePicRequset = false;
                        showMenuDialog();
                    }
                }
        );
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    JSONObject customerClicked = jsonArray.getJSONObject(position);
                    post_id = customerClicked.getInt("post_id");
                    new UpdateNotificationSeen().execute(new ApiConnector());
                    Intent showDetails = new Intent(Notifications.this.getApplicationContext(), MainActivity.class);
                    showDetails.putExtra("post_id", post_id);
                    Notifications.this.startActivity(showDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        Intent showDetails = new Intent(Notifications.this.getApplicationContext(), FullscreenActivity.class);
                        Notifications.this.startActivity(showDetails);
                    }
                }
        );
        notificationList.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (old_limit != scrollState) {
                    if (scrollState >= limit - 1) {
                        old_limit = scrollState;
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                            limit += 20;
                            Log.i("ApiConnector is Called", "" + scrollState);
                            new GetAllNotifications().execute(new ApiConnector());
                        } else {
                            internet.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


    }
    private void showMenuDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Notifications.this);
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
                Toast.makeText(Notifications.this, " " + which, Toast.LENGTH_SHORT).show();
            }
        });
        alertMenuDialog = alert.create();
        alertMenuDialog.show();
    }

    // check if the user has a camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
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

                    Intent i = new Intent(Notifications.this, newPostActivity.class);
                    startActivity(i);
                    // show the image to the user


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }else if(requestCode == REQUEST_IMAGE_CAPTURE){
                Intent i = new Intent(Notifications.this, newPostActivity.class);
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
    protected void onResume() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        limit = 20;
        old_limit = 0;
        if (networkInfo != null && networkInfo.isConnected()) {
            internet.setVisibility(View.INVISIBLE);
            new UpdateNotifications().execute(new ApiConnector());
            new GetAllNotifications().execute(new ApiConnector());
        }else{
            internet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
    public void setListAdapter(JSONArray jsonArray) throws JSONException {
        this.jsonArray = jsonArray;
        if(old_limit == 0)
        {
            adapter = new NotificationCustomAdapter(this,jsonArray);
            this.notificationList.setAdapter(adapter);
        }
        else{
            adapter.setNewNotificationCustomAdapter(this, jsonArray);
            this.notificationList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            this.notificationList.setSelection(limit - 21);
        }
    }
    private class UpdateNotifications extends AsyncTask<ApiConnector,Long,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].updateNotifications(FullscreenActivity.user_id, getApplicationContext());
        }
    }
    private class UpdateNotificationSeen extends AsyncTask<ApiConnector,Long,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].updateNotificationSeen(FullscreenActivity.user_id, getApplicationContext(),post_id);
        }
    }
    private class GetAllNotifications extends AsyncTask<ApiConnector,Long,JSONArray> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Notifications.this);
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if(old_limit == 0)
                pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getAllNotifications(FullscreenActivity.user_id, limit);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            try {
                if(pDialog != null)
                    pDialog.dismiss();
                setListAdapter(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
