package com.example.notebook.Util;

import java.util.Calendar;

public class CalendarDate {
    public CalendarDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return "CalendarDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

    public CalendarDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int year;
    public int month;
    public int day;

    public CalendarDate dayOffSet(int offset) {
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,day);
        c.add(Calendar.DAY_OF_MONTH,offset);
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;
        day=c.get(Calendar.DAY_OF_MONTH);
        return new CalendarDate(year,month,day);
    }

    public CalendarDate monthOffSet(int offset) {
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,day);
        c.add(Calendar.MONTH,offset);
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;
        day=c.get(Calendar.DAY_OF_MONTH);
        return new CalendarDate(year,month,day);
    }

    /**
     * @param month true month , needless to minus 1
     * @param field {@link Calendar#getActualMaximum(int)}
     * @return
     */
    public static int getActualMaximum(
            int year, int month, int date, int hourOfDay, int minute, int second,
            int field) {
        Calendar calendar = Calendar.getInstance();
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

    public Calendar toCalendar(){
        Calendar c=Calendar.getInstance();
        c.set(year,month-1,day);
        return c;
    }

    public static void main(String[] args) {
        CalendarDate calendarDate=new CalendarDate();
        System.out.println(calendarDate.toString());
        for(int i=0;i<100;i++){
            calendarDate.dayOffSet(1);
            System.out.println(calendarDate.toString());
        }

    }
}
