package bikurim.silverfix.com.bikurim.adapters.holders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by David on 07/07/2016.
 *
 * The official view holder of the adapter. Holds references to the relevant views inside the layout
 */

public class FamilyViewHolder extends GenericViewHolder {
    public boolean isBackgroundChanged;

    public CardView cardView;
    public TextView personLname;
    public TextView timeLeft;
    public TextView visitors;
    public ImageView clock;
    public ImageButton remove;

    public Family family;

    private ColorStateList colorStateList;

    public FamilyViewHolder(View itemView, Context context) {
        super(itemView, context);
        // isBackgroundChanged represents whether the holder is under 60 seconds or not
        isBackgroundChanged = false;

        // Getting the references for the UI components
        cardView = (CardView) itemView.findViewById(R.id.cv);
        personLname = (TextView) itemView.findViewById(R.id.person_Lname);
        visitors = (TextView) itemView.findViewById(R.id.visitors);
        timeLeft = (TextView) itemView.findViewById(R.id.person_timeLeft);
        clock = (ImageView) itemView.findViewById(R.id.clock);
        remove = (ImageButton) itemView.findViewById(R.id.time_up);

        // Sets a reference to the old colors of the text view
        colorStateList = timeLeft.getTextColors();
    }

    @Override
    public void bindData(Family family) {
        this.family = family;
        personLname.setText(family.lastName);
        visitors.setText("מבקרים:  " + family.visitorsNum);
    }

    @Override
    public void reset() {
        family.timeLeft = 0;
        personLname.setText("");
        visitors.setText("");
        timeLeft.setText("");
        cardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview_light_background));

        isBackgroundChanged = false;
    }

    public ColorStateList getOriginalColors() {
        return colorStateList;
    }

}
