package com.example.notebook.Util;

import java.util.Calendar;

public class CalendarUtil2 {
    private static Calendar calendarInstance;

    public static Calendar getSingleInstance() {
        if (calendarInstance == null) {
            synchronized (CalendarUtil2.class) {
                if (calendarInstance == null) {
                    calendarInstance = Calendar.getInstance();
                }
            }
        }
        return calendarInstance;
    }

    /**
     * @param month true month , needless to minus 1
     * @param field {@link Calendar#getActualMaximum(int)}
     * @return
     */
    public static int getActualMaximum(
            int year, int month, int date, int hourOfDay, int minute, int second,
            int field) {
        Calendar calendar = getSingleInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return calendar.getActualMaximum(field);
    }

    /**
     * @param month true month , needless to minus 1
     * @param field {@link Calendar#getActualMaximum(int)}
     * @return
     */
    public static int getActualMaximum(
            int year, int month,
            int field) {
        return getActualMaximum(year, month, 0, 0, 0, 0, field);
    }

    public static void main(String[] args) {
        for(int i=1;i<12;i++){
            int x = getActualMaximum(2020, i,  Calendar.DATE);
            System.out.println(i+":"+x);
        }
    }
}
