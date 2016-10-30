package com.example.shehabsalah.mobile_app_project;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FullscreenActivity extends Activity {
    ImageView notification;
    ImageView notification_active;
    ImageView cameraButton;
    ImageView profileButton;
    ImageView homeButton;
    ImageButton internet;
    JSONArray jsonArray;
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
    int last_post;
    public static Bitmap new_image_post;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        internet = (ImageButton) findViewById(R.id.Internet);
        internet.setImageResource(R.mipmap.no_internet);
        internet.setVisibility(View.INVISIBLE);
        notification = (ImageView) findViewById(R.id.notification);
        limit = 10;
        old_limit = 0;
        deletePosition = 0;
        last_post = 0;
        faculty_id = UserLoginActivity.faculty_id;
        user_id = UserLoginActivity.user_id;
        goPosition = false;
        notification_active = (ImageView) findViewById(R.id.notification_active);
        notification_active.setVisibility(View.INVISIBLE);
        cameraButton = (ImageView) findViewById(R.id.cameraButton);
        profileButton = (ImageView) findViewById(R.id.profileButton);
        homeButton = (ImageView) findViewById(R.id.homeButton);
        homeButton.setBackgroundColor(Color.BLACK);
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
                        Intent showDetails = new Intent(FullscreenActivity.this.getApplicationContext(), ProfileActivity.class);
                        FullscreenActivity.this.startActivity(showDetails);
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
                        Intent showDetails = new Intent(FullscreenActivity.this.getApplicationContext(), Notifications.class);
                        FullscreenActivity.this.startActivity(showDetails);
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
                        Intent showDetails = new Intent(FullscreenActivity.this.getApplicationContext(), Notifications.class);
                        FullscreenActivity.this.startActivity(showDetails);
                    }
                }
        );

        //Get Object from list view resources
        this.post = (ListView) findViewById(R.id.listView);

        // If Internet Connection is not exist and the Button of refresh (VISIBLE)
        // set on click listener to see if the user try to reconnect or not
        internet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connMgr = (ConnectivityManager)
                                FullscreenActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                            new GetAllPosts().execute(new ApiConnector());
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
                    Intent showDetails = new Intent(FullscreenActivity.this.getApplicationContext(), MainActivity.class);
                    showDetails.putExtra("post_id", customerClicked.getInt("id"));
                    FullscreenActivity.this.startActivity(showDetails);
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
                    last_post = firstVisibleItem;
                    if (firstVisibleItem >= limit - 2) {
                        old_limit = firstVisibleItem;
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            internet.setVisibility(View.INVISIBLE);
                            limit += 10;
                            Log.i("ApiConnector is Called", "" + firstVisibleItem);
                            new GetAllPosts().execute(new ApiConnector());
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showMenuDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FullscreenActivity.this);
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

                    Intent i = new Intent(FullscreenActivity.this, newPostActivity.class);
                    startActivity(i);
                    // show the image to the user


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }else if(requestCode == REQUEST_IMAGE_CAPTURE){
                Intent i = new Intent(FullscreenActivity.this, newPostActivity.class);
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
        AlertDialog.Builder alert = new AlertDialog.Builder(FullscreenActivity.this);
        alert.setTitle("Delete/Report Post");
        alert.setMessage("Do you want to delete/report this Post?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go and delete or report this comment
                Toast.makeText(FullscreenActivity.this, "This post has been deleted/reported", Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        limit = 10;
        old_limit = 0;
        if (networkInfo != null && networkInfo.isConnected()) {
            internet.setVisibility(View.INVISIBLE);
            new CheckNotifications().execute(new ApiConnector());
            new GetAllPosts().execute(new ApiConnector());
        } else {
            internet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void setListAdapter(JSONArray[] jsonArrays) throws JSONException {
        this.jsonArray = jsonArrays[0];
        if (last_post > 0) {
            adapter.setNewCustomAdapter(this, jsonArrays);
            this.post.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            this.post.setSelection(last_post);
        } else if(old_limit == 0){
            adapter = new CustomAdapter(this, jsonArrays);
            this.post.setAdapter(adapter);
        } else {
            adapter.setNewCustomAdapter(this, jsonArrays);
            this.post.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            this.post.setSelection(limit - 12);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Fullscreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.shehabsalah.mobile_app_project/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Fullscreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.shehabsalah.mobile_app_project/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class DeleteReport extends AsyncTask<ApiConnector, Long, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                new GetAllPosts().execute(new ApiConnector());
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

    private class GetAllPosts extends AsyncTask<ApiConnector, Long, JSONArray[]> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(FullscreenActivity.this);
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if (old_limit == 0 || !goPosition)
                pDialog.show();
        }

        @Override
        protected JSONArray[] doInBackground(ApiConnector... params) {
            return params[0].getAllPost(faculty_id, user_id, limit);
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
}
