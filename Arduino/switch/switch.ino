#include <SoftwareSerial.h>
#include <MsTimer2.h>

SoftwareSerial BTSerial(4, 5);

int swPin = 2;
int cmPin = 3;
int faPin = 8;

int Trig = 6;
int Echo = 7;

bool state = false;
bool cm_state = false;
bool fa_state = false;

long duration, cm;

void flash()
{
  if(digitalRead(faPin) == HIGH) {
    if(fa_state == false) {
      BTSerial.write("fon\r");
      Serial.println("fon");
      fa_state = true;
      BTSerial.flush();
      delay(50000);
    }
    else {
      fa_state = false;
      BTSerial.write("foff\r");
      Serial.println("foff");
      BTSerial.flush();
      delay(50000);
    }
  }
}

void setup()
{
  Serial.begin(9600);
  pinMode(swPin, INPUT);
  pinMode(cmPin, INPUT);
  pinMode(faPin, INPUT);
  
  pinMode(Trig,OUTPUT); // 센서 Trig 핀
  pinMode(Echo,INPUT); // 센서 Echo 핀
  
  Serial.println("Hello!");
  // set the data rate for the BT port
  BTSerial.begin(115200);
  
  attachInterrupt(0, swInterrupt, RISING);
  attachInterrupt(1, swInterrupt1, RISING);
  MsTimer2::set(10, flash); // 500ms period
  MsTimer2::start();
}
 
void loop()
{
  digitalWrite(Trig,HIGH); // 센서에 Trig 신호 입력
  delayMicroseconds(10); // 10us 정도 유지
  digitalWrite(Trig,LOW); // Trig 신호 off

  duration = pulseIn(Echo,HIGH); // Echo pin: HIGH->Low 간격을 측정
  cm = microsecondsToCentimeters(duration); // 거리(cm)로 변환
  //delay(100);
  //Serial.println(digitalRead(swPin));
  Serial.print(cm);
  Serial.println();
  if(cm_state == true) {
    delay(100);
    if(cm < 140 && cm > 70) {
      BTSerial.write("d\r");
      //BTSerial.print("cm");
      //Serial.println();
    }
  }

  delay(300);
}

long microsecondsToInches(long microseconds)
{
  return microseconds / 74 / 2;
}

long microsecondsToCentimeters(long microseconds)
{
  return microseconds / 29 / 2;
}

void swInterrupt() {
  
  //Serial.println("1");
  
  if(digitalRead(swPin) == HIGH) {
    if(state == false) {
      BTSerial.write("bon\r");
      Serial.println("bon");
      state = true;
      BTSerial.flush();
      delay(50000);
    }
    else {
      state = false;
      BTSerial.write("boff\r");
      Serial.println("boff");
      BTSerial.flush();
      delay(50000);
    }
  }
}
void swInterrupt1() {
  
  //Serial.println("1");
  
  if(digitalRead(cmPin) == HIGH) {
    if(cm_state == false) {
      BTSerial.write("con\r");
      Serial.println("con");
      cm_state = true;
      BTSerial.flush();
      delay(50000);
    }
    else {
      cm_state = false;
      BTSerial.write("coff\r");
      Serial.println("coff");
      BTSerial.flush();
      delay(50000);
    }
  }
}

