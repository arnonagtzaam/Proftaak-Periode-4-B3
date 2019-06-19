#include <Arduino.h>
#include<WiFi.h>
#include<PubSubClient.h>
#include <Adafruit_NeoPixel.h>
#include <millisDelay.h>

//Attributen

#define PIN1    25
#define PIN2    13
#define PIN3    12
#define PIN4    27
#define PIN5    33
#define PIN6    15
#define PIN7    32
#define PIN8    14
#define motionPin 21
#define N_LEDS 8
millisDelay timer;
int lastMillis = millis();
boolean standby = false;

const char* ssid = "Ziggo3729361";
const char* pass = "ccLrvn3Q7hxp";
const char* brokerUser = "emon";
const char* brokerPass = "uw2ELjAKrEUwqgLT";
const char* broker = "51.254.217.43";
const char* outTopic = "GameRoom";
const char* inTopic = "GameRoom";

Adafruit_NeoPixel strip1 = Adafruit_NeoPixel(N_LEDS, PIN1, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip2 = Adafruit_NeoPixel(N_LEDS, PIN2, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip3 = Adafruit_NeoPixel(N_LEDS, PIN3, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip4 = Adafruit_NeoPixel(N_LEDS, PIN4, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip5 = Adafruit_NeoPixel(N_LEDS, PIN5, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip6 = Adafruit_NeoPixel(N_LEDS, PIN6, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip7 = Adafruit_NeoPixel(N_LEDS, PIN7, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel strip8 = Adafruit_NeoPixel(N_LEDS, PIN8, NEO_GRB + NEO_KHZ800);

WiFiClient espClient;
PubSubClient client(espClient);
long currentTime,lastTime;
int count = 0;
char messages[50];
char receivedPayload[100];


bool oneLit = false;
bool twoLit = false;
bool threeLit = false;
bool fourLit = false;
bool fiveLit = false;
bool sixLit = false;
bool sevenLit = false;
bool eightLit = false;
bool nineLit = false;

char selectedColor[9];

bool boardCleared = true;


//Setup wifi

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


//Reconnect als de verbinding verbroken wordt
  
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

//Callback voor het ontvangen van berichten

void callback(char* topic, byte* payload, unsigned int lenght){
    for(int i=0; i<lenght; i++){
      receivedPayload[i] = (char)payload[i];
    }
}

//Setup van de MQTT verbinding en de neopixel strips

void setup() {
  // put your setup code here, to run once:
Serial.begin(115200);
setupWifi();
client.setServer(broker,1883);
client.setCallback(callback);

  strip1.begin();
  strip2.begin();
  strip3.begin();
  strip4.begin();
  strip5.begin();
  strip6.begin();
  strip7.begin();
  strip8.begin();
  timer.start(500);
  pinMode(motionPin, INPUT);

}

//Main loop waar alles in gebeurt

// x = gelijk
// y = geel
// z = rood

void loop() {
  // put your main code here, to run repeatedly:
 client.loop(); 
if(!client.connected()){
reconnect();
}

bool isDetected = digitalRead(motionPin);

if(receivedPayload[0] == '1'){
  oneLit = true;
  boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[0] = 'r';
  } else {
    selectedColor[0] = 'b';
  }
} else if(receivedPayload[0] == '2'){
  twoLit = true;
    boardCleared = false;

  if(receivedPayload[2] == 'r'){
    selectedColor[1] = 'r';
  } else {
    selectedColor[1] = 'b';
  } 
}else if(receivedPayload[0] == '3'){
  threeLit = true;
  boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[2] = 'r';
  } else {
    selectedColor[2] = 'b';
  } 
}else if(receivedPayload[0] == '4'){
  fourLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[3] = 'r';
  } else {
    selectedColor[3] = 'b';
  } 
}else if(receivedPayload[0] == '5'){
  fiveLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[4] = 'r';
  } else {
    selectedColor[4] = 'b';
  } 
}else if(receivedPayload[0] == '6'){
  sixLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[5] = 'r';
  } else {
    selectedColor[5] = 'b';
  } 
}else if(receivedPayload[0] == '7'){
  sevenLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[6] = 'r';
  } else {
    selectedColor[6] = 'b';
  } 
}else if(receivedPayload[0] == '8'){
  eightLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[7] = 'r';
  } else {
    selectedColor[7] = 'b';
  } 
}else if(receivedPayload[0] == '9'){
  nineLit = true;
    boardCleared = false;
  if(receivedPayload[2] == 'r'){
    selectedColor[8] = 'r';
  } else {
    selectedColor[8] = 'b';
}
}

if(oneLit){
  if(selectedColor[0] == 'r'){
    fillGrid(0,10,0,0);
  } else if(selectedColor[0] == 'b'){
    fillGrid(0,10,7,0);
  }
} 
if(twoLit){
  if(selectedColor[1] == 'r'){
    fillGrid(1,10,0,0);
  } else if(selectedColor[1] == 'b'){
    fillGrid(1,10,7,0);
  }
} 
if(threeLit){
  if(selectedColor[2] == 'r'){
    fillGrid(2,10,0,0);
  } else if(selectedColor[2] == 'b'){
    fillGrid(2,10,7,0);
  }
} 
if(fourLit){
  if(selectedColor[3] == 'r'){
    fillGrid(3,10,0,0);
  } else if(selectedColor[3] == 'b'){
    fillGrid(3,10,7,0);
  }
} 
if(fiveLit){
  if(selectedColor[4] == 'r'){
    fillGrid(4,10,0,0);
  } else if(selectedColor[4] == 'b'){
    fillGrid(4,10,7,0);
  }
} 
if(sixLit){
  if(selectedColor[5] == 'r'){
    fillGrid(5,10,0,0);
  } else if(selectedColor[5] == 'b'){
    fillGrid(5,10,7,0);
  }
} 
if(sevenLit){
  if(selectedColor[6] == 'r'){
    fillGrid(6,10,0,0);
  } else if(selectedColor[6] == 'b'){
    fillGrid(6,10,7,0);
  }
} 
if(eightLit){
  if(selectedColor[7] == 'r'){
    fillGrid(7,10,0,0);
  } else if(selectedColor[7] == 'b'){
    fillGrid(7,10,7,0);
  }
} 
if(nineLit){
  if(selectedColor[8] == 'r'){
    fillGrid(8,10,0,0);
  } else if(selectedColor[8] == 'b'){
    fillGrid(8,10,7,0);
  }
}

if(receivedPayload[0] == 'x'){
  bool oneLit = false;
  bool twoLit = false;
  bool threeLit = false;
  bool fourLit = false;
  bool fiveLit = false;
  bool sixLit = false;
  bool sevenLit = false;
  bool eightLit = false;
  bool nineLit = false;
  createBoard(0,0,10);
  for(int p = 0; p < 9; p++){
    fillGrid(p,0,0,10);
  }
}

  if(receivedPayload[0] == 'y'){
  bool oneLit = false;
  bool twoLit = false;
  bool threeLit = false;
  bool fourLit = false;
  bool fiveLit = false;
  bool sixLit = false;
  bool sevenLit = false;
  bool eightLit = false;
  bool nineLit = false;
  createBoard(10,7,0);
  for(int p = 0; p < 9; p++){
    fillGrid(p,10,7,0);
  }
  }

  if(receivedPayload[0] == 'z'){
  bool oneLit = false;
  bool twoLit = false;
  bool threeLit = false;
  bool fourLit = false;
  bool fiveLit = false;
  bool sixLit = false;
  bool sevenLit = false;
  bool eightLit = false;
  bool nineLit = false;
  createBoard(10,0,0);
  for(int p = 0; p < 9; p++){
    fillGrid(p,10,0,0);
  }

}

if(receivedPayload[0] == 'd'){
bool oneLit = false;
bool twoLit = false;
bool threeLit = false;
bool fourLit = false;
bool fiveLit = false;
bool sixLit = false;
bool sevenLit = false;
bool eightLit = false;
bool nineLit = false;

boardCleared = true;

memset(selectedColor, 0x00 , sizeof(selectedColor));
}

if(boardCleared){
clearBoard();
}
  
  int r = 10;
  int g = 10;
  int b = 10;

  createBoard(r, g, b);
 
  if (((millis() - lastMillis) >= 100000 )&& !isDetected) {
    standby = true;
    while(standby){
      clearBoard();
      isDetected = digitalRead(motionPin);
      if(isDetected){
        standby = false;
      }
      delay(200);
    }
    lastMillis = millis();
  }
  delay(200);
}

