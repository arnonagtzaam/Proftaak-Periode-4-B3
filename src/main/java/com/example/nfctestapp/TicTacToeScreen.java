package com.example.nfctestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TicTacToeScreen extends AppCompatActivity {

    private static TextView turnText;
    private static int turnCounter;
    private Context context;
    private ArrayList<ImageView> matrixList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        this.context = this;

        this.turnText = findViewById(R.id.TurnText);
        turnText.setText("Turn: 1");
        this.turnCounter = 1;

        this.matrixList = new ArrayList<>();
        this.matrixList.add((ImageView) findViewById(R.id.matrix1));
        this.matrixList.add((ImageView) findViewById(R.id.matrix2));
        this.matrixList.add((ImageView) findViewById(R.id.matrix3));
        this.matrixList.add((ImageView) findViewById(R.id.matrix4));
        this.matrixList.add((ImageView) findViewById(R.id.matrix5));
        this.matrixList.add((ImageView) findViewById(R.id.matrix6));
        this.matrixList.add((ImageView) findViewById(R.id.matrix7));
        this.matrixList.add((ImageView) findViewById(R.id.matrix8));
        this.matrixList.add((ImageView) findViewById(R.id.matrix9));


        for (final ImageView matrix : this.matrixList) {
            matrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentText = "" + (TicTacToeScreen.turnCounter + 1);
                    TicTacToeScreen.turnText.setText("Turn: " + currentText);
                    TicTacToeScreen.turnCounter++;
                }
            });
        }
    }
}
