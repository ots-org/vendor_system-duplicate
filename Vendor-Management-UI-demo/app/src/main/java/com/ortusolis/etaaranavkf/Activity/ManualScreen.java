package com.ortusolis.etaaranavkf.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ortusolis.etaaranavkf.NetworkUtility.Constants;
import com.ortusolis.etaaranavkf.R;
import com.ortusolis.etaaranavkf.Utility.SwitchButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManualScreen extends AppCompatActivity {

    ImageView fab;
    ImageView squareImageView;
    String imagePath = "";
    Toolbar mToolbar;
    ActionBar action;
    SwitchButton switchButton;

    public static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = "WDMS" ;
    Uri fileUri;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_screen);

        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

        fab = findViewById(R.id.fab);
        squareImageView = findViewById(R.id.picture);
        mToolbar = findViewById(R.id.toolbar);
        switchButton = findViewById(R.id.switchButton);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);
            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);
            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("Manual");
            action.setCustomView(viewActionBar, params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent,21212);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21212 && resultCode == RESULT_OK) {
            try{
                imagePath = fileUri.getPath();
                //Getting the Bitmap from Gallery
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                //Setting the Bitmap to ImageView
                if (squareImageView!=null){
                    squareImageView.setImageBitmap(imageBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            imagePath = "";
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = null;
        try {

            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentURI,  proj, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                if (!imagePath.isEmpty() ){
                    Constants.SendSms(ManualScreen.this, switchButton.isChecked() ? "/motor on/" : "/motor off/");
                    Intent returnIntent = new Intent(ManualScreen.this,HomeActivity.class);
                    Bundle bundle = getIntent().getExtras();
                    bundle.putBoolean("manual_rep",switchButton.isChecked());
                    returnIntent.putExtras(bundle);
                    startActivity(returnIntent);
                    finish();
                }
                else if (imagePath.isEmpty()){
                    Toast.makeText(ManualScreen.this, "Please Select Plant Image", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_add, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }

    /*
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+"temp"+File.separator+IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } /*else if (type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "VID_" + timeStamp + ".mp4");
			}*/ else {
            return null;
        }

        return mediaFile;
    }

}
