package bikurim.silverfix.com.bikurim.utils.general;

import java.util.Comparator;

import bikurim.silverfix.com.bikurim.models.Family;

/**
 * Created by David on 30/06/2016.
 * A container class for subclasses that implements Comparator<Family>.
 * Each subclass implements Comparator on its logic. For instance,
 * the TimeComparator compares the time_left variable and sorts the list
 * when the lowest value is at the top of the list
 *
 * @author David Lasry
 *
 */
public abstract class Comparators {

    public static final NameComparator NAME_COMPARATOR = new NameComparator();
    public static final TimeComparator TIME_COMPARATOR = new TimeComparator();
    public static final VisitorsComparator VISITORS_COMPARATOR = new VisitorsComparator();
    public static final DateComparator DATE_COMPARATOR = new DateComparator();
    private static class NameComparator implements Comparator<Family> {
        @Override
        public int compare(Family lhs, Family rhs) {
            String name1 = lhs.name.toUpperCase();
            String name2 = rhs.name.toUpperCase();
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

    private static class DateComparator implements Comparator<Family> {

        @Override
        public int compare(Family lhs, Family rhs) {
            int comperage = (int) rhs.date;
            return (int) lhs.date - comperage;
        }
    }
}
