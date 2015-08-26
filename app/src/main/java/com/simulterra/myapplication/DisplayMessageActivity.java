package com.simulterra.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Iterator;

public class DisplayMessageActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    private ViewFlipper mViewFlipper;
    private GestureDetector mGestureDetector;
    private ArrayList<Uri> mImageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        setContentView(R.layout.activity_display_message);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
            handleSendText(intent);
        }
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display_message, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing from Howzap");
        setShareIntent(intent);
        //startActivity(intent);

        super.onCreateOptionsMenu(menu);

        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.menu_item_share)
        {
        }

        return super.onOptionsItemSelected(item);
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else {
            sharedText = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        }

        // Update UI to reflect text being shared
        TextView textView = (TextView) findViewById(R.id.display_message);
        textView.setTextSize(20);
        textView.setText(sharedText);
    }

    void handleSendImage(Intent intent) {
        Bundle bundle = intent.getExtras();
        Uri imageUri = (Uri)bundle.get(Intent.EXTRA_STREAM);

        if (imageUri != null) {
            // Update UI to reflect image being shared
            ImageView iv = (ImageView) findViewById(R.id.imageDisplay);
            iv.setImageURI(imageUri);
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            // Get the ViewFlipper
            mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

            Iterator<Uri> it = imageUris.iterator();
            while(it.hasNext())
            {
                Uri obj = it.next();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), obj);

                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);
                    mViewFlipper.addView(imageView);
                }
                catch (Exception e)
                {
                    Log.d("dodo-error in bitmap:",  e.getMessage());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }

}
