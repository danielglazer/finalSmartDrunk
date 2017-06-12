package tk.smartdrunk.smartdrunk.appMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tk.smartdrunk.smartdrunk.R;

public class InfoFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "InfoFragment";
    
    View my_view ;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        my_view=inflater.inflate(R.layout.activity_info, container,false);
        Button contact = (Button) my_view.findViewById(R.id.Contact_Button);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ContactByMail = new Intent(Intent.ACTION_SENDTO
                        , Uri.fromParts("mailto", "danielglazer123@gmail.com,alon.dankner1@gmail.com", null));
                ContactByMail.putExtra(Intent.EXTRA_SUBJECT, "I just love your Smart Drunk app...");
                ContactByMail.putExtra(Intent.EXTRA_TEXT, "I really enjoy your app because : ...");
                startActivity(Intent.createChooser(ContactByMail, "Send email..."));
            }

        });
        return my_view;
    }
}
