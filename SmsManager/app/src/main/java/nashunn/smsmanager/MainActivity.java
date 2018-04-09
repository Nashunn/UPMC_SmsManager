package nashunn.smsmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private Button btnSendSms;
    private EditText zone_phone;
    private EditText zone_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isSmsPermissionGranted();

        btnSendSms = (Button) findViewById(R.id.buttonSend);
        zone_phone = (EditText) findViewById(R.id.inputPhone);
        zone_msg = (EditText) findViewById(R.id.inputMsg);

        btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });
    }

    private void sendSMS() {
        Sms sms = new Sms();
        String phone = String.valueOf(zone_phone.getText());
        String msg = String.valueOf(zone_msg.getText());

        Toast.makeText(MainActivity.this, "Champs : "+phone+", "+msg, Toast.LENGTH_SHORT).show();

        if (phone != null && msg != null)
            sms.send(MainActivity.this, phone, msg);
        else
            Toast.makeText(MainActivity.this, "Veuillez renseigner les champs", Toast.LENGTH_SHORT).show();

    }


    /**
     * Check if permission for SMS is granted
     */
    public void isSmsPermissionGranted() {
        // If permission is not granted
        if (
            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously*
                // Don't block this thread waiting for the user's response
                //todo : https://developer.android.com/training/permissions/requesting.html
            }
            else {
                // Request permission to send sms
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS
                );

            }
        }
        // Permission has already been granted
        else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Sms available", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
