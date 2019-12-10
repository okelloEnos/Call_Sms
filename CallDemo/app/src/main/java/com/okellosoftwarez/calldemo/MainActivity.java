package com.okellosoftwarez.calldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callPerson(View view) {
        EditText phone = findViewById(R.id.etPhone);

//        Setting up the phone number from the edit text
        String phoneNumber = String.format("tel: %s", phone.getText().toString().trim());

//        Create the intent to launch phone dialer
        Intent callIntent = new Intent(Intent.ACTION_DIAL);

//        Convert the phoneNumber received and convert it into Uri to be used as phone number
        Uri phoneNumberUri = Uri.parse(phoneNumber);

//        Set the phoneNumberUri to the call intent data
        callIntent.setData(phoneNumberUri);

//        check to see if the app resolves to any app
        if (callIntent.resolveActivity(getPackageManager()) != null){
            startActivity(callIntent);
        }
        else {
            Toast.makeText(this, "Your Device is Missing Phone Dialer App...", Toast.LENGTH_SHORT).show();

//            Disable the Button
            ImageButton callBtn = findViewById(R.id.callBtn_image);
            callBtn.setEnabled(false);

        }
    }
}
/**When using this example there is no need to request for permission that are on Manifest file since
 * it Outsources its activity of Calling to the native Phone dialer already installed*/