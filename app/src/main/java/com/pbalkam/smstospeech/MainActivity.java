package com.pbalkam.smstospeech;

import android.os.Bundle;
import android.app.Activity;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.TextView;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    TextToSpeech TTS;//Text to Speech
    Button sendBtn;//sends  user text
    Button recordBtn;//button to start recording
    EditText txtphoneNo;
    EditText txtMessage;
    TextView TextIn;
    Button  ReadButton;


    // String SentNum="";
    //  String gotNum="";

    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //---display the SMS received in the TextView---
            String phoneNo = txtphoneNo.getText().toString();
            TextIn = (TextView) findViewById(R.id.textView2);
            String gotNum=intent.getExtras().getString("Number");
            //SentNum=phoneNo;
            //TextIn.setText(phoneNo);
            //Boolean Equal=(SentNum==gotNum);
            //TextIn.setText("sent"+SentNum.length()+"Received Length"+gotNum.length()+Equal);
            if(phoneNo.contains(gotNum)) {
                TextIn.setText(intent.getExtras().getString("sms"));
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            //---register the receiver---
            registerReceiver(intentReceiver, intentFilter);

        TTS=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.UK);
                }
            }
        });
        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        recordBtn = (Button) findViewById(R.id.btnRecordSMSuser);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        txtMessage = (EditText) findViewById(R.id.editText2);
        ReadButton = (Button) findViewById(R.id.ReadButton);


//sending text message to number
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String message = txtMessage.getText().toString();
                sendSMSMessage(message);
            }
        });

        //Record Text Message out loud
        recordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String message = txtMessage.getText().toString();
                sendSMSMessage(message);
            }
        });

        //Read received Text Message out loud
        recordBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String toSpeak = TextIn.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();

                 TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });



    }

    protected void sendSMSMessage(String message) {
        Log.i("Send SMS", "");
        String phoneNo = txtphoneNo.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS did not send, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
//---register the receiver---
        //registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override

    protected void onPause() {
//---unregister the receiver---
        //unregisterReceiver(intentReceiver);

        if(TTS !=null){
            TTS.stop();
            TTS.shutdown();
        }
        super.onPause();
    }

    @Override
    //this means this will work even when away from app

    protected void onDestroy() {

//---unregister the receiver---
            unregisterReceiver(intentReceiver);
            super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
