package org.techtown.calender.settings.selection;


import org.techtown.calender.utils.SelectionType;

public interface SelectionInterface {

    @SelectionType
    int getSelectionType();

    void setSelectionType(@SelectionType int selectionType);
}
