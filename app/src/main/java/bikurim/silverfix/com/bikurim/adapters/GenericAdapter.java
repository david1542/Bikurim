package bikurim.silverfix.com.bikurim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by David on 11/07/2016.
 */
public abstract class GenericAdapter extends RecyclerView.Adapter<GenericViewHolder> implements Filterable{

    protected Context context;
    protected ArrayList<Family> dataSet, families;

    protected FamilyFilter filter;

    public GenericAdapter(Context context, ArrayList<Family> families) {
        this.context = context;
        this.dataSet = families;
        this.families = dataSet;
    }
    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {

    }

    public void setDataSet(ArrayList<Family> dataSet) {
        this.dataSet = dataSet;
        families = this.dataSet;
    }
    /* Gets the current the filter object */
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new FamilyFilter();
        return filter;
    }


    @Override
    public int getItemCount() {
        return families != null ? families.size() : 0;
    }

    public void clearData() {
        filter = null;
        families = null;
        dataSet = null;
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
                    if (constraint.charAt(0) == f.name.toUpperCase().indexOf(0)) {
                        queryResults.add(f);
                    } else if (f.name.toUpperCase().contains(constraint.toString().toUpperCase())) {
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
