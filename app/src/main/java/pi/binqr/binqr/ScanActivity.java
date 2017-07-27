package pi.binqr.binqr;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.*;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class ScanActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private List<CheckBox> checkBoxes;
    private Context context = this;
    private SplittedFile splittedFile;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null) {
                return;
            }

            byte[] rawBytes = new byte[0];
            try {
                rawBytes = result.getText().getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (splittedFile.isPartAdded(rawBytes)) {
                Toast.makeText(context, "Ignored: " + String.valueOf(rawBytes[0]), Toast.LENGTH_SHORT).show();
                return;
            }

            QRCode qrCode = splittedFile.addPart(rawBytes);

            if (splittedFile.isCompleted()) {
                splittedFile.save(Environment.getExternalStorageDirectory());
            }

            if (checkBoxes.size() == 0) {
                LinearLayout progress = (LinearLayout) findViewById(R.id.progress);
                for (int i = 0; i < splittedFile.getPiecesQuantity(); i++) {
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(String.format(Locale.getDefault(), "%d", i + 1));
                    checkBox.setClickable(false);
                    progress.addView(checkBox);
                    checkBoxes.add(checkBox);
                }
                TextView first_scan_text = (TextView) findViewById(R.id.first_scan_text);
                first_scan_text.setVisibility(View.GONE);
            }

            checkBoxes.get(qrCode.getMetadata().getNumber() - 1).setChecked(true);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        checkAndAskForPermissions();

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        DecoderFactory decoderFactory = new DefaultDecoderFactory(
                EnumSet.of(BarcodeFormat.QR_CODE),
                null,
                "ISO-8859-1",
                false
        );
        barcodeView.getBarcodeView().setDecoderFactory(decoderFactory);
//        barcodeView.getBarcodeView().getCameraSettings().setAutoFocusEnabled(false);
        barcodeView.decodeContinuous(callback);

        splittedFile = new SplittedFile();
        checkBoxes = new ArrayList<>();
    }

    private void checkAndAskForPermissions() {
        String[] neededPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        Set<String> ungrantedPermissions = new HashSet<>();

        for (String neededPermission : neededPermissions) {
            if (ContextCompat.checkSelfPermission(this, neededPermission) != PackageManager.PERMISSION_GRANTED) {
                ungrantedPermissions.add(neededPermission);
            }
        }

        if (ungrantedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this, ungrantedPermissions.toArray(new String[]{}), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return barcodeView.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
