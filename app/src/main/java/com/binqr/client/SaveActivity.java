package com.binqr.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class SaveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        byte[] mergedFile = getIntent().getByteArrayExtra("MERGED_FILE");
        String fileName = getIntent().getStringExtra("FILENAME");

        save(fileName, mergedFile);
    }

    private void save(String fileName, byte[] mergedFile) {
        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"BinQR");
        directory.mkdirs();

        File file = new File(directory.getPath(), fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(file.getPath());

            fos.write(mergedFile);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }

    public void open(View view) {
    }

    public void restart(View view) {
        finish();
    }
}
