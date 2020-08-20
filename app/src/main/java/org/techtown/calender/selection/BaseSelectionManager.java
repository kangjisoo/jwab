package org.techtown.calender.selection;



import androidx.annotation.NonNull;

import org.techtown.calender.model.Day;


public abstract class BaseSelectionManager {

    protected OnDaySelectedListener onDaySelectedListener;

    public abstract void toggleDay(@NonNull Day day);

    public abstract boolean isDaySelected(@NonNull Day day);

    public abstract void clearSelections();

    public BaseSelectionManager() {
    }
}
