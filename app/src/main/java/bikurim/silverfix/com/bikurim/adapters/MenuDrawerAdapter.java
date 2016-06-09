package bikurim.silverfix.com.bikurim.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bikurim.silverfix.com.bikurim.items.MenuItem;
import bikurim.silverfix.com.bikurim.R;

/**
 * Created by Dudu Lasry on 08/06/2016.
 */
public class MenuDrawerAdapter extends RecyclerView.Adapter<MenuDrawerAdapter.MenuViewHolder> {

    private ArrayList<MenuItem> items;
    public MenuDrawerAdapter(ArrayList<MenuItem> items) {
        this.items = items;
    }
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, parent, false);
        MenuViewHolder pvh = new MenuViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(MenuDrawerAdapter.MenuViewHolder holder, int position) {
        holder.optionName.setText(items.get(position).name);
        holder.icon.setImageResource(items.get(position).imageId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView optionName;

        MenuViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.option_icon);
            optionName = (TextView) itemView.findViewById(R.id.option_name);
        }
    }
}
