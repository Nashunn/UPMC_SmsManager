package nashunn.smsmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipSession.Listener;

/**
 * Created by Nicolas B. for SmsManager
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

    private String serviceProviderNumberCondition; //Can be used to filter readed message by sender
    private String serviceProviderContentCondition; //Can be used to filter readed message by specific text

    private Listener listener;

    /**
     * Set conditions' values used to filter readed messages
     * @param serviceProviderNumberCondition
     * @param serviceProviderContentCondition
     */
    public void SetSmsBroadcastReceiverConditions(String serviceProviderNumberCondition, String serviceProviderContentCondition) {
        this.serviceProviderNumberCondition = serviceProviderNumberCondition;
        this.serviceProviderContentCondition = serviceProviderContentCondition;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
