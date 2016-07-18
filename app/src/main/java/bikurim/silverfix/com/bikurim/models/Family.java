package bikurim.silverfix.com.bikurim.models;

/**
 * Created by David on 06/06/2016.
 * A model class for the family object. Holds data regarding the name, num of visitors, duration of the visit
 * and the exact date in milliseconds which the visit started
 */
public class Family implements Comparable<Family>{

    public String name = "";
    public int visitorsNum;
    public long date = 0;
    public long timeLeft = 0L;
    public long visitLength = 0L;

    private long whenInMillis;

    // A default constructor
    public Family() {

    }

    // The official constructor of the class
    public Family(String lastName, int visitorsNum, long timeLeft, long date, long visitLength) {
        this.name = lastName;
        this.visitorsNum = visitorsNum;
        this.timeLeft = timeLeft;
        this.date = date;
        this.visitLength = visitLength;

        whenInMillis = System.currentTimeMillis() + timeLeft;
    }


    public long whenInMillis() {
        return whenInMillis;
    }
    // Implementation of compareTo function in order for the list of families to be sortable

    @Override
    public int compareTo(Family another) {
        int comperage = (int) another.timeLeft;
        return (int) this.timeLeft - comperage;
    }
}
