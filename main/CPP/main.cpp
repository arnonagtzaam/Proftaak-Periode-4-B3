#include <Arduino.h>
#include<WiFi.h>
#include<PubSubClient.h>
const char* ssid = "WorkAids";
const char* pass = "7605?A3n";
const char* brokerUser = "emon";
const char* brokerPass = "uw2ELjAKrEUwqgLT";
const char* broker = "51.254.217.43";
const char* outTopic = "NEOPIXEL";
const char* inTopic = "IN";

WiFiClient espClient;
PubSubClient client(espClient);
long currentTime,lastTime;
int count = 0;
char messages[50];

void setupWifi(){
  delay(100);
  Serial.print("\nConnecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid,pass);
  
  while(WiFi.status()!= WL_CONNECTED){
    delay(100);
    Serial.print("-");
  }
  Serial.print("\nConnected to ");
  Serial.println(ssid);
  }
  
void reconnect(){
  while(!client.connected()){
    Serial.print("\nConnected to ");
    Serial.println(broker);
    if(client.connect("test123",brokerUser,brokerPass)){
      Serial.print("\nConnected to ");
      Serial.print(broker);
      client.subscribe(inTopic);
    }else{
        Serial.println("\nTrying connection");
        delay(5000);
      }
    }
  }
  void callback(char* topic, byte* payload, unsigned int lenght){
    Serial.print("Received messages; ");
    Serial.println(topic);
    for(int i=0; i<lenght; i++){
      Serial.print((char) payload[i]);
    }
    Serial.println();
  }

void setup() {
  // put your setup code here, to run once:
Serial.begin(115200);
setupWifi();
client.setServer(broker,1883);
client.setCallback(callback);
}

void loop() {
  digitalWrite(25,LOW);
  // put your main code here, to run repeatedly:
if(!client.connected()){
reconnect();
}
client.loop();
currentTime = millis();
if(currentTime - lastTime > 2000){
  count++;
  snprintf(messages,75,"Count: %ld",count);
  Serial.print("Sending messages: ");
  Serial.println(messages);
  client.publish(outTopic,messages);
  lastTime = millis();
}
}