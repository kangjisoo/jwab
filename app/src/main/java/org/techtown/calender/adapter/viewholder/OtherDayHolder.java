package org.techtown.calender.adapter.viewholder;

import android.view.View;
import android.widget.TextView;


import org.techtown.calender.model.Day;
import org.techtown.calender.view.CalendarView;
import org.techtown.loginactivity.R;

public class OtherDayHolder extends BaseDayHolder {

    public OtherDayHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        tvDay = (TextView) itemView.findViewById(R.id.tv_day_number);
    }

    public void bind(Day day) {
        tvDay.setText(String.valueOf(day.getDayNumber()));
        tvDay.setTextColor(calendarView.getOtherDayTextColor());
    }
}
