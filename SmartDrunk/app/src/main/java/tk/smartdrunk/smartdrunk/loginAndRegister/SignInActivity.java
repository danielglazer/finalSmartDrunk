package tk.smartdrunk.smartdrunk.loginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import tk.smartdrunk.smartdrunk.BaseActivity;
import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.appMenu.MenuActivity;


public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    //private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText mEmailField, mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // FirebaseDatabase refrence and FirebaseAuth instance
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Click listeners
        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.resetPasswordButton).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }
    }


    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess();
                        } else {
                            Toast.makeText(SignInActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
    }

    private void onAuthSuccess() {

        //Todo: verify email to continue to the app
        // Check if user's email is verified
//        boolean emailVerified = user.isEmailVerified();

        // Go to MainActivity
        startActivity(new Intent(SignInActivity.this, MenuActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_in) {
            signIn();
        } else if (i == R.id.button_sign_up) {
            signUp();
        } else if(i == R.id.resetPasswordButton){
            resetPassword();
        }
    }
    private void resetPassword(){
        if(mEmailField.getText() != null) {
            mAuth.sendPasswordResetEmail(mEmailField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "A password reset email was sent to the email specified.");
                                Toast.makeText(getApplicationContext(),
                                        "A password reset email was sent to the email specified.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Email not registered.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
