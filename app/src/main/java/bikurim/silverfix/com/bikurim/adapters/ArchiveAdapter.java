package bikurim.silverfix.com.bikurim.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.adapters.holders.ArchiveViewHolder;
import bikurim.silverfix.com.bikurim.adapters.holders.GenericViewHolder;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by דודו on 05/07/2016.
 */
public class ArchiveAdapter extends GenericAdapter {

    private int lastPosition = -1;
    public ArchiveAdapter(Context context, ArrayList<Family> families) {
        super(context, families);
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.archive_list_item, parent, false);
        ArchiveViewHolder avh = new ArchiveViewHolder(context, view);
        return avh;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        Family family = families.get(position);
        holder.bindData(family);

        Utils.setScaleAnimation(((ArchiveViewHolder) holder).container, holder.getAdapterPosition(), lastPosition);

    }

    @Override
    public void onViewDetachedFromWindow(GenericViewHolder holder) {
        ((ArchiveViewHolder) holder).clearAnimation();
    }
}
