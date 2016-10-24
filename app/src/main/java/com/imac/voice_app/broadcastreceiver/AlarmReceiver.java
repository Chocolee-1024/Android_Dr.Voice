package com.imac.voice_app.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.imac.voice_app.R;
import com.imac.voice_app.module.AlarmConstantManager;
import com.imac.voice_app.util.homepage.HomePageActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mode = intent.getStringExtra(AlarmConstantManager.INTENT_MODE);
        Intent gotoMainActivity = new Intent(context, HomePageActivity.class);
        Bundle whichMde = new Bundle();
        gotoMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        whichMde.putString(AlarmConstantManager.INTENT_MODE, mode);
        gotoMainActivity.putExtras(whichMde);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gotoMainActivity, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        if (mode.equals(AlarmConstantManager.MODE_WEEK)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText(context.getResources().getString(R.string.setting_weekly_notice))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_WEEKLY_ALARM, notification);
        } else if (mode.equals(AlarmConstantManager.MODE_DAILY)) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Voice 嗓音自我照護")
                    .setContentText(context.getResources().getString(R.string.setting_daily_notice))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(AlarmConstantManager.ID_DAILY_ALARM, notification);
        }
    }
}
