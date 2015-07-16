package com.example.miguelcatalino.lab_dailyselfie;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DailySelfieApp";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private File fileDir;
    private Intent camera_intent;
    private SelfieViewAdapter mAdapter;
    private ListView lv_selfie;
    private Alarm alarm = new Alarm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileDir = getFileDirectory();
        lv_selfie = (ListView) findViewById(R.id.lv_selfie);
        mAdapter = new SelfieViewAdapter(getApplicationContext());
        lv_selfie.setAdapter(mAdapter);
        lv_selfie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageRecord image = (ImageRecord) mAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + image.getPath()), "image/*");
                startActivity(intent);

            }
        });

        getAllSelfies();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarm.cancelAlarm(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarm.setAlarm(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
            startActivityForResult(camera_intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Saved:\n" + fileUri.getPath()
                        , Toast.LENGTH_LONG).show();

                ImageRecord ir = new ImageRecord(fileUri.getLastPathSegment(), fileUri.getPath());
                addNewSelfie(ir);
            } else if (resultCode == RESULT_CANCELED) {
            } else {
            }
        }
    }


    private Uri getOutputMediaFileUri(int type) {
        File fl = getOutputMediaFile(type);
        return Uri.fromFile(fl);
    }


    private File getOutputMediaFile(int type) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.i("File", timeStamp);
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE) {
            String path = fileDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg";
            mediaFile = new File(path);

        }
        return mediaFile;
    }

    public void addNewSelfie(ImageRecord selfie) {

        if (selfie == null) {
            Toast.makeText(getApplicationContext(), "Image could not be acquired", Toast.LENGTH_LONG).show();
        } else {
            mAdapter.add(selfie);
        }

    }

    private File getFileDirectory() {
        File mediaStorageDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), TAG);
        } else {
            mediaStorageDir = this.getApplicationContext().getFilesDir();
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("File", "failed to create directory");
            }
        }
        return mediaStorageDir;
    }

    private void getAllSelfies() {

        for (File i : fileDir.listFiles()) {
            addNewSelfie(new ImageRecord(i.getName(), i.getAbsolutePath()));
        }
    }
}
