package mx.iteso.petgo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import mx.iteso.petgo.databinding.ActivityScheduleTripBinding;

public class ActivityScheduleTrip extends AppCompatActivity implements View.OnClickListener {
    private ActivityScheduleTripBinding mBinding;
    private static final String ZERO = "0";
    private static final String COLON = ":";
    private static final String SLASH = "/";

    public final Calendar calendar = Calendar.getInstance();
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int month = calendar.get(Calendar.MONTH);
    final int year = calendar.get(Calendar.YEAR);

    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_trip);

        mBinding.ibDateScheduleTrip.setOnClickListener(this);
        mBinding.ibTimeScheduleTrip.setOnClickListener(this);
        mBinding.btnAcceptScheduleTrip.setOnClickListener(this);
        mBinding.btnCancelScheduleTrip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_date_schedule_trip:
                getDate();
                break;
            case R.id.ib_time_schedule_trip:
                getHour();
                break;
            case R.id.btn_accept_schedule_trip:
                scheduleTrip();
                break;
            case R.id.btn_cancel_schedule_trip:
                cancel();
                break;
        }
    }

    private void getDate() {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int actualMonth = month + 1;

                String formattedDay = (dayOfMonth < 10)? ZERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String formattedMonth = (actualMonth < 10) ? ZERO + String.valueOf(actualMonth) : String.valueOf(actualMonth);
                String formattedDate = formattedDay + SLASH + formattedMonth + SLASH + year;

                mBinding.etDateScheduleTrip.setText(formattedDate);
            }
        }, year, month, day);

        datePicker.show();
    }

    private void getHour() {
        TimePickerDialog hourPicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedHour = (hourOfDay < 9) ? String.valueOf(ZERO + hourOfDay) : String.valueOf(hourOfDay);
                String formattedMinute = (minute < 9) ? String.valueOf(ZERO + minute) : String.valueOf(minute);

                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                String formattedTime = formattedHour + COLON + formattedMinute + " " + AM_PM;

                mBinding.etTimeScheduleTrip.setText(formattedTime);
            }
        }, hour, minute, false);

        hourPicker.show();
    }

    private void scheduleTrip() {
        Intent intent = new Intent(this, ActivityMain.class);
        // TODO add trip
        setResult(RESULT_OK, intent);
        finish();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
