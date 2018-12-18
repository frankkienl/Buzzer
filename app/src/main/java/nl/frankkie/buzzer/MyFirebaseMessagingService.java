package nl.frankkie.buzzer;

import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putString("myToken", s).apply();
        Toast.makeText(this, "Token", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(2 * 1000);
    }
}
