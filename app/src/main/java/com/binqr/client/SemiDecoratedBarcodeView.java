package com.binqr.client;

import android.content.Context;
import android.util.AttributeSet;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class SemiDecoratedBarcodeView extends DecoratedBarcodeView {

    public SemiDecoratedBarcodeView(Context context) {
        super(context);
    }

    public SemiDecoratedBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SemiDecoratedBarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void decodeContinuous(BarcodeCallback callback) {
        super.getBarcodeView().decodeContinuous(callback);
    }
}
