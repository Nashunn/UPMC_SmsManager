package nashunn.smsmanager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Nicolas B. for SmsManager
 */
public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE =0 ;

    private Button btnSendSms;
    private EditText zone_phone;
    private EditText zone_msg;
    private Switch switchListener;

    private SmsReceiver smsReceiver = new SmsReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckSmsPermission(); //ask permission for sms

        btnSendSms = (Button) findViewById(R.id.buttonSend);
        zone_phone = (EditText) findViewById(R.id.inputPhone);
        zone_msg = (EditText) findViewById(R.id.inputMsg);
        switchListener = (Switch) findViewById(R.id.switchListener);

        //Listeners
        setSmsReceiver();
        setMainListeners();
    }


    /**
     * Send a sms with informations from inputs on main activity
     */
    private void sendSMS() {
        String phone = String.valueOf(zone_phone.getText());
        String msg = String.valueOf(zone_msg.getText());

        Toast.makeText(MainActivity.this, "Sms : "+phone+" -> "+msg, Toast.LENGTH_SHORT).show();

        if (phone != null && msg != null)
            Sms.send(MainActivity.this, phone, msg);
        else
            Toast.makeText(MainActivity.this, "Missing informations !", Toast.LENGTH_SHORT).show();

    }

    /**
     * Check if permission for SMS is granted, if not ask for it
     */
    private void CheckSmsPermission() {
        // If permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                // Display non-blocking explanation, see more below :
                //todo : https://developer.android.com/training/permissions/requesting.html
            }
            else {
                // Request permission to send sms
                showRequestPermissionsInfoAlertDialog(true);
            }
        }
    }

    /**
     * Self explanatory
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(MainActivity.this, "Sms available", Toast.LENGTH_SHORT).show();
                // Request permission to send sms
                else
                    CheckSmsPermission();
            }
        }
    }

    /**
     * Ask permission to send and read SMS
     */
    private void askSMSPermissions() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.SEND_SMS},
                SMS_PERMISSION_CODE
        );
    }

    /**
     * Explain to the user why we need permission
     * @param makeSystemRequestAfter
     */
    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequestAfter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialogSmsTitle);
        builder.setMessage(R.string.dialogSmsMsg);

        builder.setPositiveButton(R.string.actionOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request if true
                if (makeSystemRequestAfter) {
                    askSMSPermissions();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Set listeners of mainActivity
     */
    private void setMainListeners() {
        // Listeners
        switchListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeSmsBcListener();
            }
        });
        btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });
    }

    /**
     * Active or desactive the Sms broadcast Listener
     */
    public void activeSmsBcListener() {
        //Register the listener
        if(switchListener.isChecked()) {
            registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
            Toast.makeText(MainActivity.this, "Listenning SMS", Toast.LENGTH_SHORT).show();
        }
        //Unregister the listener
        else {
            unregisterReceiver(smsReceiver);
            Toast.makeText(MainActivity.this, "Listenning SMS", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set properly the SmsReceiver and conditions used to listen sms
     */
    public void setSmsReceiver() {
        //Set conditions
        SmsReceiver.SetSmsBroadcastReceiverConditions("", "@SMS");

        activeSmsBcListener(); //active the listener if switch is checked

        smsReceiver.setListener(new SmsListener() {
            @Override
            public void onTextReceived(String number, String text) {
                Toast.makeText(MainActivity.this, "Sms received : "+text, Toast.LENGTH_SHORT).show();
                Sms.send(MainActivity.this, number, "Your sms has been received");
            }
        });
    }
}
