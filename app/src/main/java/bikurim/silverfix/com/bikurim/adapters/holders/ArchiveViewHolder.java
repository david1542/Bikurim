package bikurim.silverfix.com.bikurim.adapters.holders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by דודו on 11/07/2016.
 */
public class ArchiveViewHolder extends GenericViewHolder {

    private TextView nameTv, dateTv, visitorsTv;
    public RelativeLayout container;

    private Family family;
    public ArchiveViewHolder(Context context, View v) {
        super(v, context);

        container = (RelativeLayout) v.findViewById(R.id.archive_container);
        nameTv = (TextView) v.findViewById(R.id.full_name);
        dateTv = (TextView) v.findViewById(R.id.family_date);
        visitorsTv = (TextView) v.findViewById(R.id.visitors_number);
    }

    @Override
    public void bindData(Family family) {
        this.family = family;

        // Extracting the values from the family object
        String name = Utils.formatNameString(family.name);
        long date = family.date;
        int visitors = family.visitorsNum;

        // Translates the milliseconds interval to a human-readable date/time format
        Date dateTime = new Date(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String dateString = dateFormat.format(dateTime);

        // Binding the data to the relevant UI widgets
        nameTv.setText(name);
        visitorsTv.setText("" + visitors);
        dateTv.setText(dateString);
    }

    @Override
    public void reset() {
        family = null;
        nameTv.setText("");
        visitorsTv.setText("");
        dateTv.setText("");
    }

    public void clearAnimation() {
        container.clearAnimation();
    }
}
