package org.techtown.calender.selection.criteria.month;

import androidx.annotation.NonNull;



import org.techtown.calender.model.Day;
import org.techtown.calender.selection.BaseSelectionManager;
import org.techtown.calender.utils.DateUtils;

import java.util.Calendar;

public class NextMonthCriteria extends BaseMonthCriteria {

    private long currentTimeInMillis;

    public NextMonthCriteria() {
        currentTimeInMillis = System.currentTimeMillis();
    }

    @Override
    protected int getMonth() {
        Calendar calendar = DateUtils.getCalendar(currentTimeInMillis);
        calendar.add(Calendar.MONTH, 1);
        return calendar.get(Calendar.MONTH);
    }

    @Override
    protected int getYear() {
        Calendar calendar = DateUtils.getCalendar(currentTimeInMillis);
        calendar.add(Calendar.MONTH, 1);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Created by leonardo2204 on 06/10/17.
     */

    public static class NoneSelectionManager extends BaseSelectionManager {

        @Override
        public void toggleDay(@NonNull Day day) {

        }

        @Override
        public boolean isDaySelected(@NonNull Day day) {
            return false;
        }

        @Override
        public void clearSelections() {

        }
    }
}
