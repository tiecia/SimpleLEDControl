#include <Firmata.h>
#include <FastLED.h>
#include <Arduino.h>
#include <HardwareSerial.h>
#include <HardwareSerial.cpp>
#include <EEPROM.h>


#define NUM_LEDS 200
#define NUM_STRIPS 2


int stripPin;
int brightness;
int red;
int green;
int blue;

CRGB leds[2][NUM_LEDS];

void setup()
{
    Serial.begin(115200);
    FastLED.addLeds<NEOPIXEL, 2>(leds[0], NUM_LEDS);
    FastLED.addLeds<NEOPIXEL, 3>(leds[1], NUM_LEDS);
    FastLED.clear(true);
    
    FastLED.setBrightness(100);
    FastLED.show();
}

void loop()
{
  if(Serial.available() >= 5){
    stripPin = Serial.read();
    brightness = Serial.read();
    red = Serial.read();
    green = Serial.read();
    blue = Serial.read();

    printDebug();

    FastLED.setBrightness(brightness);
    for(int j = 0; j<NUM_LEDS; j++){
      leds[stripPin-2][j].setRGB(red,green,blue);
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
