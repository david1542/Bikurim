package bikurim.silverfix.com.bikurim.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.items.FamilyItem;
import bikurim.silverfix.com.bikurim.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder> {

    private ArrayList<FamilyItem> tempArray;
    private ArrayList<FamilyItem> families;
    private FamilyFilter filter;
    private boolean flagSearch;

    public RecyclerViewAdapter(ArrayList<FamilyItem> families) {
        this.families = families;
        tempArray = new ArrayList<FamilyItem>();
        tempArray.addAll(families);
        Log.d("tempArray: ", "" + tempArray.size());
        Log.d("families: ", "" + families.size());
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personLname.setText(families.get(position).lastName);
        holder.timeLeft.setText(families.get(position).timeLeft);
    }

    @Override
    public int getItemCount() {
        return families.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new FamilyFilter();
        return filter;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personLname;
        TextView timeLeft;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personLname = (TextView) itemView.findViewById(R.id.person_Lname);
            timeLeft = (TextView) itemView.findViewById(R.id.person_timeLeft);
        }
    }

    private class FamilyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint.length() == 0 || constraint == null) {
                results.values = tempArray;
                results.count = tempArray.size();
                families.addAll(tempArray);
            } else {
                ArrayList<FamilyItem> queryResults = new ArrayList<FamilyItem>();
                for (FamilyItem f : tempArray) {
                    if (constraint.charAt(0) == f.lastName.toUpperCase().indexOf(0)){
                        queryResults.add(f);
                    }
                    else if (f.lastName.toUpperCase().contains(constraint.toString().toUpperCase())) {
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
            families = (ArrayList<FamilyItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
