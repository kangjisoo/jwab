package org.techtown.calender.view.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.calender.adapter.viewholder.OtherDayHolder;
import org.techtown.calender.model.Day;
import org.techtown.calender.view.CalendarView;
import org.techtown.loginactivity.R;


public class OtherDayDelegate {

    private CalendarView calendarView;

    public OtherDayDelegate(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public OtherDayHolder onCreateDayHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_other_day, parent, false);
        return new OtherDayHolder(view, calendarView);
    }

    public void onBindDayHolder(Day day, OtherDayHolder holder, int position) {
        holder.bind(day);
    }
}
