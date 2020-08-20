package org.techtown.calender.adapter.viewholder;

import android.view.View;
import android.widget.TextView;


import org.techtown.calender.model.Day;
import org.techtown.calender.utils.Constants;
import org.techtown.calender.view.CalendarView;
import org.techtown.loginactivity.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayOfWeekHolder extends BaseDayHolder {

    private SimpleDateFormat mDayOfWeekFormatter;

    public DayOfWeekHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        tvDay = (TextView) itemView.findViewById(R.id.tv_day_name);
        mDayOfWeekFormatter = new SimpleDateFormat(Constants.DAY_NAME_FORMAT, Locale.getDefault());
    }

    public void bind(Day day) {
        tvDay.setText(mDayOfWeekFormatter.format(day.getCalendar().getTime()));
        tvDay.setTextColor(calendarView.getWeekDayTitleTextColor());
    }
}