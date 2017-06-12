package tk.smartdrunk.smartdrunk.appMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tk.smartdrunk.smartdrunk.AddDrinkActivity;
import tk.smartdrunk.smartdrunk.R;

/**
 * Created by Daniel on 24/11/2016.
 */

public class HomeFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    
    private DatabaseReference mDatabase;
    View my_view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        my_view=inflater.inflate(R.layout.home_layout, container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("Tabs");
        my_view.findViewById(R.id.addDrinkFab).setOnClickListener(this);
        return my_view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addDrinkFab) {
            addDrink();
        } else if (i == R.id.button_sign_up) {
            viewTab();
        }
    }

    private void addDrink(){
        startActivity(new Intent(getView().getContext(), AddDrinkActivity.class));
    }

    private void viewTab(){
        return;
    }
}
