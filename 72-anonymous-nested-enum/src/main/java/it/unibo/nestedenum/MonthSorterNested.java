package it.unibo.nestedenum;

import java.util.Comparator;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {

    private enum Month{
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        private final int days;

        Month(final int days){
            this.days = days;
        }

        public static Month fromString(String s){
            String tmp = s.toUpperCase();
            try{
                return Month.valueOf(tmp);
            }  catch (IllegalArgumentException e) {

                Month match = null;
                for(Month m: values()){
                    if(m.toString().toUpperCase().contains(tmp)){
                        if(match != null){
                            throw new IllegalArgumentException("Not a legal month: " + s);
                        }
                        match = m;
                    }
                }

                if(match == null){
                    throw new IllegalArgumentException("Not a legal month: " + s);
                }
                return match;
            }
        }

    }

    public static class SortByDate implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            int m1 = Month.fromString(o1).days;
            int m2 = Month.fromString(o2).days;
            return Integer.compare(m1, m2);
        }
        
    }

    public static class SortByMonthOrder implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            Month m1 = Month.fromString(o1);
            Month m2 = Month.fromString(o2);
            return m1.compareTo(m2);
        }
        
    }

    @Override
    public Comparator<String> sortByDays() {
        return new SortByDate();
    }

    @Override
    public Comparator<String> sortByOrder() {
        return new SortByMonthOrder();
    }
}
