package bikurim.silverfix.com.bikurim.adapters.holders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 07/07/2016.
 */
public class TimeUpViewHolder extends GenericViewHolder {

    public CardView cardView;
    public TextView personLname;
    public TextView visitors;
    public ImageButton remove;

    public Family family;

    public TimeUpViewHolder(View item, Context context) {
        super(item, context);

        // Getting the references for the UI components
        cardView = (CardView) itemView.findViewById(R.id.cv);
        personLname = (TextView) itemView.findViewById(R.id.person_Lname);
        visitors = (TextView) itemView.findViewById(R.id.visitors);
        remove = (ImageButton) itemView.findViewById(R.id.time_up);

        // Starting the fade animation on the remove button
        Utils.setFadeAnimation(remove);
    }

    @Override
    public void bindData(Family family) {
        this.family = family;
        personLname.setText(family.lastName);
        visitors.setText("מבקרים:  " + family.visitorsNum);

    }

    @Override
    public void reset() {
        personLname.setText("");
        visitors.setText("");
        cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview_light_background));
    }
}
