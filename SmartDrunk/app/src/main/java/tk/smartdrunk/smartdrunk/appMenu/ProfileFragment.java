package tk.smartdrunk.smartdrunk.appMenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.loginAndRegister.SignInActivity;
import tk.smartdrunk.smartdrunk.models.User;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static tk.smartdrunk.smartdrunk.AddDrinkActivity.getAge;
import static tk.smartdrunk.smartdrunk.models.User.getUid;

public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";


    // Profile fields
    private User user;
    private EditText mEmailField, mPasswordField, mWeightField;
    private TextView genderTextView, mAgeField, contactNumber;
    private QuickContactBadge emergencyContact;
    private Switch newDriverSwitch;
    private Button update, delete;
    private DatabaseReference userDB, userTabsDB, tabDrinksDB;
    private FirebaseAuth mAuth;
    private ArrayList<String> tabsToDelete = new ArrayList<String>();
    private boolean isFirstUserChange = true;
    private boolean isFirstTabChange = true;
    View my_view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.activity_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        isFirstUserChange = true;
        isFirstTabChange = true;
        mEmailField = (EditText) my_view.findViewById(R.id.field_email);
        mPasswordField = (EditText) my_view.findViewById(R.id.field_password);
        mWeightField = (EditText) my_view.findViewById(R.id.field_weight);
        genderTextView = (TextView) my_view.findViewById(R.id.genderTextView);
        mAgeField = (TextView) my_view.findViewById(R.id.birthDateTextView);
        emergencyContact = (QuickContactBadge) my_view.findViewById(R.id.emergencyContact);
        newDriverSwitch = (Switch) my_view.findViewById(R.id.newDriverSwitch);
        contactNumber = (TextView) my_view.findViewById(R.id.contact_number);
        update = (Button) my_view.findViewById(R.id.updateProfileButton);
        delete = (Button) my_view.findViewById(R.id.deleteUserButton);
        //if not logged in
        if (getUid() == null) {
            mAuth.signOut();
            // Go to SignInActivity
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        userDB = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());
        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        tabDrinksDB = FirebaseDatabase.getInstance().getReference().child("tab-drinks");
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null && isFirstUserChange) {
                    isFirstUserChange = false;
                    mEmailField.setText(user.getEmail());
                    mWeightField.setText(String.valueOf(user.getWeight()));
                    String[] birth = user.getBirthDate().split("\\.");
                    Calendar date = Calendar.getInstance();
                    date.set(YEAR, parseInt(birth[2]));
                    date.set(MONTH, parseInt(birth[0]));
                    date.set(Calendar.DAY_OF_MONTH, parseInt(birth[1]));
                    genderTextView.setText("Gender:   " + user.getGender());
                    mAgeField.setText("Age:   " + getAge(date));
                    newDriverSwitch.setChecked(user.isNewDriver());
                    contactNumber.setText(user.getEmergencyContact());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ValueEventListener tabListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isFirstTabChange) {
                    isFirstTabChange = false;
                    tabsToDelete.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        tabsToDelete.add(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userDB.getRef().addValueEventListener(userListener);
        userTabsDB.getRef().addValueEventListener(tabListener);
        emergencyContact.setImageToDefault();
        update.setOnClickListener(this);
        emergencyContact.setOnClickListener(this);
        delete.setOnClickListener(this);
        return my_view;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.updateProfileButton) {
            updateProfile();
        } else if (i == R.id.emergencyContact) {
            pickContact();
        } else if (i == R.id.deleteUserButton) {
            deleteUser();
        }
    }

    void updateProfile() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        user.setNewDriver(newDriverSwitch.isChecked());
        user.setEmergencyContact(contactNumber.getText().toString());
        user.setWeight(Double.parseDouble(mWeightField.getText().toString()));


        FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        if (!user.getEmail().equals(mEmailField.getText().toString())) {
            firebaseuser.updateEmail(mEmailField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.setEmail(mEmailField.getText().toString());
                                update();
                                Log.d(TAG, "User email address updated.");
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "User email updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        update();
        if (!TextUtils.isEmpty(mPasswordField.getText().toString())) {
            firebaseuser.updatePassword(mPasswordField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "User password updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "User password update failed.");
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Password update failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    void pickContact() {
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent1, 1);
    }

    private void update() {
        Map<String, Object> userValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + getUid(), userValues);
        userDB.getParent().updateChildren(childUpdates);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    //Context applicationContext = MenuActivity.getContextOfApplication();
                    //applicationContext.getContentResolver();
                    c = getActivity().getApplicationContext().getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.Data.DISPLAY_NAME,
                                    ContactsContract.Contacts.Photo.PHOTO,
                            },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        //Todo: add the photo of the selected emergency contact
//                           byte[] bytedata = c.getBlob(2);
//                            if (bytedata != null) {
//                                emergencyContact.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(bytedata)));
//                                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver()
//                                        , uri);
//                                emergencyContact.setImageBitmap(BitmapFactory.decodeStream(input));
//                            }
//
//                                Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(ContactsContract.Contacts._ID));
//                                QuickContactBadge badge = (QuickContactBadge) findViewById(R.id.emergencyContact);
//                                badge.setMode(ContactsContract.QuickContact.MODE_LARGE);
//                                badge.assignContactUri(contactUri);
//                                Uri photoUri = Uri.parse(c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
//                                        emergencyContact.setImageURI(photoUri);
//                            }
//                            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//                                    .parseLong(getId()));
//                            Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//                            if (u != null) {
//                                emergencyContact.setImageURI(u);
//                            } else {
//                                emergencyContact.setImageResource(R.drawable.ic_action_contact);
//                            }
                        TextView nameText = (TextView) my_view.findViewById(R.id.contact_name);
                        nameText.setText(c.getString(1));
                        TextView numberText = (TextView) my_view.findViewById(R.id.contact_number);
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

    protected boolean validateForm() {
        boolean result = true;
        if (!mEmailField.getText().toString().contains("@")) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }


        if (TextUtils.isEmpty(mWeightField.getText().toString())) {
            mWeightField.setError("Required");
            result = false;
        }

        if (TextUtils.isEmpty(mWeightField.getText().toString()) == false) {
            if (Double.parseDouble(mWeightField.getText().toString()) < 0.2835) {
                mWeightField.setError("Invalid - The lightest baby in the world weight more");
                result = false;
            } else if (Double.parseDouble(mWeightField.getText().toString()) > 635.03) {
                mWeightField.setError("Invalid - The Heaviest person in the world weight less");
                result = false;
            } else {
                mWeightField.setError(null);
            }
        }


        if (TextUtils.isEmpty(contactNumber.getText().toString())) {
            contactNumber.setError("Required");
            result = false;
        } else {
            contactNumber.setError(null);
        }
        return result;
    }

    private void deleteUser() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        firebaseUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account .");
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    "User was deleted successfully.", Toast.LENGTH_SHORT).show();
                                            //Todo:delete tabs and drinks of this user from the firebase realtime DB
                                            for (String temp : tabsToDelete) {
                                                if (tabDrinksDB.child(temp) != null) {
                                                    tabDrinksDB.child(temp).getRef().removeValue();
                                                }
                                            }
                                            if (userTabsDB != null) {
                                                userTabsDB.getRef().removeValue();
                                            }
                                            if (userDB != null) {
                                                userDB.getRef().removeValue();
                                            }
                                            // Go to SignInActivity
                                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    "User delete failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked - do nothing
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setMessage("Are you sure you want to delete this user?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}