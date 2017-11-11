package com.binqr.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveActivity extends Activity {

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        byte[] mergedFile = getIntent().getByteArrayExtra("MERGED_FILE");
        String fileName = getIntent().getStringExtra("FILENAME");

        file = save(fileName, mergedFile);
    }

    private File save(String fileName, byte[] mergedFile) {
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "BinQR");
        directory.mkdirs();

        File file = new File(directory.getPath(), fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());

            fos.write(mergedFile);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("SaveActivity", "Exception in save", e);
        }
        return file;
    }

    public void open(View view) {
        try {
            FileOpen.openFile(this, file);
        } catch (IOException e) {
            Log.e("SaveActivity", "Exception in open", e);
        }
    }

    public void restart(View view) {
        finish();
    }
}
