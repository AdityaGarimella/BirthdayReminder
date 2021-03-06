package com.aditya.birthdayreminder.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.aditya.birthdayreminder.R;
import com.aditya.birthdayreminder.model.BirthdayBeans;
import com.aditya.birthdayreminder.sharedpreference.SharedPreferencesManager;
import com.aditya.birthdayreminder.sqlite.DatabaseHandler;
import com.aditya.birthdayreminder.views.activities.BirthdaysListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Aditya G on 18-12-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    //Variables
    private Intent mIntentNotificationIntent;
    private PendingIntent mPendingIntent;
    private Context mContext;
    private List<BirthdayBeans> mListBirthdayBeans;
    private List<BirthdayBeans> mListTodaysBirthdayBeans;
    private int mStoredYear, mStoredMonth, mStoredDayOfMonth, mTodaysBirthdaysCount;
    private String mNotificationText = "";

    //Objects
    private TaskStackBuilder mObjectTaskStackBuilder;
    private NotificationCompat.Builder mObjectNotificationCompatBuilder;
    private Notification mObjectNotification;
    private NotificationManager mObjectNotificationManager;
    private DatabaseHandler mObjectDatabaseHandler;
    private Calendar mObjectCalendarStoredDate, mObjectCalendarTodaysDate;


    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

//        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
//            //Set notification time after system reboot
//            setNotificationTime();
//        }

        setNotificationTime();

        createIntentTappedNotification();
        instantiateObjects();
        getListOfBirthdays();
        getListOfTodaysBirthdays();
        getCountOfTodaysBirthdays();

        if (mTodaysBirthdaysCount > 0) {
            createNotificationText();
            createNotification();
        }

    }

    void setNotificationTime() {

        Calendar mObjectCalendar = Calendar.getInstance();
        Calendar mObjectCalendarCurrent = Calendar.getInstance();
        AlarmManager mObjectAlarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);


        mObjectCalendar.set(Calendar.HOUR_OF_DAY, SharedPreferencesManager.getNotificationTimeHour(mContext));
        mObjectCalendar.set(Calendar.MINUTE, SharedPreferencesManager.getNotificationTimeMinute(mContext));
        mObjectCalendar.set(Calendar.SECOND, 0);
        mObjectCalendar.set(Calendar.MILLISECOND, 0);

        if (mObjectCalendar.before(mObjectCalendarCurrent))
            mObjectCalendar.add(Calendar.DATE, 1);

        Intent intentThisToAlarmReceiver = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntentBroadcast = PendingIntent.getBroadcast(mContext, 100, intentThisToAlarmReceiver,
                PendingIntent.FLAG_UPDATE_CURRENT);


//        mObjectAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mObjectCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentBroadcast);

//        mObjectAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mObjectCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentBroadcast);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mObjectAlarmManager.setExact(AlarmManager.RTC_WAKEUP, mObjectCalendar.getTimeInMillis(), pendingIntentBroadcast);
        } else {
            mObjectAlarmManager.set(AlarmManager.RTC_WAKEUP, mObjectCalendar.getTimeInMillis(), pendingIntentBroadcast);
        }
    }

    private void createNotificationText() {

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) == 0 && mTodaysBirthdaysCount == 1) {
            mNotificationText = mTodaysBirthdaysCount + " person has birthday today";
        }

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) == 0 && mTodaysBirthdaysCount > 1) {
            mNotificationText = mTodaysBirthdaysCount + " persons have birthdays today";
        }

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) == 1 && mTodaysBirthdaysCount == 1) {
            mNotificationText = mTodaysBirthdaysCount + " person has birthday in " + SharedPreferencesManager.
                    getNotificationDaysBefore(mContext) + " day";
        }

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) == 1 && mTodaysBirthdaysCount > 1) {
            mNotificationText = mTodaysBirthdaysCount + " persons have birthdays in " + SharedPreferencesManager.
                    getNotificationDaysBefore(mContext) + " day";
        }

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) > 1 && mTodaysBirthdaysCount == 1) {
            mNotificationText = mTodaysBirthdaysCount + " person has birthday in " + SharedPreferencesManager.
                    getNotificationDaysBefore(mContext) + " days";
        }

        if (SharedPreferencesManager.getNotificationDaysBefore(mContext) > 1 && mTodaysBirthdaysCount > 1) {
            mNotificationText = mTodaysBirthdaysCount + " persons have birthdays in " + SharedPreferencesManager.
                    getNotificationDaysBefore(mContext) + " days";
        }


    }

    void instantiateObjects() {
        mObjectDatabaseHandler = new DatabaseHandler(mContext);
        mObjectCalendarTodaysDate = Calendar.getInstance();
        mObjectCalendarStoredDate = Calendar.getInstance();
        mListTodaysBirthdayBeans = new ArrayList<BirthdayBeans>();
    }

    void getListOfBirthdays() {

        mListBirthdayBeans = mObjectDatabaseHandler.getAllBirthdays();
    }

    void getListOfTodaysBirthdays() {
        for (int i = 0; i < mListBirthdayBeans.size(); i++) {

            mStoredYear = mListBirthdayBeans.get(i).getmYear();
            mStoredMonth = mListBirthdayBeans.get(i).getmMonth();
            mStoredDayOfMonth = mListBirthdayBeans.get(i).getmDayOfMonth();

            mObjectCalendarStoredDate.set(Calendar.YEAR, mStoredYear);
            mObjectCalendarStoredDate.set(Calendar.MONTH, mStoredMonth);
            mObjectCalendarStoredDate.set(Calendar.DAY_OF_MONTH, mStoredDayOfMonth);


            if ((mObjectCalendarTodaysDate.get(Calendar.DAY_OF_YEAR) + SharedPreferencesManager.getNotificationDaysBefore(mContext)) == (mObjectCalendarStoredDate.get(Calendar.DAY_OF_YEAR))) {

                mListTodaysBirthdayBeans.add(mListBirthdayBeans.get(i));
            }

        }
    }

    void getCountOfTodaysBirthdays() {

        mTodaysBirthdaysCount = mListTodaysBirthdayBeans.size();
    }

    void createIntentTappedNotification() {
        //Creating the intent when tapped on notification
        mIntentNotificationIntent = new Intent(mContext, BirthdaysListActivity.class);

        //Creating the Taskbuilder
        mObjectTaskStackBuilder = TaskStackBuilder.create(mContext);
        mObjectTaskStackBuilder.addParentStack(BirthdaysListActivity.class);
        mObjectTaskStackBuilder.addNextIntent(mIntentNotificationIntent);

        //Creating the pending intent
        mPendingIntent = mObjectTaskStackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    void createNotification() {

        mObjectNotificationCompatBuilder = new NotificationCompat.Builder(mContext);
        mObjectNotification = mObjectNotificationCompatBuilder.setContentTitle("Birthday Reminder")
                .setContentText(mNotificationText)
                .setTicker("New Message Alert")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(mPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).build();

        mObjectNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mObjectNotificationManager.notify(0, mObjectNotification);
    }


}
