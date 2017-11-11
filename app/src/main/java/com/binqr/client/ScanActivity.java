package com.binqr.client;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.*;
import com.journeyapps.barcodescanner.camera.CameraSettings.FocusMode;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class ScanActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private DecoratedBarcodeView barcodeView;
    private List<CheckBox> checkBoxes;
    private Context context = this;
    private SplittedFile splittedFile;
    private String[] neededPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
        barcodeView.getBarcodeView().getCameraSettings().setFocusMode(FocusMode.MACRO);

        barcodeView.decodeContinuous(callback);

        barcodeView.setStatusText("");

        splittedFile = new SplittedFile();
        checkBoxes = new ArrayList<>();
    }

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
                Intent intent = new Intent(getBaseContext(), SaveActivity.class);
                intent.putExtra("MERGED_FILE", splittedFile.getMergedFile());
                intent.putExtra("FILENAME", splittedFile.getFilename());
                startActivity(intent);
                finish();
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

    private void checkAndAskForPermissions() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        for (String neededPermission : neededPermissions) {
            if (ContextCompat.checkSelfPermission(this, neededPermission) != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
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
