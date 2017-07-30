package tk.smartdrunk.smartdrunk;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tk.smartdrunk.smartdrunk.models.Tab;

/**
 * Created by Daniel on 7/30/2017.
 */

public class TabListAdapter extends ArrayAdapter<Tab> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {

        ImageView hungover;
        TextView closeDate;
        TextView openDate;
        TextView maxBAC;
    }

    public TabListAdapter(@NonNull Context context, @LayoutRes int resource, List<Tab> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get tab's information
        String tabCloseDate = getItem(position).getTabCloseDate();
        String tabOpenDate = getItem(position).getTabOpenDate();
        String wasHangover = getItem(position).getWasHangover();
        double maxBAC = getItem(position).getMaxBAC();


        //Create a tab
        Tab tab = new Tab(tabOpenDate, tabCloseDate, wasHangover, maxBAC);

        //create the view result for showing the list
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.maxBAC = (TextView) convertView.findViewById(R.id.MaxBAC);
            holder.openDate = (TextView) convertView.findViewById(R.id.openDate);
            holder.closeDate = (TextView) convertView.findViewById(R.id.closeDate);
            holder.hungover = (ImageView) convertView.findViewById(R.id.imagehungover);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            result = convertView;
        }

        lastPosition = position;

        String stringBAC = String.valueOf(tab.getMaxBAC());
        holder.maxBAC.setText(stringBAC);
        holder.openDate.setText(tab.getTabOpenDate());
        holder.closeDate.setText(tab.getTabCloseDate());

        switch (tab.getWasHangover()){
            case "Not Supplied":holder.hungover.setImageResource(android.R.drawable.presence_invisible); break;
            case "Yes": holder.hungover.setImageResource(android.R.drawable.presence_online);break;
            case "No": holder.hungover.setImageResource(android.R.drawable.presence_busy);break;
        }


        return convertView;
    }
}
