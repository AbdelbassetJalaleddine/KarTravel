package jalaleddine.abdelbasset.kartravel;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class VerifyPhoneActivity extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    String Name;
    String phonenumber;
    String gender;
    boolean signup = false;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please Wait...")
                .setCancelable(false)
                .build();


        phonenumber = getIntent().getStringExtra("phonenumber");
        Name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        signup = getIntent().getBooleanExtra("Sign Up",false);

        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && signup) {
                            alertDialog.show();
                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            FirebaseDatabase.getInstance().getReference().child(FirebaseInstanceId.getInstance().getId());
                            FirebaseDatabase.getInstance().getReference().child(FirebaseInstanceId.getInstance().getId()).child("Gender").setValue(gender);
                            FirebaseDatabase.getInstance().getReference().child(FirebaseInstanceId.getInstance().getId()).child("Name").setValue(Name);
                            FirebaseDatabase.getInstance().getReference().child(FirebaseInstanceId.getInstance().getId()).child("Phone Number").setValue(phonenumber);

                            FirebaseDatabase.getInstance().getReference().child("Users").child(phonenumber);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(phonenumber).child("ID").setValue(FirebaseInstanceId.getInstance().getId());
                            FirebaseDatabase.getInstance().getReference().child("Users").child(phonenumber).child("Name").setValue(Name);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(phonenumber).child("Gender").setValue(gender);
                            SharedPreferences.Editor editor = getSharedPreferences("UsersData", MODE_PRIVATE).edit();
                            editor.putString("phonenumber", phonenumber);
                            editor.putString("name", Name);
                            editor.putString("gender", gender);
                            editor.apply();
                            alertDialog.hide();
                            startActivity(intent);

                        } else if(task.isSuccessful()){
                            alertDialog.show();
                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            alertDialog.hide();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VerifyPhoneActivity.this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
