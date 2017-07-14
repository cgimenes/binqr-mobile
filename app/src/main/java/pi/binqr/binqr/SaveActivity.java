package pi.binqr.binqr;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class SaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

//        byte[] file = getIntent().getExtras().getByteArray("file");
//        saveFile(file);
    }

    protected void saveFile(byte[]... bytes) {
        File file = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(file.getPath());

            fos.write(bytes[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }
}
