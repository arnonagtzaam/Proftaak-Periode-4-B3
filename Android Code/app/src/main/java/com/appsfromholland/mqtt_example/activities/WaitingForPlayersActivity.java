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
import android.widget.TextView;

import com.appsfromholland.mqtt_example.R;
import com.appsfromholland.mqtt_example.mqttLogics.MQTTConfig;
import com.appsfromholland.mqtt_example.mqttLogics.PahoMqttClient;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


/*
In deze klasse wordt voor de speler alvast een connectie gemaakt met een mqttserver. Wanneer dit voltooid is zal een "Connectie" String worden doorgestuurd naar de server.
Als er al iemand in de server zit zal diegene via de MessageArrived methode dit ontvangen en een "Start" String terugsturen. Zo weten beide spelers dat ze de Game Activity kunnen launchen.
Zit er nog niemand in de server dan zal er niks gebeuren tot er een "Connectie" String binnenkomt van een toekomstige speler. Bovenop de Strings plaatsen wij altijd een persoonlijk ID zodat de code niet reageert op eigen verzonden messages.

 */

public class WaitingForPlayersActivity extends AppCompatActivity {
    private TextView connectingTextView;
    private boolean twoPlayersConnected;
    private static Context context;
    private MyBroadcastReceiver myBroadCastReceiver;
    private MqttAndroidClient client;
    static final String BROADCAST_ACTION = "com.appsfromholland.mqttpayloadavailabex";

    public static int connectedPlayers;

    private PahoMqttClient pahoMqttClient;
    private MqttAndroidClient mqttAndroidClient;
    private static final String TAG = "TI14-MQTT";
    private String lastSendString = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_player);
        this.connectingTextView = findViewById(R.id.connectionTextView);
        this.connectingTextView.setText(R.string.connectionTextView);
        this.context = this;
        this.connectedPlayers = 0;
        this.twoPlayersConnected = false;


        launchMqtt(); //maakt verbinding aan en slaat nodige onderdelen op als attributen

        this.mqttAndroidClient = pahoMqttClient.getMqttClient(
                getApplicationContext(),
                MQTTConfig.getInstance().MQTT_BROKER_URL(),
                MQTTConfig.getInstance().CLIENT_ID());
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

                try {
                    pahoMqttClient.subscribe(mqttAndroidClient, MQTTConfig.getInstance().PUBLISH_TOPIC(), 0);
                    Thread.sleep(10);
                    MqttMessage mqttMessage = new MqttMessage((("Connected"+MQTTConfig.getInstance().CLIENT_ID()).getBytes()));
                    mqttAndroidClient.publish(MQTTConfig.getInstance().PUBLISH_TOPIC(),mqttMessage);
                    lastSendString = "Connected" + MQTTConfig.getInstance().CLIENT_ID();
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.i(TAG, "connectionLost()");
            }

            @Override //in de onderstaande methode wordt gekeken of de binnenkomende message een keyword heeft wat wij gebruiken voor de logica.
                      //als dat zo is wordt er of een message teruggestuurd voor de andere speler. In geval 2 wordt er een nieuwe activity gelaunched namelijk: TicTacToeActivity.
            public void messageArrived(String s, MqttMessage mqttMessage) throws MqttException, UnsupportedEncodingException {
                String payload = new String(mqttMessage.getPayload());
                if(payload.contains("Connected") && !(payload.equals("Connected"+MQTTConfig.getInstance().CLIENT_ID()))){
                    MqttMessage mqttMessage1 = new MqttMessage(("Start" + MQTTConfig.getInstance().CLIENT_ID()).getBytes());
                    mqttAndroidClient.publish(MQTTConfig.getInstance().PUBLISH_TOPIC(),mqttMessage1);

                    //LAUNCH
                    Log.i("LAUNCH","GAME HAS LAUNCHED SUCCESFULLY");
                    launchGameActivity("r");
                }
                if(payload.contains("Start") && !(payload.equals("Start"+MQTTConfig.getInstance().CLIENT_ID()))){
                    //LAUNCH
                    Log.i("LAUNCH","GAME HAS LAUNCHED SUCCESFULLY");
                    launchGameActivity("b");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.i(TAG, "deliveryComplete()");
            }
        });

    }

    //zijn er twee spelers geconnect en is alles goedgekeurd binnen de server dan wordt met deze methode de laatste activity gelaunched.
    public void launchGameActivity(String message) throws MqttException {
        pahoMqttClient.disconnect(mqttAndroidClient);
        Intent intent = new Intent(getApplicationContext(),TicTacToeScreenActivity.class);
        intent.putExtra("player", message);
        startActivity(intent);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        private final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    /**
     * This method called when this Activity finished
     * Override this method to unregister MyBroadCastReceiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // make sure to unregister your receiver after finishing of this activity
        unregisterReceiver(myBroadCastReceiver);
    }


    //opzet eerste mqttclient
    public void launchMqtt() {
        pahoMqttClient = new PahoMqttClient();

        client = pahoMqttClient.getMqttClient(
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

    public PahoMqttClient getPahoMqttClient(){
        return this.pahoMqttClient;
    }

    public Context getContext(){
        return context;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}




