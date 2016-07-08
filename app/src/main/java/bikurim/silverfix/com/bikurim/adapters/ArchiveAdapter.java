package bikurim.silverfix.com.bikurim.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;

/**
 * Created by דודו on 05/07/2016.
 */
public class ArchiveAdapter extends CursorAdapter {

    public ArchiveAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.archive_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ArchiveViewHolder viewHolder = new ArchiveViewHolder(view);
        viewHolder.setData(cursor);
    }

    public class ArchiveViewHolder {
        private TextView nameTv, dateTv, visitorsTv;

        public ArchiveViewHolder(View v) {
            nameTv = (TextView) v.findViewById(R.id.family_name);
            dateTv = (TextView) v.findViewById(R.id.family_date);
            visitorsTv = (TextView) v.findViewById(R.id.visitors_number);
        }

        public void setData(Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FamiliesTablesContract.NAME_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(FamiliesTablesContract.DATE_COLUMN));
            int visitors = cursor.getInt(cursor.getColumnIndexOrThrow(FamiliesTablesContract.VISITORS_COLUMNS));

            // Translates the milliseconds interval to a human-readable date/time format
            Date dateTime = new Date(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            String dateString = dateFormat.format(dateTime);
            // Binding the data to the text views
            nameTv.setText(name);
            dateTv.setText(dateString);
            visitorsTv.setText("" + visitors);
        }
    }
}
