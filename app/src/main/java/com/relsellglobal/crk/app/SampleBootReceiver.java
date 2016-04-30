package com.relsellglobal.crk.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class SampleBootReceiver extends BroadcastReceiver {
    CrkAlarmReceiver alarm = new CrkAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context);
        }
    }
}
