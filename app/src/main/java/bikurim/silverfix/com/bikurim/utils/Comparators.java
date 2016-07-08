package bikurim.silverfix.com.bikurim.utils;

import java.util.Comparator;

import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by David on 30/06/2016.
 */
public abstract class Comparators {

    public static final NameComparator NAME_COMPARATOR = new NameComparator();
    public static final TimeComparator TIME_COMPARATOR = new TimeComparator();
    public static final VisitorsComparator VISITORS_COMPARATOR = new VisitorsComparator();
    private static class NameComparator implements Comparator<Family> {
        @Override
        public int compare(Family lhs, Family rhs) {
            String name1 = lhs.lastName.toUpperCase();
            String name2 = rhs.lastName.toUpperCase();
            return name1.compareTo(name2);
        }
    }
    private static class TimeComparator implements Comparator<Family> {
        @Override
        public int compare(Family lhs, Family rhs) {
            int comparage = (int) rhs.timeLeft;
            return (int) lhs.timeLeft - comparage;
        }
    }
    private static class VisitorsComparator implements  Comparator<Family> {

        @Override
        public int compare(Family lhs, Family rhs) {
            int comparage = rhs.visitorsNum;
            return lhs.visitorsNum - comparage;
        }
    }
}
