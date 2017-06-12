package tk.smartdrunk.smartdrunk.loginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tk.smartdrunk.smartdrunk.BaseActivity;
import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.appMenu.MenuActivity;


public abstract class LoginOrRegister extends BaseActivity implements View.OnClickListener{

    EditText mEmailField;
    EditText mPasswordField;
    Button mSignUpButton;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FirebaseDatabase refrence and FirebaseAuth instance
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);

        // Click listeners
        mSignUpButton.setOnClickListener(this);
    }

     void onAuthSuccess(FirebaseUser user) {
         String username = usernameFromEmail(user.getEmail());

         // Write new user
         writeNewUser(user.getUid(), username, user.getEmail());

         // Go to MainActivity
         startActivity(new Intent(this, MenuActivity.class));
         finish();
    }

     String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }



    // [START basic_write]
      abstract void writeNewUser(String userId, String name, String email);
    // [END basic_write]

    @Override
    public abstract void onClick(View v);
    abstract boolean validateForm();
    abstract void signUp();


}
