package bikurim.silverfix.com.bikurim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.adapters.holders.FamilyViewHolder;
import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.adapters.holders.TimeUpViewHolder;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.utils.Utils;
import bikurim.silverfix.com.bikurim.utils.managers.CountDownManager;
import bikurim.silverfix.com.bikurim.utils.interfaces.TimerEventListener;

public class FamiliesAdapter extends RecyclerView.Adapter<GenericViewHolder> implements Filterable, TimerEventListener {

    // last position holds the last position of the element that was added, for animation purposes
    private static int lastPosition = -1;
    private Context context;
    private ArrayList<Family> families;
    private ArrayList<Family> dataSet;

    private int[] viewTypes;

    private FamilyFilter filter;

    private CountDownManager countDownManager;

    public FamiliesAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.dataSet = families;
        this.families = dataSet;

        viewTypes = Constants.Tags.VIEW_TYPES;

        countDownManager = new CountDownManager(Constants.Values.TIME_INTERVAL, this);
        countDownManager.start();
    }


    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflating the view from a given layout resource file
        View v;
        switch (viewType) {
            case Constants.Tags.FAMILY_VIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item, parent, false);
                FamilyViewHolder fvh = new FamilyViewHolder(v, context);
                return fvh;
            case Constants.Tags.TIME_UP_VIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeup_list_item, parent, false);
                TimeUpViewHolder tvh = new TimeUpViewHolder(v, context);
                return tvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        // Binds the Family object to the holder view
        Family family = families.get(position);
        holder.bindData(family);

        if(holder instanceof FamilyViewHolder)
            countDownManager.addHolder((FamilyViewHolder) holder);
        // Sets animation on the given view, in case it wasn't displayed before
        Utils.setSlideAnimation(context, holder.getView(), position, lastPosition, false);
    }

    @Override
    public int getItemCount() {
        return families != null ? families.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(families.get(position).timeLeft <= 0)
            return viewTypes[1];
        return viewTypes[0];
    }

    /* Gets the current the filter object */
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new FamilyFilter();
        return filter;
    }

    /* Changes the UI of a holder to a time's up view with a flickering ImageButton and a TextView*/

    @Override
    public void onFinish(FamilyViewHolder holder) {
        // Switches between the clock icon to the alarm icon
        holder.reset();
        notifyItemChanged(holder.getAdapterPosition());
    }

    @Override
    public void onLessThanMinute(FamilyViewHolder holder) {
        holder.cardView.setBackgroundResource(R.color.time_up_bg);
        holder.isBackgroundChanged = true;
    }

    public void removeData(int pos, GenericViewHolder holder) {
        // Sets the last position to the given deleted position for animation purposes
        lastPosition = pos;

        // Removes the family object from the data set
        families.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());

        if(holder instanceof FamilyViewHolder)
            // Cancels the the timer and removes it from the entry set
            countDownManager.cancelTimer((FamilyViewHolder) holder);

        holder.reset();
    }


    /* Cancels the timers and clears the entry set */
    public void cancelTimers() {
        countDownManager.reset();
        countDownManager.stop();
    }

    /* Clears the adapter's data and resets the last position to -1 */
    public void clearData() {
        cancelTimers();
        filter = null;
        lastPosition = -1;
    }

    /* Filter class that queries the constraint on the data set every whenInMillis the user types a key */
    private class FamilyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint.length() == 0 || constraint == null) {
                results.values = dataSet;
                results.count = dataSet.size();
            } else {
                ArrayList<Family> queryResults = new ArrayList<Family>();
                for (Family f : dataSet) {
                    if (constraint.charAt(0) == f.lastName.toUpperCase().indexOf(0)) {
                        queryResults.add(f);
                    } else if (f.lastName.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        queryResults.add(f);
                    }
                }
                results.values = queryResults;
                results.count = queryResults.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            synchronized (families) {
                families = (ArrayList<Family>) results.values;
            }
            notifyDataSetChanged();
        }
    }
}
