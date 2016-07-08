package bikurim.silverfix.com.bikurim.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 04/07/2016.
 * A utility class for several generic uses
 */
public class Utils {

    /* Animations useful methods */

    /* Starts a slide in animation for a given Card View */
    public static  void setSlideAnimation(Context context, View viewToAnimate, int position,
                                          int lastPosition, boolean isEndAnimation) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        if (!isEndAnimation) {
            if (position > lastPosition) {
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
                return;
            }
        }
        viewToAnimate.startAnimation(animation);
    }

    public static void setFadeAnimation(View viewToAnimate) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 1f, .1f);
        fadeOut.setDuration(750);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewToAnimate, "alpha", .1f, 1f);
        fadeIn.setDuration(750);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }

    public static String updateFormatTime(long millisUntilFinished) {
        String result = "";
        String minutes, seconds;
        minutes = "" + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        long secondsDiff = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
        seconds = "" + secondsDiff;
        if (minutes.length() == 1)
            minutes = "0" + minutes;
        if (seconds.length() == 1)
            seconds = "0" + seconds;
        result = minutes + ":" + seconds;
        return result;
    }

    public static long persistDate(Date date) {
        if(date != null)
            return date.getTime();
        return 0;
    }

    public static Date loadDate(Cursor cursor, int index) {
        if(cursor.isNull(index))
            return null;
        return new Date(cursor.getLong(index));
    }
    public static boolean checkInput(String str) {
        if (str.length() <= 1 || str == null || containsDigits(str))
            return false;
        return true;
    }

    public static boolean containsDigits(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}
