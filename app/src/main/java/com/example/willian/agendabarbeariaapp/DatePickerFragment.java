package com.example.willian.agendabarbeariaapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by Willian on 16/05/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


     EditText edData;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, year, month, dayofmonth);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

      edData = (EditText) getActivity().findViewById(R.id.edtData_cad);
        edData.setText(day + "/" +(month +1) + "/" +year);

    }



}




