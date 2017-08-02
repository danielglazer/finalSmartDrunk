package tk.smartdrunk.smartdrunk.notificationsAndAlarms;

/**
 * Created by Daniel on 8/2/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import tk.smartdrunk.smartdrunk.R;
import tk.smartdrunk.smartdrunk.appMenu.MenuActivity;

/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotifyService extends Service {

    private String notificationString;

    /**
     * set notificationTitle
     *
     * @param notificationTitle
     */
    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    private String notificationTitle;

    /**
     * set notificationString
     *
     * @param notificationString
     */
    public void setNotificationString(String notificationString) {
        this.notificationString = notificationString;
    }

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        notificationString = intent.getStringExtra("msg");
        // If this service was started by out AlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        String uriPath = "android.resource://tk.smartdrunk.smartdrunk/";
        // This is the 'title' of the notification
        CharSequence title = "SmartDrunk:)";
        switch(notificationString.toString()) {
            case "According to our estimation you are now legally qualify for driving!":
                uriPath += R.raw.coolnotification20;title="SmartDrunk - Drive"; break;
            case "Did you suffer from a hangover? please let us know!":
                uriPath += R.raw.doramon0;title="SmartDrunk - Hangover"; break;
        }
        // This is the icon to use on the notification
        int icon = R.mipmap.ic_launcher_round;
        // This is the scrolling text of the notification
        CharSequence text = notificationString;
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification;
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MenuActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        notification = builder.setContentIntent(contentIntent)
                .setSmallIcon(icon).setTicker(text).setWhen(time)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(text).build();

        notification.sound = Uri.parse(uriPath);;

        mNM.notify(NOTIFICATION, notification);

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        mNM.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        stopSelf();
    }
}