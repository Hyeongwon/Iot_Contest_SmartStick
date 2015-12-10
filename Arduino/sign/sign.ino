#include <SoftwareSerial.h>
//#include <MsTimer2.h>

SoftwareSerial BTSerial(2, 3);
const int led_pin = 13;
char a[20] = "AT+ADVDATA=";

int num[10][7]{
  {0,0,0,0,0,0,1}, //0
  {1,0,0,1,1,1,1}, //1
  {0,0,1,0,0,1,0}, //2
  {0,0,0,0,1,1,0}, //3
  {1,0,0,1,1,0,0},//4
  {0,1,0,0,1,0,0},//5
  {1,1,0,0,0,0,0},//6
  {0,0,0,1,1,0,1},//7
  {0,0,0,0,0,0,0},//8
  {0,0,0,1,1,0,0}//9
};

/*
void flash()
{
  char flag = BTSerial.read();
  Serial.write(flag);
  static boolean output = HIGH;
  //if(flag == '1' || flag == '2' || flag == '3')
    //digitalWrite(led_pin, output);
}*/

void setup() {
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(led_pin, OUTPUT);
  //MsTimer2::set(200, flash); // 500ms period
  //MsTimer2::start();
  Serial.begin(9600);
  BTSerial.begin(9600);

  pinMode(4,OUTPUT);
  pinMode(5,OUTPUT);
  pinMode(6,OUTPUT);
  pinMode(7,OUTPUT);
  pinMode(8,OUTPUT);
  pinMode(9,OUTPUT);
  pinMode(10,OUTPUT);
}
int num1 = 10;
int cout = 0;
void loop() {
    digitalWrite(4, HIGH);
    digitalWrite(5, HIGH);
    digitalWrite(6, HIGH);
    digitalWrite(7, HIGH);
    digitalWrite(8, HIGH);
    digitalWrite(9, HIGH);
    
    digitalWrite(11,LOW);
    digitalWrite(12, 255);
    BTSerial.write((char*)"AT+ADVDATA=1\n");
    BTSerial.write((char*)"AT+VER?\n");
    
    //Serial.write(BTSerial.read());
    delay(6000);
   // BTSerial.write((char*)"AT+ADVDATA=2\n");
    for(int i = 0; i < 50; i++) {
      digitalWrite(12, LOW);
      delay(100);
      digitalWrite(12, HIGH);
      delay(100);
      a[11] = i + '4';
      a[12] = '\n';
      Serial.write(a);
      BTSerial.write(a);
      cout = 0;

      if(i == 0 || i == 5 || i == 10 || i == 15 || i == 20 || i == 25 || i == 30 || i == 35 || i == 40 || i == 45 ) {
        num1--;
        for(int j = 4; j < 11; j++) {
          digitalWrite(j,num[num1][cout]);
          cout++;
        }
        //0,0,1,0,0,1,0
        //1,0,0,1,1,1,1
   
      }
      //if(num1 == 10) num1=0;
     }
    num1 = 10;
    digitalWrite(10, LOW);
    digitalWrite(4, HIGH);
    digitalWrite(5, HIGH);
    digitalWrite(6, HIGH);
    digitalWrite(7, HIGH);
    digitalWrite(8, HIGH);
    digitalWrite(9, HIGH);
    
    BTSerial.write((char*)"AT+ADVDATA=3\n");
    digitalWrite(12, LOW);
    digitalWrite(11, HIGH);
    delay(6000);

    
}

