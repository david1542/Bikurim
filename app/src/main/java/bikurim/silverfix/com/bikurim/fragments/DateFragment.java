package bikurim.silverfix.com.bikurim.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 05/07/2016.
 * A date picker dialog fragment that appears on the archive activity
 */
public class DateFragment extends DialogFragment {

    private DatePicker picker;
    public static DateFragment newInstance() {
        DateFragment fragment = new DateFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int year = Calendar.YEAR;
        int month = Calendar.MONTH;
        int day = Calendar.DAY_OF_MONTH;
        picker = (DatePicker) view.findViewById(R.id.date_picker);

        picker.setMaxDate(System.currentTimeMillis());
        picker.setMinDate(System.currentTimeMillis() - Constants.Intent.MINIMUM_DATE);
        picker.setSpinnersShown(true);
        picker.init(year, month, day, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.date_fragment, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.date_picker_message)
                .setPositiveButton(R.string.proceed_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setDate();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_add_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        onViewCreated(dialogView, savedInstanceState);
        builder.setView(dialogView);
        return builder.create();
    }

    private long getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return calendar.getTime().getTime();
    }

    private void setDate() {
        Intent intent = new Intent();
        intent.putExtra(Constants.Intent.ARCHIVE_DATE, getDate());
        intent.setAction(Constants.Intent.SORT_ACTION);

        // Sending a broadcast to the activity
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
