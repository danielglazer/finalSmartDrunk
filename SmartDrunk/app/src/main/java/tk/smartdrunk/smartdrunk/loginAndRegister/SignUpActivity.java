package tk.smartdrunk.smartdrunk.loginAndRegister;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.appMenu.MenuActivity;
import tk.smartdrunk.smartdrunk.models.User;

import static tk.smartdrunk.smartdrunk.models.User.isValidDate;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // SignUp fields
    private EditText mEmailField, mPasswordField, mWeightField, mBirthDateField;
    private TextView genderTextView;
    private RadioGroup genderRadioGroup;
    private QuickContactBadge emergencyContact;
    private Switch newDriverSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // FirebaseDatabase refrence and FirebaseAuth instance
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views and fields
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mWeightField = (EditText) findViewById(R.id.field_weight);
        genderTextView = (TextView) findViewById(R.id.genderTextView);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        mBirthDateField = (EditText) findViewById(R.id.birthEditText);
        emergencyContact =(QuickContactBadge) findViewById(R.id.emergencyContact);
        newDriverSwitch = (Switch) findViewById(R.id.newDriverSwitch);
        emergencyContact.setImageToDefault();
        emergencyContact.setOnClickListener(this);
        findViewById(R.id.button_sign_up).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.button_sign_up) {
            signUp();
        } else if(i == R.id.emergencyContact){
             pickContact();
         }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    protected boolean validateForm() {
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

        if(TextUtils.isEmpty(mWeightField.getText().toString())){
            mWeightField.setError("Required");
            result = false;
        }

        if (TextUtils.isEmpty(mWeightField.getText().toString()) == false ){
            if(Double.parseDouble(mWeightField.getText().toString()) < 0.2835){
                mWeightField.setError("Invalid - The lightest baby in the world weight more");
                result = false;
            }
            else if(Double.parseDouble(mWeightField.getText().toString()) > 635.03){
                mWeightField.setError("Invalid - The Heaviest person in the world weight less");
                result = false;
            }
            else{
                mWeightField.setError(null);
            }
        }

        if(genderRadioGroup.getCheckedRadioButtonId() == -1){
            TextView genderTextView = (TextView) findViewById(R.id.genderTextView);
            genderTextView.setError("Required");
            result = false;
        } else {
            genderTextView.setError(null);
        }
        if(TextUtils.isEmpty(mBirthDateField.getText().toString())) {
            mBirthDateField.setError("Required");
            result = false;
        }
        if(TextUtils.isEmpty(mBirthDateField.getText().toString()) == false) {
            if(isValidDate(mBirthDateField.getText().toString()) == false) {
                result = false;
                mBirthDateField.setError("Not a valid date");
            } else {
                mBirthDateField.setError(null);
            }
        }
        TextView numberText = (TextView)findViewById(R.id.contact_number);
        if (TextUtils.isEmpty(numberText.getText().toString())) {
            numberText.setError("Required");
            result = false;
        } else {
            numberText.setError(null);
        }
        return result;
    }

    void pickContact(){
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.Data.DISPLAY_NAME,
                                    ContactsContract.Contacts.Photo.PHOTO,
                                    },
                            null, null, null);

                        if (c != null && c.moveToFirst()) {
                            TextView nameText =(TextView) findViewById(R.id.contact_name);
                            nameText.setText(c.getString(1));
                            TextView numberText = (TextView)findViewById(R.id.contact_number);
                            numberText.setText(c.getString(0));
                        }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }


//    public void showSelectedNumber(String[] contact) {
//
//        Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
//        findViewById(R.id.emergencyContact).setBackground();
//    }




    void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());

                            //send verification email
                            task.getResult().getUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });
                        } else {
                            //mAuth.getCurrentUser().delete();
                            Toast.makeText(SignUpActivity.this,
                                    "Sign Up Failed - email is already registered",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {

        // Write new user
        writeNewUser(user.getUid(), user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, MenuActivity.class));
        finish();
    }

    // [START register_write]
    void writeNewUser(String userId, String email){

        double weight = Double.parseDouble(mWeightField.getText().toString());
        String gender;
        if(genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButton_male){
            gender = "Male";
        } else if(genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButton_female){
            gender = "Female";
        } else {
            gender = "Other";
        }
        String birthDate = mBirthDateField.getText().toString();
        TextView numberText = (TextView)findViewById(R.id.contact_number);
        String contactNumber = numberText.getText().toString();
        User user1 = new User(email, weight, gender, birthDate, newDriverSwitch.isChecked(),contactNumber);

        mDatabase.child("users").child(userId).setValue(user1);
    }
    // [END register_write]

}
