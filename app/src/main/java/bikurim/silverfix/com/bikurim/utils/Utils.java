package bikurim.silverfix.com.bikurim.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.database.FamiliesTablesContract;
import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by David on 04/07/2016.
 * A utility class for several generic uses
 */
public class Utils {

    public static ArrayList<Family> toArrayList(Cursor cursor) {
        ArrayList<Family> families = new ArrayList<>();
        Family family = new Family();

        if(cursor != null) {
            String name;
            int visitors;
            long date, lengthVisit;
            if(cursor.moveToFirst()) {
                do {
                    name = cursor.getString(cursor.getColumnIndexOrThrow(FamiliesTablesContract.NAME_COLUMN));
                    visitors = cursor.getInt(cursor.getColumnIndexOrThrow(FamiliesTablesContract.VISITORS_COLUMNS));
                    date = cursor.getLong(cursor.getColumnIndexOrThrow(FamiliesTablesContract.DATE_COLUMN));
                    lengthVisit = cursor.getLong(cursor.getColumnIndexOrThrow(FamiliesTablesContract.LENGTH_COLUMN));

                    family = new Family(name, visitors, 0, date, lengthVisit);
                    families.add(family);
                } while (cursor.moveToNext());
            }
        }

        return families;
    }

    /* Vibration Method */
    public static void vibrate(Context context, long duration) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }
    /* Animations useful methods */

    public static void setScaleAnimation(View viewToAnimate, int position, int lastPosition) {
        if(position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(Constants.Values.SCALE_ANIM_DURATION);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
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
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 1f, .3f);
        fadeOut.setDuration(750);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewToAnimate, "alpha", .3f, 1f);
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

    /* This methods takes a string and simply divides it by spaces, so the first whitespace
    * would be the divider between the first name and the last name */
    public static String formatNameString(String name) {
        String[] splitName = name.split("\\s+");
        String result = " " + splitName[0] + "\n\r";

        for(int i = 1; i < splitName.length; i++)
            result += splitName[i] + " ";

        return result;
    }

    public static String getLastName(String name) {
        String[] splitName = name.split("\\s+");
        String result = "";
        for(int i = 1; i < splitName.length; i++)
            result += splitName[i] + " ";

        return result;
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

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
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
        if (str.length() <= 1 || str == null || containsDigits(str) || !isFullName(str))
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

    public static boolean isFullName(String name) {
        return name.contains(" ");
    }
}
