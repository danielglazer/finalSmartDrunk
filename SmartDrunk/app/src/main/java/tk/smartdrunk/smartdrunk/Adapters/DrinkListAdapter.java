package tk.smartdrunk.smartdrunk.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.models.Drink;
import tk.smartdrunk.smartdrunk.models.Tab;

/**
 * Created by Daniel on 8/4/2017.
 */

public class DrinkListAdapter extends ArrayAdapter<Drink> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        ImageView drinkLogo;
        TextView alcoholVolume;
        TextView drinkNumber;
        TextView drinkDate;
        TextView drinkVolume;
    }

    public DrinkListAdapter(@NonNull Context context, @LayoutRes int resource, List<Drink> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get tab's information
        int drinkVolume = getItem(position).getDrinkVolume();
        double alcoholVolume = getItem(position).getAlcoholVolume();
        int drinkNumber = getItem(position).getDrinkNumber();
        String drinkDate= getItem(position).getDrinkDate();


        //Create a tab
        Drink drink = new Drink (drinkVolume, alcoholVolume, drinkNumber, drinkDate);

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
            holder.drinkLogo = (ImageView) convertView.findViewById(R.id.drinkLogo);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
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
        //holder.tabLogo.setImageResource(R.mipmap.ic_tab);
        return convertView;
    }
}
