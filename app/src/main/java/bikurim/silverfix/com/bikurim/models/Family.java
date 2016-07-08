package bikurim.silverfix.com.bikurim.models;

/**
 * Created by David on 06/06/2016.
 * A model class for the family object. Holds data regarding the name, num of visitors, duration of the visit
 * and the exact date in milliseconds which the visit started
 */
public class Family implements Comparable<Family>{

    public String lastName = "";
    public int visitorsNum;
    public long date = 0;
    public long whenInMillis = 0L;
    public long timeLeft = 0L;

    // A default constructor
    public Family() {

    }

    // The official constructor of the class
    public Family(String lastName, int visitorsNum, long whenInMillis, long timeLeft, long date) {
        this.lastName = lastName;
        this.visitorsNum = visitorsNum;
        this.whenInMillis = whenInMillis;
        this.timeLeft = timeLeft;
        this.date = date;
    }

    // Implementation of compareTo function in order for the list of families to be sortable

    @Override
    public int compareTo(Family another) {
        int comperage = (int) another.whenInMillis;
        return (int) this.whenInMillis - comperage;
    }
}
