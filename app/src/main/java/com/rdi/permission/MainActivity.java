package com.rdi.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private File[] mFilesArray;
    private File[] mFilesArrayInDirrectore;

    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 900;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.list_package);


        if (isExternalStorageReadable()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                readAllFilesFromExternalStorage();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
            }
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                readAllFilesFromDirectory(position);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readAllFilesFromExternalStorage();
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    private void readAllFilesFromExternalStorage() {
        File rootFolder = Environment.getExternalStorageDirectory();
        mFilesArray = rootFolder.listFiles();
        mListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mFilesArray));
    }

    private void readAllFilesFromDirectory(int position) {
        File dir = new File( mFilesArray[position].toString());
        if (dir.isDirectory()) {
            mFilesArrayInDirrectore = dir.listFiles();
            Log.d("readAllFiles", "readAllFilesFromDirectory: " + dir +"readAllFilesFromDirectory: "+ mFilesArrayInDirrectore);
            mListView.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, mFilesArrayInDirrectore));
        }

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        readAllFilesFromExternalStorage();
    }
}
