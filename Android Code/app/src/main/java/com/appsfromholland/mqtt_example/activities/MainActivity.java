package com.appsfromholland.mqtt_example.activities;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.appsfromholland.mqtt_example.R;


/**
Deze klasse zorgt voor het tonen van het eerste scherm. Hierin wordt aangetoond dat de telefoon contact moet maken met de nfc-kaart (een paaltje).
 Wanneer deze actie voltooid is zal een nieuwe activity worden gestart namelijk het wachtscherm.
 */
public class MainActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start services
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = findViewById(R.id.InstructionText);

        textView.setText("Welkom!" +
                "\nScan je telefoon tegen de paal " +
                "om mee te doen met het spel");

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            System.out.println("geen adapter");
            finish();
            return;
        }

        handleIntent(getIntent());

    }


    private void handleIntent(Intent intent) {
        this.textView.setText("Houd je telefoon tegen een magisch paaltje in de rij");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            View view = new View(this);
            Intent waitingForPlayerIntent = new Intent(view.getContext(), WaitingForPlayersActivity.class);
            view.getContext().startActivity(waitingForPlayerIntent);

        }
    }
}
