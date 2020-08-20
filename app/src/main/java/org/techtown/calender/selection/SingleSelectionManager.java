package org.techtown.calender.selection;



import androidx.annotation.NonNull;

import org.techtown.calender.model.Day;


public class SingleSelectionManager extends BaseSelectionManager {

    private Day day;

    public SingleSelectionManager(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    @Override
    public void toggleDay(@NonNull Day day) {
        this.day = day;
        onDaySelectedListener.onDaySelected();
    }

    @Override
    public boolean isDaySelected(@NonNull Day day) {
        return day.equals(this.day);
    }

    @Override
    public void clearSelections() {
        day = null;
    }
}
