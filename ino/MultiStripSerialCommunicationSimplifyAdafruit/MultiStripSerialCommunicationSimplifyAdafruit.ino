#include <Firmata.h>
#include <Adafruit_NeoPixel.h>
#include <Arduino.h>
#include <HardwareSerial.h>
#include <HardwareSerial.cpp>
#include <EEPROM.h>


#define NUM_LEDS 300
#define NUM_STRIPS 14


int stripPin;
int brightness;
int red;
int green;
int blue;

CRGB leds[5][NUM_LEDS];

void setup()
{
    Serial.begin(115200);
    FastLED.addLeds<NEOPIXEL, 0>(leds[0], NUM_LEDS);
    FastLED.addLeds<NEOPIXEL, 1>(leds[1], NUM_LEDS);
    FastLED.addLeds<NEOPIXEL, 2>(leds[2], NUM_LEDS);
    FastLED.addLeds<NEOPIXEL, 3>(leds[3], NUM_LEDS);
    FastLED.addLeds<NEOPIXEL, 4>(leds[4], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 5>(leds[5], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 6>(leds[6], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 7>(leds[7], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 8>(leds[8], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 9>(leds[9], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 10>(leds[10], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 11>(leds[11], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 12>(leds[12], NUM_LEDS);
//    FastLED.addLeds<NEOPIXEL, 13>(leds[13], NUM_LEDS);
    FastLED.clear(true);
    
    FastLED.setBrightness(100);
    FastLED.show();
}

void loop()
{
  //Change # of pin code 200
  if(Serial.available() >= 5){
    stripPin = Serial.read();
    brightness = Serial.read();
    red = Serial.read();
    green = Serial.read();
    blue = Serial.read();

    printDebug();

    FastLED.setBrightness(brightness);
    for(int j = 0; j<NUM_LEDS; j++){
      leds[stripPin][j].setRGB(red,green,blue);
    }
    FastLED.show();
  }
}

void printDebug(){
    Serial.write(stripPin);
    Serial.write(brightness);
    Serial.write(red);
    Serial.write(green);
    Serial.write(blue);
}

//void waitForNumInput(){
//  while(!Serial.available()){} //Wait for serial
//  byte input = Serial.read();
//  if(input > 0 || input < 15){ //If input is within range of pins set num of strips
//    numOfUsableStrips = input;
//  } else {
//    waitForNumInput(); //If not in range, wait for another input 
//  }
//}
//
//void setGreen(){
//  for(int i = 0; i<NUM_STRIPS; i++){
//    for(int j = 0; j<NUM_LEDS; j++){
//      leds[i][j].setRGB(0,255,0);     //Set all LEDs to red to indicate ready to recieve num of strips
//    }
//  }
//}
