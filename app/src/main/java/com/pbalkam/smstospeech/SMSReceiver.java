package com.pbalkam.smstospeech;

/**
 * Created by Peter on 6/7/2016.
 */

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;

import android.telephony.SmsMessage;

import android.widget.EditText;
import android.widget.Toast;





public class SMSReceiver extends BroadcastReceiver


{
    EditText txtphoneNo;

    @Override

    public void onReceive(Context context, Intent intent)

    {

//---get the SMS message passed in---

        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        String str = "";
        String sender="";

        if (bundle != null)

        {

//---retrieve the SMS message received---

            Object[] pdus = (Object[]) bundle.get("pdus");

            msgs = new SmsMessage[pdus.length];

            for (int i=0; i<msgs.length; i++){

                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                sender=msgs[i].getOriginatingAddress().toString();//get sender number

                //str += " :";

                str += msgs[i].getMessageBody().toString();

                str += "\n";

            }

//---display the new SMS message---



            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

//---launch the MainActivity---
            /*
            String PhoneNo=txtphoneNo.getText().toString();
            if(PhoneNo!=null) {//this keeps the app from always launching
                Intent mainActivityIntent = new Intent(context, MainActivity.class);

                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(mainActivityIntent);
            }
*/
            //---send a broadcast to update the SMS received in the activity---

            Intent broadcastIntent = new Intent();

            broadcastIntent.setAction("SMS_RECEIVED_ACTION");

            broadcastIntent.putExtra("sms", str);
            broadcastIntent.putExtra("Number",sender);

            context.sendBroadcast(broadcastIntent);
        }

    }

}
