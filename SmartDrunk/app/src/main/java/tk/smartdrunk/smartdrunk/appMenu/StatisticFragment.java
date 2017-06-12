package tk.smartdrunk.smartdrunk.appMenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.smartdrunk.smartdrunk.R;

/**
 * Created by Daniel on 24/11/2016.
 */

public class StatisticFragment extends android.support.v4.app.Fragment
{
    private static final String TAG = "StatisticFragment";

    View my_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        my_view=inflater.inflate(R.layout.statistics_layout, container,false);
        return my_view;
    }
}
