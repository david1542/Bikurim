package bikurim.silverfix.com.bikurim.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bikurim.silverfix.com.bikurim.items.FamilyItem;
import bikurim.silverfix.com.bikurim.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder> implements Filterable{

    private ArrayList<FamilyItem> families;
    private ArrayList<FamilyItem> dataSet;
    private ArrayList<PersonViewHolder> holders;
    private Handler handler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (holders) {
                for (PersonViewHolder holder : holders) {
                    long currentTime = System.currentTimeMillis();
                    holder.updateFormatTime(currentTime);
                }
            }
        }
    };
    private FamilyFilter filter;
    private boolean flagSearch;

    public RecyclerViewAdapter(ArrayList<FamilyItem> families) {
        this.dataSet = families;
        this.families = families;
        holders = new ArrayList<PersonViewHolder>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }


    @Override
    public RecyclerViewAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item, parent, false);
        RecyclerViewAdapter.PersonViewHolder pvh = new RecyclerViewAdapter.PersonViewHolder(v);
        synchronized (holders) {
            holders.add(pvh);
        }
        return pvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.PersonViewHolder holder, int position) {
        FamilyItem familyItem = families.get(position);
        holder.setData(familyItem);
    }

    @Override
    public int getItemCount() {
        return families.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new FamilyFilter();
        return filter;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView personLname;
        public TextView timeLeft;
        public FamilyItem familyItem;

        public PersonViewHolder(View itemView) {
            super(itemView);
            personLname = (TextView) itemView.findViewById(R.id.person_Lname);
            timeLeft = (TextView) itemView.findViewById(R.id.person_timeLeft);
        }

        public void setData(FamilyItem item) {
            familyItem = item;
            personLname.setText(familyItem.lastName);
            updateFormatTime(System.currentTimeMillis());
        }

        public void updateFormatTime(long currentMillis) {
            long timeLeft = familyItem.time - currentMillis;
            if (timeLeft > 0) {
                int seconds = (int) (timeLeft / 1000) % 60;
                int minutes = (int) ((timeLeft / (1000 * 60)) % 60);
                if(seconds > 10 && minutes > 10)
                    this.timeLeft.setText(minutes + ":" + seconds);
                else if(seconds > 10 && minutes < 10)
                    this.timeLeft.setText("0" + minutes + ":" + seconds);
                else if (seconds < 10 && minutes > 10)
                    this.timeLeft.setText(minutes + ":0" + seconds);
                else if (seconds < 10 && minutes < 10)
                    this.timeLeft.setText("0" + minutes + ":0" + seconds);
            } else {
                this.timeLeft.setText("Expired!");
            }
        }
    }

    private class FamilyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint.length() == 0 || constraint == null) {
                results.values = dataSet;
                results.count = dataSet.size();
                Log.d("Constraint is empty", "TRUE");
            } else {
                ArrayList<FamilyItem> queryResults = new ArrayList<FamilyItem>();
                for (FamilyItem f : dataSet) {
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
            // Now we have to inform the adapter about the new list filtered
            families = (ArrayList<FamilyItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
