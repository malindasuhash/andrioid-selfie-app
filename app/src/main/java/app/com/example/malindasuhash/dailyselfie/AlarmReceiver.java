package app.com.example.malindasuhash.dailyselfie;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Receives the intent from Alarm so that a notification can be added to the
 * notification bar.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static int NotificationId = 232323;
    private static int NotificationNumber = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Received", "Alarm callback received.");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_cam_dark)
                .setContentTitle("Daily Selfie")
                .setAutoCancel(true)
                .setNumber(++NotificationNumber)
                .setContentText("Time for another selfie");

        Intent intentToLoadApp = new Intent(context, ListImages.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToLoadApp, 0);
        builder.setContentIntent(pendingIntent);

        manager.notify(NotificationId, builder.build());
    }
}
