package bikurim.silverfix.com.bikurim.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.R;
import bikurim.silverfix.com.bikurim.utils.Utils;

/**
 * Created by David on 09/06/2016.
 * This class represents the add dialog that occurs when the user decides to add a new
 * family to the list
 */
public class AddFragment extends DialogFragment {

    private CheckBox checkBox;
    private EditText familyName;
    private CheckBox extraTime;
    private NumberPicker visitors;

    public static AddFragment newInstance()
    {
        AddFragment frag = new AddFragment();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        familyName = (EditText) view.findViewById(R.id.family_name);
        extraTime = (CheckBox) view.findViewById(R.id.check_box);
        extraTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(getContext(), "נוספו 15 דקות הארכת זמן", Toast.LENGTH_SHORT).show();
            }
        });
        visitors = (NumberPicker) view.findViewById(R.id.visitors_number);
        visitors.setMaxValue(20);
        visitors.setMinValue(1);
        visitors.setWrapSelectorWheel(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_fragment, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_message)
                .setPositiveButton(R.string.proceed_add_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = familyName.getText().toString();
                        if(Utils.checkInput(input))
                            saveState();
                        else
                            Toast.makeText(getActivity(), "שם משפחה לא תקין. אנא הכנס שוב", Toast.LENGTH_SHORT).show();
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
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void saveState() {
        // Retrieving the values from the views in the Dialog
        String name = familyName.getText().toString();
        boolean isExtraTime = extraTime.isChecked();
        int visitorsNum = visitors.getValue();

        // Calculating the current whenInMillis and stores it as a string
        long dateTime;
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+3"));

        dateTime = Utils.persistDate(c.getTime());

        // Initiating a new intent with the above data as extras
        Intent intent = new Intent();
        intent.putExtra(Constants.Intent.FAMILY_NAME, name);
        intent.putExtra(Constants.Intent.IS_CHECKED, isExtraTime);
        intent.putExtra(Constants.Intent.DATE_TIME, dateTime);
        intent.putExtra(Constants.Intent.VISITORS_NUMBER, visitorsNum);
        intent.setAction(Constants.Intent.SAVE_ACTION);

        // Finally, initializing a new broadcast to the LocalBroadcastReceiver
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
