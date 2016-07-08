package bikurim.silverfix.com.bikurim;

/**
 * Created by David on 23/06/2016.
 */
public class Constants {
    public static class Intent {
        // Intent Filters
        public static final String SORT_ACTION = "SORT_ACTION";
        public static final String SAVE_ACTION = "SAVE_STATE";
        public static final String WAKE_ACTION = "WAKE_DEVICE";

        // Bundle extras constants
        public static final String REMINDER_ID = "reminderID";
        public static final String IS_CHECKED = "isChecked";
        public static final String FAMILY_NAME = "familyName";
        public static final String VISITORS_NUMBER = "visitorsNumber";
        public static final String DATE_TIME = "dateTime";
        public static final String ARCHIVE_DATE = "archiveDate";

        // Time Constants
        public static final long DEFAULT_TIME = 80000;
        public static final long EXTRA_TIME = 100000;

        public static final long MINIMUM_DATE = 157784760000l;
    }

    public static class Tags {
        public static final String ADD_TAG = "AddDialog";
        public static final String DATE_TAG = "DateDialog";
        public static final String DRAWER_TAG = "Drawer";
        public static final String SHARED_TAG = "BikurimSh";
        /*-----------------------------------------------*/
        public static final String SHARED_NAME_TAG = "Name";

        /* View Holders identifiers */

        public static final int FAMILY_VIEW = 1;
        public static final int TIME_UP_VIEW = 2;

        public static final int[] VIEW_TYPES = {FAMILY_VIEW, TIME_UP_VIEW};
    }

    public static class Names {
        public static final String REMINDER_SERVICE = "ReminderService";
    }

    public static class Values {
        public static final long LIMIT_TO_DAY = 86400000;

        public static final long TIME_INTERVAL = 1000;

        public static final long ALERT_TIME = 60000;

        public static final int ARCHIVE_LOADER =  0;

        public static final long FADE_DURATION = 3;
    }
}
