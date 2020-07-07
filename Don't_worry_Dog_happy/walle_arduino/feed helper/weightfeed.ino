
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <SoftwareSerial.h>

#include "HX711.h" //HX711로드셀 엠프 관련함수 호출
#define calibration_factor 7050.0 // 로드셀 스케일 값 선언
#define DOUT  16 //엠프 데이터 아웃 핀 넘버 선언
#define CLK  5  //엠프 클락 핀 넘버 
HX711 scale(DOUT, CLK); //엠프 핀 선언 

//SoftwareSerial mySerial(2,3);
#define D6 12 // SPI Bus MISO for rx 
#define D5 14 // SPI Bus MOSI for tx

SoftwareSerial mySerial(D5, D6);

int prev_val;
unsigned long count = 50;
unsigned long last_millis;
int bowl=0;
int bowl_factor=0;
int total_factor=0;
// 프로그램 시작 - 초기화 작업
void setup()
{
   WiFi.begin("it3", "12345678"); 
  if (WiFi.status() == WL_CONNECTED) { 
     Serial.println("connect!!!\n");
  }
  delay(100);
  Serial.println("Waiting for connection");
  Serial.begin(9600);     // 시리얼 통신 초기화
  Serial.println("Arduino Examples - GY-521 Gyro& Accelator (MPU-6050) Module");
  Serial.println("    http://docs.whiteat.com/?p=2662");


  Serial.println("HX711 scale TEST");  
  scale.set_scale();  //스케일 지정 
  scale.tare();  //스케일 설정
  long zero_factor = scale.read_average(); //Get a baseline reading
  Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.println(zero_factor);
  
  mySerial.begin(9600);
  
}


void loop()
{  
 
   if(millis()%15000==0){
      //mySerial.print("aaa");
      scale.set_scale(calibration_factor);
      //delay(4000);  
      bowl=scale.get_units()/22*453.596+2,0;
      Serial.print("Reading: ");
      Serial.print(bowl);  //무제 출력 
      Serial.print(" g"); //단위
      Serial.println();   
      wifisend();
   }
 
}
void wifisend(){
  
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status

    HTTPClient http;
    http.begin("http://walle2018.iptime.org:3392/feed");
   
    int httpCode = http.GET();
    if (httpCode > 0) {
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);

      // file found at server
      if (httpCode == HTTP_CODE_OK) {
        //String payload = http.writeToStream(&Serial);
        String payload = http.getString();
        
        Serial.print("haahahah");
        Serial.print(payload);

        char cTempData[5];
        payload.substring(1, 4).toCharArray(cTempData, 5);

        Serial.print(cTempData);
        int read_factor = atoi(cTempData);
        
        bowl_factor=read_factor- bowl;
        Serial.print("readf:");
        Serial.print(read_factor);
        Serial.print("  bowlf:");
        Serial.print(bowl_factor);
        Serial.print("  bowl:");
        Serial.println(bowl);
        if(bowl_factor<=0) 
          total_factor=0;
         else
          total_factor=bowl_factor;
     
        Serial.println(total_factor);
        mySerial.print(total_factor);
      }
    } else {
      Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }
 
  else {
    Serial.print("Error in Wifi connection"); 
  }
}

