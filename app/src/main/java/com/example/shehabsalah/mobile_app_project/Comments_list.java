package com.example.shehabsalah.mobile_app_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class Comments_list extends Activity implements DialogInterface {
    ListView list;
    CommentCustomAdapter adapter;
    int post_id;
    ImageButton internet;
    ImageView set_comment;
    EditText enter_comment_holder;
    String txt;
    int position;
    JSONArray localJsonArray;
    static int deletePosition;
    int comment_id;
    AlertDialog alertDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conmments_list);
        list = (ListView) findViewById(R.id.Comment_area2);
        internet = (ImageButton) findViewById(R.id.Internet);
        set_comment = (ImageView) findViewById(R.id.set_comment);
        enter_comment_holder = (EditText) findViewById(R.id.enter_comment_holder);
        position = 0;
        deletePosition = 0;
        txt = "";
        this.post_id = getIntent().getIntExtra("post_id", -1);

        if (post_id > 0) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                internet.setVisibility(View.INVISIBLE);
                new GetAllComments().execute(new ApiConnector());
            } else {
                internet.setVisibility(View.VISIBLE);
            }
        }
        internet.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr1 = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo1 = connMgr1.getActiveNetworkInfo();
                if (networkInfo1 != null && networkInfo1.isConnected()) {
                    internet.setVisibility(View.INVISIBLE);
                    new GetAllComments().execute(new ApiConnector());
                } else {
                    internet.setVisibility(View.VISIBLE);

                }
            }
        });
       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Log.i("comment_id", localJsonArray.length() + "");
               deletePosition = position;
               showDialog();
            return true;
           }
       });
        list.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem > position) {
                    position = firstVisibleItem;
                    Log.i("comment_position",""+position);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
        set_comment.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
                String comment = enter_comment_holder.getText().toString();
                enter_comment_holder.setText("");
                if (comment.length() > 0) {
                    txt = comment;
                    new SetNewComment().execute(new ApiConnector());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You cannot post an empty comment", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Comments_list.this);
        alert.setTitle("Delete/Report Comment");
        alert.setMessage("Do you want to delete/report this comment?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go and delete or report this comment
                Toast.makeText(Comments_list.this, "This comment has been deleted/reported", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject customerClicked = localJsonArray.getJSONObject(deletePosition);
                    comment_id = customerClicked.getInt("comment_id");
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
    private void hideSoftKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Comments_list Page", // TODO: Define a title for the content shown.
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
                "Comments_list Page", // TODO: Define a title for the content shown.
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

    private class DeleteReport extends AsyncTask<ApiConnector,Long,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                new GetAllComments().execute(new ApiConnector());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            return params[0].deleteComment(comment_id, getApplicationContext(), FullscreenActivity.user_id,post_id);
        }
    }
    private class SetNewComment extends AsyncTask<ApiConnector, Long, Boolean> {
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                new GetAllComments().execute(new ApiConnector());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected Boolean doInBackground(ApiConnector... params) {
            Log.i("newposttxt",txt);
            return params[0].setComment(post_id, txt, getApplicationContext(), FullscreenActivity.user_id);
        }
    }

    @Override
    public void cancel() {
        //do nothing
    }

    @Override
    public void dismiss() {
        //do nothing
    }



    private class GetAllComments extends AsyncTask<ApiConnector, Long, JSONArray> {
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            localJsonArray = jsonArray;
            getComments(jsonArray);
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].getAllComments(post_id);
        }
    }

    private void getComments(JSONArray jsonArray) {
        try {
            adapter = new CommentCustomAdapter(this, jsonArray);
            list.setAdapter(adapter);
            if(position > 0)
                list.setSelection(position + 1);
            Log.i("comments", "setAdapter for comments");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
