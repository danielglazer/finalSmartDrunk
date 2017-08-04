package tk.smartdrunk.smartdrunk.appMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.smartdrunk.smartdrunk.R;

import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.currentTab;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.user;
import static tk.smartdrunk.smartdrunk.classifier_LDA.BAC_LDA.round;
import static tk.smartdrunk.smartdrunk.models.User.getUpdatedBAC;

/**
 * Created by Daniel on 8/4/2017.
 */

public class HangoverFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "HangoverFragment";

    private TextView currentBAC, suitableDriveBACLimit, currentTabMaxBAC, bestSeparator, confidenceValue;
    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //initializing variables
        my_view = inflater.inflate(R.layout.hangover_fragment, container, false);
        currentBAC = (TextView) my_view.findViewById(R.id.currentBAC);
        suitableDriveBACLimit = (TextView) my_view.findViewById(R.id.suitableDriveLimitBAC);
        currentTabMaxBAC = (TextView) my_view.findViewById(R.id.currentTabMaxBAC);
        bestSeparator = (TextView) my_view.findViewById(R.id.bestSeparator);
        confidenceValue = (TextView) my_view.findViewById(R.id.confidenceValue);

        currentBAC.setText("Current BAC is:  " + String.format("%.5f", round(getUpdatedBAC(user), 5)) + "%");
        double BACdrivelimit = user.isNewDriver() ? 0.01 : 0.05;
        suitableDriveBACLimit.setText("The drive limit suitable to your information is:  " + BACdrivelimit + "%");
        if (currentTab != null) {
            currentTabMaxBAC.setText("Current tab max BAC is:  " + String.format("%.5f", round(currentTab.getMaxBAC(), 5)) + "%");
        } else {
            currentTabMaxBAC.setText("Current tab max BAC is:  None. you don't have an open tab");
        }
        if (user.getBestSeparator() == -1 || user.getConfidenceValue() == -1) {
            bestSeparator.setText(R.string.have_no_separator);
        } else {
            bestSeparator.setText("The best separator for you is:  " +
                    String.format("%.5f", round(user.getBestSeparator(), 5)) + "%");
            confidenceValue.setText("The confidence value of this separator is:  " +
                    String.format("%.3f", round((user.getConfidenceValue() * 100), 3)) + "%");
            if ((round((user.getConfidenceValue() * 100), 3)) < 75) {
                //this estimation is kind of raw. alert the user
                confidenceValue.setTextColor(getResources().getColor(R.color.red));
            } else {
                //its good
                confidenceValue.setTextColor(getResources().getColor(R.color.green));
            }
        }
        if (user.getBestSeparator() != -1 && user.getConfidenceValue() != -1 && currentTab != null) {
            //it's risky for the user to drink more than what he/she already have
            currentTabMaxBAC.setTextColor(getResources().getColor(R.color.red));
            bestSeparator.setTextColor(getResources().getColor(R.color.red));
        }

        return my_view;
    }
}
