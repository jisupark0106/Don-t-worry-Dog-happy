#define RELAY 2
#define BUTTON 3


int pState = LOW;
int response=0;

void setup() {
  pinMode(RELAY, OUTPUT);
  pinMode(BUTTON, INPUT);
  digitalWrite(RELAY, HIGH);
  Serial.begin(9600);
}

void loop() {
 // int state = digitalRead(BUTTON);


  if (Serial.available()) {
    String a = Serial.readString();
    
    response = a.toInt();
    Serial.println(a);
    Serial.println(response);
    for(int i=0;i<response;i++){
     //if (Serial.read()=='a') 
       feed();
       delay(100);
    }
      
  }
}

void feed() {
  digitalWrite(RELAY, LOW);
  delay(100);
  digitalWrite(RELAY, HIGH);

}
