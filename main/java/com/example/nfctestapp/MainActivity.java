package com.example.nfctestapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.nfc.*;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private NfcAdapter mNfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = findViewById(R.id.text);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            System.out.println("geen adapter");
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            this.textView.setText("NFC is disabled.");
        } else {
            textView.setText("NFC is enabled");
        }

        handleIntent(getIntent());
    }




    private void handleIntent(Intent intent) {
        this.textView.setText("intent: " + intent.getAction());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            this.textView.setText("Registered NFC input");
            System.out.println("karelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarelkarel");
        }
    }
}
