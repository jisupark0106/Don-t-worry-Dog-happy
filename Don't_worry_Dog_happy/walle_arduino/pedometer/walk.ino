/*
  GY-521 ���̷�&���ӵ� ���� (MPU-6050)
  �����Դϴ�.

  UNO R3 : http://kit128.com/goods/view?no=337
  GY-521 ���̷�&���ӵ� ���� (MPU-6050) ��� : http://kit128.com/goods/view?no=146

  ��ó: http://whiteat.com/Arduino

*/
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include<Wire.h>

const int MPU = 0x68;  // I2C address of the MPU-6050
int16_t AcX, AcY, AcZ, Tmp, GyX, GyY, GyZ;
int prev_val;
unsigned long count = 0;
unsigned long last_millis;

// ���α׷� ���� - �ʱ�ȭ �۾�
void setup()
{
  Serial.begin(115200);     // �ø��� ��� �ʱ�ȭ
  Serial.println("Arduino Examples - GY-521 Gyro& Accelator (MPU-6050) Module");
  Serial.println("    http://docs.whiteat.com/?p=2662");

  Wire.begin();
  Wire.beginTransmission(MPU);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);

  // gyro scale
  Wire.beginTransmission(MPU);
  Wire.write(0x1B);  //
  Wire.write(0xF8);     //
  Wire.endTransmission(true);

  // acc scale
  Wire.beginTransmission(MPU);
  Wire.write(0x1C);  //
  Wire.write(0xF8);     //
  Wire.endTransmission(true);

}


void loop()
{
  Wire.beginTransmission(MPU);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU, 14, true);  // request a total of 14 registers
  AcX = Wire.read() << 8 | Wire.read();  // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
  AcY = Wire.read() << 8 | Wire.read();  // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
  AcZ = Wire.read() << 8 | Wire.read();  // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  Tmp = Wire.read() << 8 | Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  GyX = Wire.read() << 8 | Wire.read();  // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
  GyY = Wire.read() << 8 | Wire.read();  // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
  GyZ = Wire.read() << 8 | Wire.read();  // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)


  int val = abs(AcX) + abs(AcY) + abs(AcZ);

  if (abs(prev_val - val) > 700)
  {

    //  Serial.println("in");
    if ( ( millis() - last_millis ) > 250 ) // 0.3�� �̸� �������� ������ �߰��Ǵ� ���� ���� ������ �ƴ϶� ������ ��鸲 �� �������� ���ɼ��� ����.
    {
      count++;
      last_millis = millis();
      if (count % 2 == 0)
        Serial.println(count / 2);

    }
  }
  prev_val = val;
  if (millis() % 15000 == 0)
    wifisend();


}
void wifisend() {
  WiFi.begin("it3", "12345678");
  while (WiFi.status() != WL_CONNECTED)

    delay(100);
  Serial.println("Waiting for connection");

   
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status

    byte* bssid = WiFi.BSSID();

    char mac[18] = {0};
    sprintf(mac, "%02x:%02x:%02x:%02x:%02x:%02x", bssid[0],  bssid[1],  bssid[2], bssid[3], bssid[4], bssid[5]);
    
    HTTPClient http;
    http.begin("http://walle2018.iptime.org:3392/momentum");
    http.addHeader("Content-Type", "application/json");
    String jsonString = "{\"dstep\":\"";
    jsonString += count / 2;
    jsonString += "\",\"madd\":\"";
    jsonString += String(mac);
    jsonString += "\"}";

    int httpCode = http.POST(jsonString);
    Serial.print("http result:");
    Serial.println(httpCode);
    http.writeToStream(&Serial);
    String payload = http.getString();
    http.end();
    count = 0;

  } else {
    Serial.print("Error in Wifi connection");

  }
}
