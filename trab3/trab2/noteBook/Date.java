package trab2.noteBook;

import java.util.Calendar;

public class Date implements Comparable<Date> {
    private final int day, month, year;



    public Date(int d, int m, int y) {
        day = d;
        month = m;
        year = y;
    }


    public Date(String s) {
        int i1 = s.indexOf('-');
        day = Integer.parseInt(s.substring(0, i1));
        int i2 = s.lastIndexOf('-');
        month = Integer.parseInt(s.substring(i1 + 1, i2));
        year = Integer.parseInt(s.substring(i2 + 1));
    }


    public Date() {
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
    }


    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int compareTo(Date d) {
        int res = year - d.year;
        if (res == 0) {
            res = month - d.month;
            if (res == 0) return day - d.day;
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Date)) return false;
        Date d = (Date) o;
        return day == d.day && month == d.month && year == d.year;
    }


    @Override
    public int hashCode() {
        return  (year*100+month)*100+day;
    }

    @Override
    public String toString() {
        return String.format("%02d-%02d-%02d", day, month, year);
    }

    public Date nextDate(int n) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day + n);
        return new Date(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
    }



    public static int getDays(Date d1, Date d2) {
        Calendar c = Calendar.getInstance();
        c.set(d1.year, d1.month - 1, d1.day);
        long t1 = c.getTimeInMillis();
        c.set(d2.year, d2.month - 1, d2.day);
        long t2 = c.getTimeInMillis();
        return (int) ((t1 - t2) / 1000 / 60 / 60 / 24);
    }

}

