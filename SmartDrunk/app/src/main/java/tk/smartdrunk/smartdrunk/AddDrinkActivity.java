package tk.smartdrunk.smartdrunk;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tk.smartdrunk.smartdrunk.models.User;

public class AddDrinkActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "AddDrinkActivity";

    private DatabaseReference userDB, userTabsDB, tabDrinksDB;
    private CheckBox lastDrink;
    private EditText drinkVolume, alcoholVolume, drinkNumber;
    private TextView servingSize, drinkType;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        userTabsDB = FirebaseDatabase.getInstance().getReference().child("user-tabs").child(getUid());
        tabDrinksDB = FirebaseDatabase.getInstance().getReference().child("tab-drinks");
        userDB = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());

        lastDrink = (CheckBox) findViewById(R.id.isLastDrinkCheckBox);
        drinkVolume = (EditText) findViewById(R.id.drinkVolumeEditText);
        alcoholVolume =(EditText) findViewById(R.id.alcoholVolumeEditText);
        drinkNumber = (EditText) findViewById(R.id.drinkNumberEditText);
        servingSize = (TextView) findViewById(R.id.drinkServingSizeTextView);
        drinkType = (TextView) findViewById(R.id.drinkTypeTextView);

        drinkVolume.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //TODO: show the user his serving size
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no op
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no op
            }

        });
        alcoholVolume.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //TODO: show the user his drink type
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no op
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no op
            }

        });
        ValueEventListener userListener , userTabsDB;
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userDB.getRef().addValueEventListener(userListener);

        userTabsDB= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        tabDrinksDB.getRef().addValueEventListener(userTabsDB);
        findViewById(R.id.addDrinkButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addDrinkButton) {
            addDrink();
        }
    }

    private void addDrink(){
        if(!validate()){
            return;
        }
        // We need to search for an open tab if found add a drink to this tab if not open one
        // We need to recalculate the user BAC
        // We need to close the tab if the user want to do so
        // If the this is the last drink in the tab we need to ask the user if he is hangover
    }
    private boolean validate(){
        boolean result = true;
        if (TextUtils.isEmpty(drinkVolume.getText().toString()) &&Integer.parseInt(drinkVolume.getText().toString()) > 0) {
            if(Integer.parseInt(drinkVolume.getText().toString()) > 0){
                drinkVolume.setError("Must be positive number");
            } else{
                drinkVolume.setError("Required");
            }
            result = false;
        } else {
            drinkVolume.setError(null);
        }
        if (TextUtils.isEmpty(alcoholVolume.getText().toString())) {
            if(Double.parseDouble(alcoholVolume.getText().toString()) < 100){
                alcoholVolume.setError("must be lower than 100%");
            }else{
                alcoholVolume.setError("Required");
            }
            result = false;
        } else {
            alcoholVolume.setError(null);
        }
        if (TextUtils.isEmpty(drinkNumber.getText().toString())) {
            drinkNumber.setError("Required");
            result = false;
        } else {
            drinkNumber.setError(null);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO: destroy watchers
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
