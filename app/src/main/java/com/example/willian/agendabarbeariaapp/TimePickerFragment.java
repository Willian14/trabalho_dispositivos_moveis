package com.example.willian.agendabarbeariaapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;


import java.util.Calendar;

/**
 * Created by Willian on 20/05/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private EditText edHora;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    edHora = (EditText) getActivity().findViewById(R.id.edtHora_cad);
        edHora.setText(hourOfDay + ":" + minute);
    }
}