//Vult een gat in het bord

static void createBoard(int r, int g, int b) {
  for (int k = 0; k < 8; k++) {
    strip3.setPixelColor(k, strip3.Color(r, g, b));
    strip6.setPixelColor(k, strip6.Color(r, g, b));
  }

  strip1.setPixelColor(2, strip1.Color(r, g, b));
  strip1.setPixelColor(5, strip1.Color(r, g, b));

  strip2.setPixelColor(2, strip2.Color(r, g, b));
  strip2.setPixelColor(5, strip2.Color(r, g, b));

  strip4.setPixelColor(2, strip4.Color(r, g, b));
  strip4.setPixelColor(5, strip4.Color(r, g, b));

  strip5.setPixelColor(2, strip5.Color(r, g, b));
  strip5.setPixelColor(5, strip5.Color(r, g, b));

  strip7.setPixelColor(2, strip7.Color(r, g, b));
  strip7.setPixelColor(5, strip7.Color(r, g, b));

  strip8.setPixelColor(2, strip8.Color(r, g, b));
  strip8.setPixelColor(5, strip8.Color(r, g, b));


  strip1.show();
  strip2.show();
  strip3.show();
  strip4.show();
  strip5.show();
  strip6.show();
  strip7.show();
  strip8.show();
}


//Vult de lijnen van het 3x3 scherm

