package com.appsfromholland.mqtt_example.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsfromholland.mqtt_example.R;
import com.appsfromholland.mqtt_example.gameLogics.Square;
import com.appsfromholland.mqtt_example.gameLogics.TicTacToeGame;
import com.appsfromholland.mqtt_example.mqttLogics.MQTTConfig;
import com.appsfromholland.mqtt_example.mqttLogics.PahoMqttClient;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.appsfromholland.mqtt_example.activities.WaitingForPlayersActivity.BROADCAST_ACTION;


/*
In deze klasse wordt de serverlogica verbonden met de gamelogica. Beide spelers staan klaar om via de app input te geven via de server.
Kruisje gaat altijd eerst. Er wordt gekeken naar Strings waarin alle informatie staat die de ander nodig heeft om het bord op zijn app op de juiste manier in te kleuren.
Een voorbeeld van deze String is: "r:6". In dit geval zal de code dit ontcijferen als kleur vakje nummer 6 rood (rood is in het spel kruisje).
 In de gamelogics klasse TicTacToeGame wordt bijgehouden wie aan zet is en of er iemand gewonnen heeft. Ook wordt er gekeken naar gelijkspel.
 In het geval dat de gamelogics aangeeft dat het spel moet worden afgesloten zal dat ook via de server aan beide spelers bekend worden gemaakt.
 Kortom, deze klasse dient als ontcijferaar van Strings vanuit de server. Daarnaast stuurt hij de juiste informatie op basis van deze Strings terug todat er een situatie onstaat waarin de connectie kan worden verbroken.
 Deze klasse sluit ook het spel en stuurt beide spelers terug naar het eerste scherm.
 */
public class TicTacToeScreenActivity extends AppCompatActivity {

    private static TextView turnText;
    private ArrayList<ImageView> matrixList;
    private int idNumber = 2131165279 + 131075;
    private int matrixNumber;
    private TicTacToeGame ticTacToeGame;
    private MyBroadcastReceiver myBroadCastReceiver;
    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient gameClient;
    private String player;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        player = this.getIntent().getStringExtra("player");

        Log.i("PLAYERTURN", player);


        launchMqtt(); //maakt connectie met nieuwe topic (zodat de wachtrij niet vervuild raakt met gamelogics)

        gameClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                try {
                    pahoMqttClient.subscribe(gameClient, "GameRoom", 0);
                    Thread.sleep(10);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override //Deze methode houdt bij wat er al ingevuld is op het bord en of het spel al beeindigd moet worden.
                      //Ook zet het op basis van feedback vanuit de gamelogics de juiste images op het spelbord in de app.
                      //Bij het eindigen stuurt deze methode de gebruiker terug naar de main activity.
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String payload = new String(mqttMessage.getPayload());
                Log.i("GAMEROOM", payload);
                if ((!payload.contains(MQTTConfig.getInstance().CLIENT_ID()))) {
                    String[] parts = payload.split(":", 2);
                    Log.i("PARTS",parts[1].toString());
                    int newMatrixNumber = Integer.parseInt(parts[0]);
                    newMatrixNumber = newMatrixNumber-1;
                    ticTacToeGame.checkGameStatus(newMatrixNumber);
                    if(parts[1].contains("r")){
                        matrixList.get(newMatrixNumber).setImageResource(R.drawable.kruiske);
                    }
                    else if(parts[1].contains("b")){
                        matrixList.get(newMatrixNumber).setImageResource(R.drawable.rondske);
                    }

                }
                if(!ticTacToeGame.isGameStatus()){
                    printWinner();

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                gameClient.publish("GameRoom",new MqttMessage("d".getBytes()));
                                Thread.sleep(10);
                                pahoMqttClient.disconnect(gameClient);
                                unregisterReceiver(myBroadCastReceiver);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
                            View view = new View(getApplicationContext());
                            Intent mainActivity = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(mainActivity);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask,3000);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        this.ticTacToeGame = new TicTacToeGame();
        this.turnText = findViewById(R.id.TurnText);
        turnText.setText("Speler " + ticTacToeGame.getPlayerTurn() + "'s beurt");

        this.matrixList = new ArrayList<>();
        this.matrixList.add((ImageView) findViewById(R.id.matrix0));
        this.matrixList.add((ImageView) findViewById(R.id.matrix1));
        this.matrixList.add((ImageView) findViewById(R.id.matrix2));
        this.matrixList.add((ImageView) findViewById(R.id.matrix3));
        this.matrixList.add((ImageView) findViewById(R.id.matrix4));
        this.matrixList.add((ImageView) findViewById(R.id.matrix5));
        this.matrixList.add((ImageView) findViewById(R.id.matrix6));
        this.matrixList.add((ImageView) findViewById(R.id.matrix7));
        this.matrixList.add((ImageView) findViewById(R.id.matrix8));


        //De loop hieronder houdt bij welke vakjes al gekozen zijn en kleurt deze alleen op de gebruiker van deze app in.
        //Nadat dit gedaan is wordt er via de server doorgestuurd dat er een keuze is gemaakt en wordt ook het bord van de tegenspeler geupdate met deze nieuwe informatie.
        for (final ImageView matrix : this.matrixList) {
            matrix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ticTacToeGame.isGameStatus()) {
                        String playerID;
                        if (ticTacToeGame.getPlayerCounter() == 1){
                            playerID = "r";
                        }else {
                            playerID = "b";
                        }
                        if (playerID.equals(player))
                        setMatrixNumber(matrix.getId() - idNumber);
                        for (Square square : ticTacToeGame.getPlayField().getSquares()) {
                            if (!square.isClicked() && square.getPosition() == matrixNumber) {
                                if (player.equals("r")) {
                                    matrix.setImageResource(R.drawable.kruiske);
                                } else {
                                    matrix.setImageResource(R.drawable.rondske);
                                }
                                ticTacToeGame.checkGameStatus(matrixNumber);
                                MqttMessage mqttMessage = new MqttMessage(((matrixNumber + 1) + ":" + player + MQTTConfig.getInstance().CLIENT_ID()).getBytes());
                                try {
                                    gameClient.publish("GameRoom", mqttMessage);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                                TicTacToeScreenActivity.turnText.setText("Speler " + ticTacToeGame.getPlayerTurn() + "'s beurt");
                            }
                        }

                    }
                }
            });
        }
    }

    public void setMatrixNumber(int number) {
        this.matrixNumber = number;
    }

    public void printWinner() throws MqttException { //Laat de spelers zien wat de eindstand is op de app. De server wordt geupdate en dus zullen beide spelers dit gelijk zien.
        String playerID;
        if(ticTacToeGame.getPlayerCounter() == 2){
            playerID = "Kruisje wint!";
            gameClient.publish("GameRoom",new MqttMessage("z".getBytes()));
        }
        else if(ticTacToeGame.getPlayerCounter() == 1){
            playerID = "Rondje wint!";
            gameClient.publish("GameRoom",new MqttMessage("y".getBytes()));
        }
        else{
            playerID = "Niemand wint!";
            gameClient.publish("GameRoom",new MqttMessage("x".getBytes()));
        }
        this.turnText.setText(playerID);


    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        private final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    public void launchMqtt() {
        pahoMqttClient = new PahoMqttClient();

        this.gameClient = pahoMqttClient.getMqttClient(
                getApplicationContext(),
                MQTTConfig.getInstance().MQTT_BROKER_URL(),
                MQTTConfig.getInstance().CLIENT_ID());

        // Setup Broadcast receiver
        myBroadCastReceiver = new MyBroadcastReceiver();

        // Start Broadcast receiver
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            registerReceiver(myBroadCastReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
