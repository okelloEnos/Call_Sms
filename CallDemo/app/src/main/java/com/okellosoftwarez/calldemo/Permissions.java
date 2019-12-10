package com.okellosoftwarez.calldemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Permissions extends AppCompatActivity {

    private TelephonyManager telephonyManager;
    private static final int CALL_REQUEST = 1;

    private phoneListener mphoneListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

//        Create telephony manager
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (isTelephonyEnabled()) {

//            Check for phone Permission
            checkPhonePermissiom();

//            Register the phone Listener to monitor Phones activity

            mphoneListener = new phoneListener();

            telephonyManager.listen(mphoneListener, phoneListener.LISTEN_CALL_STATE);

        } else {
            Toast.makeText(this, "TELEPHONY NOT ENABLED...", Toast.LENGTH_SHORT).show();

//            disable the Button
            disableBtn();
        }

    }

    private void checkPhonePermissiom() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

//            Permission not granted request for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

        } else {
            enableButton();
        }

    }

    private void disableBtn() {

        ImageButton imageButton = findViewById(R.id.imageButtonCall);
        imageButton.setVisibility(View.INVISIBLE);

        if (isTelephonyEnabled()) {
            Button retryBtn = findViewById(R.id.retryBtn);
            retryBtn.setVisibility(View.VISIBLE);
        }
    }

    private void enableButton() {

        ImageButton imageButton = findViewById(R.id.imageButtonCall);
        imageButton.setVisibility(View.VISIBLE);
    }

    private boolean isTelephonyEnabled() {
        if (telephonyManager != null) {
            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    public void calling(View view) {

    Intent callIntent = new Intent(Intent.ACTION_CALL);

//    Receiving the phone Number
        EditText phone = findViewById(R.id.etPhone);
        String string_phone = phone.getText().toString().trim();

        String realPhone = String.format("tel: %s", string_phone);
        Uri phoneNumber = Uri.parse(realPhone);

        callIntent.setData(phoneNumber);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);

        }
    }

    public void retryApp(View view) {
        enableButton();

        Intent restart_intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(restart_intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CALL_REQUEST: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                Permission Granted
                } else {
                    Toast.makeText(this, "Failed to gain Permission...", Toast.LENGTH_SHORT).show();

//                Disable the button
                    disableBtn();
                }

            }
        }
    }
    public class phoneListener extends PhoneStateListener {
        private boolean returnFromHook = false;
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            String message = "Phone Status : ";
            switch (state){

                case TelephonyManager.CALL_STATE_RINGING :
//               For Incoming call(No outgoing calls)
                    message = message + "RINGING , number : " + phoneNumber;
                    Toast.makeText(Permissions.this, message, Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK :
//                Call active...
                    message = message + "OFF_HOOK...";
                    Toast.makeText(Permissions.this, message, Toast.LENGTH_SHORT).show();
                    returnFromHook = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE :
//                no call connected after and before a call
//                 Phone is idle before and after phone call.
//                 If running on version older than 19 (KitKat),
//                 restart activity when phone call ends.

                    message = message + "IDLE : ";

                    Toast.makeText(Permissions.this, message, Toast.LENGTH_SHORT).show();

//                check for returning from off_hook/active call
                    if (returnFromHook){
                        // No need to do anything if >= version KitKat.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
//                        Restart the app

                            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                    break;
                default:
//                    an exception

                    message = message + "Phone Off ...";
                    Toast.makeText(Permissions.this, message, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        destroy the phone listener
        if (isTelephonyEnabled()){
            telephonyManager.listen(mphoneListener, phoneListener.LISTEN_NONE);
        }
    }
}
