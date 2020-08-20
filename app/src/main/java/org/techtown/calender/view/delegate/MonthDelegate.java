package org.techtown.calender.view.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.calender.adapter.DaysAdapter;
import org.techtown.calender.adapter.viewholder.MonthHolder;
import org.techtown.calender.model.Month;
import org.techtown.calender.settings.SettingsManager;
import org.techtown.loginactivity.R;


public class MonthDelegate {

    private SettingsManager appearanceModel;

    public MonthDelegate(SettingsManager appearanceModel) {
        this.appearanceModel = appearanceModel;
    }

    public MonthHolder onCreateMonthHolder(DaysAdapter adapter, ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.view_month, parent, false);
        final MonthHolder holder = new MonthHolder(view, appearanceModel);
        holder.setDayAdapter(adapter);
        return holder;
    }

    public void onBindMonthHolder(Month month, MonthHolder holder, int position) {
        holder.bind(month);
    }
}
