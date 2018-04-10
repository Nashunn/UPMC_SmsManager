package nashunn.smsmanager;

import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Class that do basic actions with SMS
 *
 * Created by Nicolas B. for SmsManager
 */
public class Sms {
    private SmsManager smsManager = SmsManager.getDefault();

    public void send(AppCompatActivity context, String phone, String msg) {
        smsManager.sendTextMessage(phone, null, msg, null, null);
    }
}
