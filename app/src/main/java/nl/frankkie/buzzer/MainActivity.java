package nl.frankkie.buzzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String PROJECT_ID = "616962368505";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = {MESSAGING_SCOPE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    protected void initUI() {
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }


    String myToken;

    protected void register() {
        myToken = PreferenceManager.getDefaultSharedPreferences(this).getString("myToken", "");
        if (myToken.length() == 0) {

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        myToken = task.getResult().getToken();
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                                .edit().putString("myToken", myToken).apply();
                        register(); //retry
                    } else {
                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                        b.setMessage("Token is empty. Please restart app.");
                        b.setPositiveButton(android.R.string.ok, null);
                        b.create().show();
                    }
                }
            });
            return;
        }
        String regName = ((EditText) findViewById(R.id.ed_register)).getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> device = new HashMap<>();
        device.put("token", myToken);
        device.put("name", regName);
        db.collection("buzzer")
                .add(device)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        registerSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Buzzer", e.getMessage(), e);
                        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                        b.setMessage("Registration failed!");
                        b.setPositiveButton(android.R.string.ok, null);
                        b.create().show();
                    }
                });
    }

    protected void registerSuccess() {
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(getPackageName(), getPackageName() + ".MainActivity");
        //disable this activity
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        //show
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Registration success! Have fun!");
        b.setPositiveButton(android.R.string.ok, null);
        b.create().show();
    }

}
