package bikurim.silverfix.com.bikurim.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bikurim.silverfix.com.bikurim.Constants;
import bikurim.silverfix.com.bikurim.R;

/**
 * Created by Dudu on 09/06/2016.
 */
public class AddFragment extends DialogFragment {

    private CheckBox checkBox;
    private EditText familyName;
    private CheckBox extraTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    Toast.makeText(getActivity(), "נוספו 15 דקות הארכת זמן", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.add_fragment);
        builder.setMessage(R.string.add_message)
                .setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveState();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void saveState() {

        String name = familyName.getText().toString();
        boolean isExtraTime = extraTime.isChecked();
        String dateTime;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        dateTime = dateformat.format(c.getTime());
        Intent intent = new Intent();
        intent.putExtra(Constants.Intent.FAMILY_NAME, name);
        intent.putExtra(Constants.Intent.IS_CHECKED, isExtraTime);
        intent.putExtra(Constants.Intent.DATE_TIME, dateTime);
        intent.setAction(Constants.Intent.SAVE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
