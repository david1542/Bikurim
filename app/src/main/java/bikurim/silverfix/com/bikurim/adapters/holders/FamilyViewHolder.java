package bikurim.silverfix.com.bikurim.adapters.holders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.models.Family;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 07/07/2016.
 *
 * The official view holder of the adapter. Holds references to the relevant views inside the layout
 */

public class FamilyViewHolder extends GenericViewHolder {
    public boolean isStrokeChanged, isHolderAdded;

    public CardView cardView;
    public RelativeLayout frame;
    public TextView name;
    public TextView timeLeft;
    public TextView visitors;


    public Family family;

    private ColorStateList colorStateList;

    public FamilyViewHolder(Context context, View v) {
        super(v, context);
        // isStrokeChanged represents whether the holder is under 60 seconds or not
        isStrokeChanged = false;
        isHolderAdded = false;

        // Getting the references for the UI components
        cardView = (CardView) v.findViewById(R.id.cv);
        frame = (RelativeLayout) v.findViewById(R.id.card_frame);
        name = (TextView) v.findViewById(R.id.person_Lname);
        visitors = (TextView) v.findViewById(R.id.visitors);
        timeLeft = (TextView) v.findViewById(R.id.person_timeLeft);

        // Sets a reference to the old colors of the text view
        colorStateList = timeLeft.getTextColors();
    }

    @Override
    public void bindData(Family family) {
        this.family = family;
        name.setText(Utils.formatNameString(family.name));
        visitors.setText(" מבקרים: "+family.visitorsNum);
    }

    @Override
    public void reset() {
        family = null;
        name.setText("");
        visitors.setText("");
        timeLeft.setText("");

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.family_item_blue_stroke);

        this.frame.clearAnimation();
        this.frame.setAnimation(null);

        this.frame.setBackground(drawable);

        isStrokeChanged = false;
        isHolderAdded = false;
    }

    public ColorStateList getOriginalColors() {
        return colorStateList;
    }

}
