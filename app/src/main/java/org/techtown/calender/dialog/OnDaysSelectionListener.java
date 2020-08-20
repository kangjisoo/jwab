package org.techtown.calender.dialog;

import org.techtown.calender.model.Day;

import java.util.List;

public interface OnDaysSelectionListener {
    void onDaysSelected(List<Day> selectedDays);
}
