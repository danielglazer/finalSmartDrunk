package tk.smartdrunk.smartdrunk.appMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import tk.smartdrunk.smartdrunk.R;

public class DriveFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "DriveFragment";

    private TextView tvDay, tvHour, tvMinute, tvSecond, tvEvent;
    private LinearLayout linearLayout1, linearLayout2;
    private Handler handler;
    private Runnable runnable;

    View my_view;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(R.layout.drive_fragment, container, false);

        linearLayout1 = (LinearLayout) my_view.findViewById(R.id.ll1);
        linearLayout2 = (LinearLayout) my_view.findViewById(R.id.ll2);
        tvDay = (TextView) my_view.findViewById(R.id.txtTimerDay);
        tvHour = (TextView) my_view.findViewById(R.id.txtTimerHour);
        tvMinute = (TextView) my_view.findViewById(R.id.txtTimerMinute);
        tvSecond = (TextView) my_view.findViewById(R.id.txtTimerSecond);
        tvEvent = (TextView) my_view.findViewById(R.id.tvevent);


        return my_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countDownStart();
    }


    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Here Set your Event Date
                    Date eventDate = dateFormat.parse("2017-8-3");
                    Date currentDate = new Date();
                    if (!currentDate.after(eventDate)) {
                        long diff = eventDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        tvDay.setText("" + String.format("%02d", days));
                        tvHour.setText("" + String.format("%02d", hours));
                        tvMinute.setText("" + String.format("%02d", minutes));
                        tvSecond.setText("" + String.format("%02d", seconds));
                    } else {
                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        tvEvent.setText("Android Event Start");
                        handler.removeCallbacks(runnable);
                        //handler.removeMessages(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    // //////////////COUNT DOWN END/////////////////////////

}