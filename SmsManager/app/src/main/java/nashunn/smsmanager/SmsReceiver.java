package nashunn.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Class listening to incoming SMS in broadcast
 *
 * Created by Nicolas B. for SmsManager
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String logTAG = "SmsBroadcastReceiver";

    //Filters
    private static String serviceProviderNumberCondition = ""; //Can be used to filter readed message by sender
    private static String serviceProviderContentCondition = ""; //Can be used to filter readed message by specific text

    private SmsListener listener;

    /**
     * Set conditions' values used to filter readed messages
     *
     * @param numberCondition
     * @param contentCondition
     */
    public static void SetSmsBroadcastReceiverConditions(String numberCondition, String contentCondition) {
        serviceProviderNumberCondition = numberCondition;
        serviceProviderContentCondition = contentCondition;
    }

    /**
     * Set the Listener interface
     *
     * @param l Sms Listener
     */
    public void setListener(SmsListener l) {
        this.listener = l;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";

            // If OS version is >= Android KitKat
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            }
            else {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to user
                        Log.e(logTAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }

            //Check filters
            if(listener != null) {
                // Listen all
                if(serviceProviderNumberCondition.equals("") && serviceProviderContentCondition.equals(""))
                    listener.onTextReceived(smsBody); //notify

                // Listen only when number corresponds to conditions
                else if(!serviceProviderNumberCondition.equals("") && serviceProviderContentCondition.equals("")) {
                    if(smsSender.equals(serviceProviderNumberCondition))
                        listener.onTextReceived(smsBody); //notify
                }

                // Listen only when content start with conditions
                else if(serviceProviderNumberCondition.equals("") && !serviceProviderContentCondition.equals("")) {
                    if(smsBody.startsWith(serviceProviderContentCondition))
                        listener.onTextReceived(smsBody); //notify
                }

                // Listen only when number and content corresponds to conditions
                else {
                    if(smsSender.equals(serviceProviderNumberCondition) && smsBody.startsWith(serviceProviderContentCondition))
                        listener.onTextReceived(smsBody); //notify
                }
            }
        }
    }
}