static void fillGrid(int gridIndex, int r, int g, int b) {
  switch (gridIndex) {
    case 0:
      strip1.setPixelColor(0, strip1.Color(r, g, b));
      strip1.setPixelColor(1, strip1.Color(r, g, b));
      strip2.setPixelColor(0, strip2.Color(r, g, b));
      strip2.setPixelColor(1, strip2.Color(r, g, b));
      strip1.show();
      strip2.show();
      break;
    case 1:
      strip4.setPixelColor(0, strip4.Color(r, g, b));
      strip4.setPixelColor(1, strip4.Color(r, g, b));
      strip5.setPixelColor(0, strip5.Color(r, g, b));
      strip5.setPixelColor(1, strip5.Color(r, g, b));
      strip4.show();
      strip5.show();
      break;
    case 2:
      strip7.setPixelColor(0, strip7.Color(r, g, b));
      strip7.setPixelColor(1, strip7.Color(r, g, b));
      strip8.setPixelColor(0, strip8.Color(r, g, b));
      strip8.setPixelColor(1, strip8.Color(r, g, b));
      strip7.show();
      strip8.show();
      break;
    case 3:
      strip1.setPixelColor(3, strip1.Color(r, g, b));
      strip1.setPixelColor(4, strip1.Color(r, g, b));
      strip2.setPixelColor(3, strip2.Color(r, g, b));
      strip2.setPixelColor(4, strip2.Color(r, g, b));
      strip1.show();
      strip2.show();
      break;
    case 4:
      strip4.setPixelColor(3, strip4.Color(r, g, b));
      strip4.setPixelColor(4, strip4.Color(r, g, b));
      strip5.setPixelColor(3, strip5.Color(r, g, b));
      strip5.setPixelColor(4, strip5.Color(r, g, b));
      strip4.show();
      strip5.show();
      break;
    case 5:
      strip7.setPixelColor(3, strip7.Color(r, g, b));
      strip7.setPixelColor(4, strip7.Color(r, g, b));
      strip8.setPixelColor(3, strip8.Color(r, g, b));
      strip8.setPixelColor(4, strip8.Color(r, g, b));
      strip7.show();
      strip8.show();
      break;
    case 6:
      strip1.setPixelColor(6, strip1.Color(r, g, b));
      strip1.setPixelColor(7, strip1.Color(r, g, b));
      strip2.setPixelColor(6, strip2.Color(r, g, b));
      strip2.setPixelColor(7, strip2.Color(r, g, b));
      strip1.show();
      strip2.show();
      break;
    case 7:
      strip4.setPixelColor(6, strip4.Color(r, g, b));
      strip4.setPixelColor(7, strip4.Color(r, g, b));
      strip5.setPixelColor(6, strip5.Color(r, g, b));
      strip5.setPixelColor(7, strip5.Color(r, g, b));
      strip4.show();
      strip5.show();
      break;
    case 8:
      strip7.setPixelColor(6, strip7.Color(r, g, b));
      strip7.setPixelColor(7, strip7.Color(r, g, b));
      strip8.setPixelColor(6, strip8.Color(r, g, b));
      strip8.setPixelColor(7, strip8.Color(r, g, b));
      strip7.show();
      strip8.show();
      break;
  }
}



static void clearBoard(){
   strip1.clear();
   strip1.show();
   strip2.clear();
   strip2.show();
   strip3.clear();
   strip3.show();
   strip4.clear();
   strip4.show();
   strip5.clear();
   strip5.show();
   strip6.clear();
   strip6.show();
   strip7.clear();
   strip7.show();
   strip8.clear();
   strip8.show();
}
