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
}
