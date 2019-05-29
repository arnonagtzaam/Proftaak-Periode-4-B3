package com.example.nfctestapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingForPlayers extends AppCompatActivity {
private TextView connectingTextView;
private boolean twoPlayersConnected;
private static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_player);
        this.connectingTextView = findViewById(R.id.connectionTextView);
        this.connectingTextView.setText(R.string.connectionTextView);
        this.context = this;
        this.twoPlayersConnected = true;

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(twoPlayersConnected){
                    View view = new View(WaitingForPlayers.context);
                    Intent TicTacToeIntent = new Intent(view.getContext(),TicTacToeScreen.class);
                    view.getContext().startActivity(TicTacToeIntent);
                }
            }
        };
        timer.schedule(timerTask,2000);

    }
}
