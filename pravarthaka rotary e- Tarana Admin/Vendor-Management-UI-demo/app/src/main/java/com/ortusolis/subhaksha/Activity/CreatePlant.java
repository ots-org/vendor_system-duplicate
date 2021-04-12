package com.ortusolis.subhaksha.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ortusolis.subhaksha.R;
import com.ortusolis.subhaksha.pojo.PlantModel;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreatePlant extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView squareImageView;
    EditText plantName,plantDesc;
    String imagePath = "";
    Toolbar mToolbar;
    ActionBar action;
    LinearLayout rootLayout;
    Uri fileUri;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private int revealX;
    private int revealY;

    public static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = "WDMS" ;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_add);

        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

        fab = findViewById(R.id.fab);
        squareImageView = findViewById(R.id.picture);
        plantName = findViewById(R.id.text);
        plantDesc = findViewById(R.id.description);
        mToolbar = findViewById(R.id.toolbar);

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
            toolbarTitle.setText("New Plant");
            action.setCustomView(viewActionBar, params);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            action.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorPrimary));
            toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
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

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21212 && resultCode == RESULT_OK) {
            try{
                imagePath = fileUri.getPath()/*getRealPathFromURI(fileUri)*/;

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
                if (!imagePath.isEmpty() && !plantName.getText().toString().isEmpty() && !plantDesc.getText().toString().isEmpty()){

                    PlantModel plantModel = new PlantModel();
                    plantModel.setPath(imagePath);
                    plantModel.setName(plantName.getText().toString());
                    plantModel.setDescription(plantDesc.getText().toString());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("plant",new Gson().toJson(plantModel));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else if (imagePath.isEmpty()){
                    Toast.makeText(CreatePlant.this, "Please Select Plant Image", Toast.LENGTH_LONG).show();
                }
                else if (plantName.getText().toString().isEmpty()){
                    plantName.setError("Please Enter Plant Name");
                }
                else if (plantDesc.getText().toString().isEmpty()){
                    plantDesc.setError("Please Enter Plant Description");
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
