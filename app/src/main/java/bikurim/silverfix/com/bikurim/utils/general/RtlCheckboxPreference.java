package bikurim.silverfix.com.bikurim.utils.general;

import android.content.Context;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by דודו on 15/07/2016.
 */
public class RtlCheckboxPreference extends CheckBoxPreference {

    public RtlCheckboxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return view;
    }
}
