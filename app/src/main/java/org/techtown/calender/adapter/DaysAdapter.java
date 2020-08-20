package org.techtown.calender.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.calender.adapter.viewholder.DayHolder;
import org.techtown.calender.adapter.viewholder.DayOfWeekHolder;
import org.techtown.calender.adapter.viewholder.OtherDayHolder;
import org.techtown.calender.model.Day;
import org.techtown.calender.model.Month;
import org.techtown.calender.utils.Constants;
import org.techtown.calender.view.CalendarView;
import org.techtown.calender.view.ItemViewType;
import org.techtown.calender.view.delegate.DayDelegate;
import org.techtown.calender.view.delegate.DayOfWeekDelegate;
import org.techtown.calender.view.delegate.OtherDayDelegate;

public class DaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Month month;
    private DayOfWeekDelegate dayOfWeekDelegate;
    private DayDelegate dayDelegate;
    private OtherDayDelegate otherDayDelegate;
    private CalendarView calendarView;

    private DaysAdapter(Month month,
                        DayOfWeekDelegate dayOfWeekDelegate,
                        DayDelegate dayDelegate,
                        OtherDayDelegate otherDayDelegate,
                        CalendarView calendarView) {
        setHasStableIds(false);
        this.month = month;
        this.dayOfWeekDelegate = dayOfWeekDelegate;
        this.dayDelegate = dayDelegate;
        this.otherDayDelegate = otherDayDelegate;
        this.calendarView = calendarView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < Constants.DAYS_IN_WEEK && calendarView.isShowDaysOfWeek()) {
            return ItemViewType.DAY_OF_WEEK;
        }
        if (month.getDays().get(position).isBelongToMonth()) {
            return ItemViewType.MONTH_DAY;
        } else {
            return ItemViewType.OTHER_MONTH_DAY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemViewType.DAY_OF_WEEK:
                return dayOfWeekDelegate.onCreateDayHolder(parent, viewType);
            case ItemViewType.MONTH_DAY:
                return dayDelegate.onCreateDayHolder(parent, viewType);
            case ItemViewType.OTHER_MONTH_DAY:
                return otherDayDelegate.onCreateDayHolder(parent, viewType);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Day day = month.getDays().get(position);
        switch (holder.getItemViewType()) {
            case ItemViewType.DAY_OF_WEEK:
                dayOfWeekDelegate.onBindDayHolder(day, (DayOfWeekHolder) holder, position);
                break;
            case ItemViewType.OTHER_MONTH_DAY:
                otherDayDelegate.onBindDayHolder(day, (OtherDayHolder) holder, position);
                break;
            case ItemViewType.MONTH_DAY:
                dayDelegate.onBindDayHolder(this, day, (DayHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return month == null ? 0 : month.getDays().size();
    }

    public void setMonth(Month month) {
        this.month = month;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return month.getDays().get(position).getCalendar().getTimeInMillis();
    }

    public static class DaysAdapterBuilder {

        private Month month;
        private DayOfWeekDelegate dayOfWeekDelegate;
        private DayDelegate dayDelegate;
        private OtherDayDelegate anotherDayDelegate;
        private CalendarView calendarView;

        public DaysAdapterBuilder setMonth(Month month) {
            this.month = month;
            return this;
        }

        public DaysAdapterBuilder setDayOfWeekDelegate(DayOfWeekDelegate dayOfWeekDelegate) {
            this.dayOfWeekDelegate = dayOfWeekDelegate;
            return this;
        }

        public DaysAdapterBuilder setDayDelegate(DayDelegate dayDelegate) {
            this.dayDelegate = dayDelegate;
            return this;
        }

        public DaysAdapterBuilder setOtherDayDelegate(OtherDayDelegate anotherDayDelegate) {
            this.anotherDayDelegate = anotherDayDelegate;
            return this;
        }

        public DaysAdapterBuilder setCalendarView(CalendarView calendarView) {
            this.calendarView = calendarView;
            return this;
        }

        public DaysAdapter createDaysAdapter() {
            return new DaysAdapter(month, dayOfWeekDelegate, dayDelegate, anotherDayDelegate, calendarView);
        }
    }
}
