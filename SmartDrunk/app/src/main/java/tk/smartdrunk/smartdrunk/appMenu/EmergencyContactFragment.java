package tk.smartdrunk.smartdrunk.appMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import tk.smartdrunk.smartdrunk.R;

import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.user;

/**
 * Created by Daniel on 6/6/2017.
 */

public class EmergencyContactFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String TAG = "EmergencyContactFragmen";

    private String phoneNumber = user.getEmergencyContact();
    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.emergency_contact_fragment, container, false);
        ImageButton smsButton = (ImageButton) my_view.findViewById(R.id.smsButton);
        ImageButton callButton = (ImageButton) my_view.findViewById(R.id.callButton);
        smsButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
        return my_view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (phoneNumber != null) {
            if (i == R.id.smsButton) {
                smsContact();
            } else if (i == R.id.callButton) {
                callContact();
            }
        }
    }

    private void smsContact() {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms://" + phoneNumber));
        sendIntent.putExtra("address", "phoneNumber");
        sendIntent.putExtra("sms_body", "SOS - This is an auto generated message sent using the SmartDrunk app." +
                " The writer of this sms listed you as his/hers emergency contact and probably could use your help.");
        startActivity(sendIntent);
    }

    private void callContact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
