package nashunn.smsmanager;

/**
 * Interface that help notify whenever a SMS is received by SmsReceiver
 *
 * Created by Nicolas B. for SmsManager
 */
public interface SmsListener {
    void onTextReceived(String text);
}
