package com.moringaschool.mynewsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class OtpRegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    EditText phoneNumber, codeEnter;
    Button nextBtn;
    ProgressBar progressBar;
    TextView state;
    CountryCodePicker codePicker;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        phoneNumber = findViewById(R.id.phone);
        codeEnter = findViewById(R.id.codeEnter);
        progressBar = findViewById(R.id.progressBar);
        nextBtn = findViewById(R.id.nextBtn);
        state = findViewById(R.id.state);
        codePicker = findViewById(R.id.ccp);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verificationInProgress){
                    if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 10){

                        String phoneNum = "+"+codePicker.getSelectedCountryCode()+phoneNumber.getText().toString();
                        Log.d(TAG,"onClick: Phone No -> " + phoneNum);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP..");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);


                    }else {
                        phoneNumber.setError("Phone Number is Not Valid");
                    }
                }else
                {
                    String userOTP = codeEnter.getText().toString();
                    if (!userOTP.isEmpty() && userOTP.length() == 6){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                        verifyAuth(credential);
                    }else
                    {
                        codeEnter.setError("Valid OTP is required.");
                    }
                }
            }
        });
    }
}
