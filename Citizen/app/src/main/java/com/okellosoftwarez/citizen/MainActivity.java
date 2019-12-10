package com.okellosoftwarez.citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phone, message, sender;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.etPhone);
        message = findViewById(R.id.etMessage);
        sender = findViewById(R.id.etFromPhone);
        sendBtn = findViewById(R.id.sendBtn);
    }

    public void send_Btn(View view) {
        int permissionChecked = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionChecked == PackageManager.PERMISSION_GRANTED){

            for (int i = 0 ; i<10 ; i++) {
                myMessage();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

    private void myMessage() {

        String phoneNo = phone.getText().toString().trim();
        String senderNo = sender.getText().toString().trim();
        String txt_message = message.getText().toString().trim();

        if (!phoneNo.equals("") || !txt_message.equals("") || !senderNo.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, senderNo, txt_message, null, null);

            Toast.makeText(this, "Message Sent...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Do not have required Permission...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 0 :
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    myMessage();
                }
                else {
                    Toast.makeText(this, "You Don't Have the Required Permission...", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
