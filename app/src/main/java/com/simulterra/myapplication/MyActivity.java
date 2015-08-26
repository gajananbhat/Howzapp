package com.simulterra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;


public class MyActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.simulterra.myapplication.MESSAGE";
    public final static String ACCESS_KEY_INTENT = "com.simulterra.myapplication.ACCESSKEY";
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //store the accesskey in shared preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String myAccessKey = "my_temp_access_key";
        editor.putString(getString(R.string.com_simulterra_myapplication_ACCESSKEY), myAccessKey);
        editor.commit();

        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!gpsenabled)
        {
            // Create a dialog here that requests the user to enable GPS, and use an intent
            // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
            // to take the user to the Settings screen to enable GPS when they click "OK"
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Enable Gps called");
            startActivity(intent);
        }
        /*else
        {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Gps is enabled");
            startActivity(intent);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //store the accesskey in shared preferences
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            String acccessKey = sharedPref.getString(getString(R.string.com_simulterra_myapplication_ACCESSKEY), "access key not found");
            //return true;
            //DISPLAY THE MESSAGE
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(ACCESS_KEY_INTENT, acccessKey);
            startActivity(intent);

        }
        else if (id == R.id.action_search) {
            //DISPLAY THE MESSAGE
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Search Clicked");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        //DISPLAY THE MESSAGE
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /*called when user clicks a call phone number*/
    public void callPhone(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_phone);
        String phoneNumber = editText.getText().toString();
        Log.d("dodo-phone", phoneNumber);

        Uri number = Uri.parse("tel:" + phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(callIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe)
            startActivity(callIntent);
        else
        {
            //warn that could not find the intent
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Cant invoke the dialer");
            startActivity(intent);
        }
    }

    /*called when user clicks a browse webpage*/
    public void callBrowse(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_browse);
        String website = editText.getText().toString();
        Log.d("dodo-page", website);

        Uri webpage = Uri.parse(website);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe)
            startActivity(webIntent);
        else
        {
            //warn that could not find the intent
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Cant invoke the browser");
            startActivity(intent);
        }
    }

    /*called when user clicks a mapbutton*/
    public void callMap(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_map);
        String address = editText.getText().toString();
        Log.d("dodo-map", address.replaceAll(" ", "+"));

        Uri addressUri = Uri.parse("geo:0,0?q=" + address.replaceAll(" ", "+"));
        //Uri addressUri = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, addressUri);

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe)
            startActivity(webIntent);
        else
        {
            //warn that could not find the intent
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Cant invoke the map");
            startActivity(intent);
        }
    }

    /*called when user clicks a share button*/
    public void callShare(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_share);
        String status = editText.getText().toString();
        Log.d("dodo-status", status);

        String title = "Share your status with";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, status );

        Intent chooser = Intent.createChooser(shareIntent, title);

        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if(isIntentSafe)
            startActivity(chooser);
        else
        {
            //warn that could not find the intent
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "Cant invoke the intent chooser");
            startActivity(intent);
        }
    }

    //called when get contact button is pressed
    public void callPickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    //start chatting in generic chatting channel
    public void callChat(View view) {
        //warn that could not find the intent
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "start chatting yo");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                // Do something with the phone number...
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Picked Number is " + number);
                startActivity(intent);
            }
        }
    }
}
