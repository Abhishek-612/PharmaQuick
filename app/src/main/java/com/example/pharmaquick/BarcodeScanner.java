package com.example.pharmaquick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeScanner extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        String barcode = rawResult.getContents();
        String type = rawResult.getBarcodeFormat().getName();

        Log.v("result", barcode); // Prints scan results
        Log.v("barcode", type); // Prints the scan format (qrcode, pdf417 etc.)

        if(rawResult!=null){
            getProduct(barcode,type);
        }

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    private void getProduct(String barcode, String type){
        if(type.contains("EAN")){

        }
        else if(type.contains("UPC")){

        }
        else if(type.contains("ISBM")){

        }
    }
}
