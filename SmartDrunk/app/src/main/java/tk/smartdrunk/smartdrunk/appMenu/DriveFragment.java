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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tk.smartdrunk.smartdrunk.R;

import static tk.smartdrunk.smartdrunk.R.color;
import static tk.smartdrunk.smartdrunk.R.id;
import static tk.smartdrunk.smartdrunk.R.layout;
import static tk.smartdrunk.smartdrunk.appMenu.MenuActivity.user;
import static tk.smartdrunk.smartdrunk.models.User.getCalendar;

public class DriveFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "DriveFragment";

    private TextView tvDay, tvHour, tvMinute, tvSecond, tvEvent;
    private LinearLayout linearLayout;
    private Handler handler;
    private Runnable runnable;

    View my_view;

    @SuppressLint("SimpleDateFormat")
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_view = inflater.inflate(layout.drive_fragment, container, false);

        linearLayout = (LinearLayout) my_view.findViewById(id.ll1);
        tvDay = (TextView) my_view.findViewById(id.txtTimerDay);
        tvHour = (TextView) my_view.findViewById(id.txtTimerHour);
        tvMinute = (TextView) my_view.findViewById(id.txtTimerMinute);
        tvSecond = (TextView) my_view.findViewById(id.txtTimerSecond);
        tvEvent = (TextView) my_view.findViewById(id.tvevent);
        return my_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countDownStart(computeTimeToEvent());
    }


    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart(final Calendar calendar) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    // Here Set your Event Date
                    Date currentDate = new Date();
                    if (calendar.after(currentDate)) {
                        long diff = calendarToDate(calendar).getTime() - currentDate.getTime();
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
                        tvEvent.setVisibility(View.VISIBLE);
                        tvEvent.setTextColor(getResources().getColor(color.green));
                        linearLayout.setVisibility(View.GONE);
                        tvEvent.setText(R.string.legally_can_drive);
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
//Convert Calendar to Date
    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public Calendar computeTimeToEvent() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 24 µg per 100 ml (0.024%) of breath (penalties only apply above 26 µg per 100 ml (0.026%) of breath due to
        // lawsuits about sensitivity of devices used).
        // This is equivalent to a BAC of 0.05.
        // New drivers,drivers under 24 years of age and commercial drivers 5 µg per 100 ml of breath.
        // This is equivalent to a BAC of 0.01.
        double driverMaxBAC = user.isNewDriver() ? 0.01 : 0.05;
        double DP = 0;
        int secondsDP = 0;
        Date lastUpdated = null;
        Calendar finishTime;
        if (user.getLastBAC() <= driverMaxBAC) {
            finishTime = Calendar.getInstance();
        } else {
            try {
                lastUpdated = dateFormat.parse(user.getLastUpdatedDate());
                double MR = user.getGender().equals("female") ? (0.017 * 1.1) : 0.017;
                DP = (user.getLastBAC() - driverMaxBAC) / MR;
                //convert DP to seconds
                secondsDP = (int) (DP * 60 * 60);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Something went wrong.", Toast.LENGTH_LONG).show();
            }
            finishTime = getCalendar(lastUpdated);
            finishTime.add(Calendar.SECOND, secondsDP);
        }
        return finishTime;
    }
}