package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.ticketwatchlistmanager.MainActivity;

import java.util.*;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {
    //I chose to make the format strict and only allow for uppercase messages with no spaces.
    private static final Pattern format = Pattern.compile("^Ticker:<<[A-Z]{1,5}>>$");

    @Override
    public void onReceive(Context context, Intent intent) {
       final Bundle bundle = intent.getExtras();
       if (bundle == null){
           return;
       }

       Object[] pdus = (Object[]) bundle.get("pdus");
       if (pdus == null || pdus.length == 0) {
           return;
       }

       String pduFormat = bundle.getString("format");
       StringBuilder sb = new StringBuilder();

       for (Object pdu: pdus){
           SmsMessage message;
           if (format != null) {
               message = SmsMessage.createFromPdu((byte[]) pdu, pduFormat);
           } else {
               message = SmsMessage.createFromPdu((byte[]) pdu);
           }
           if (message != null && message.getMessageBody() != null){
               sb.append(message.getMessageBody());
           }
       }

       String message = sb.toString().trim();
       String status;
       String ticker = null;

       if (format.matcher(message).matches()){

           int start = message.indexOf("<<") + 2;
           int end = message.indexOf(">>");
           ticker = message.substring(start, end).toUpperCase();
           status = "valid";
       } else {
           status = "invalid format";
       }

       Intent launchIntent = new Intent(context, MainActivity.class);
       launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
       launchIntent.putExtra("sms_status", status);
       launchIntent.putExtra("sms_ticker", ticker);
       context.startActivity(launchIntent);
    }
}