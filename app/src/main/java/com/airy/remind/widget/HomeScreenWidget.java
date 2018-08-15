package com.airy.remind.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.airy.remind.R;
import com.airy.remind.service.WidgetService;

public class HomeScreenWidget extends AppWidgetProvider {

    public static final String TAG = "HomeScreenWidget";
    public static final String CLICK_ACTION = "com.WidgetProvider.onclick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // generate the component
        ComponentName componentName = new ComponentName(context,HomeScreenWidget.class);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.remind_widget_layout);
        Intent intent = new Intent(context, WidgetService.class); //绑定Service
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        views.setRemoteAdapter(R.id.widget_remind_task_list,intent);

        // set the click action
        Intent clickIntent = new Intent(context,HomeScreenWidget.class);
        clickIntent.setAction(CLICK_ACTION);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_remind_task_list,pendingIntent);

//        final Intent refreshIntent = new Intent(context,HomeScreenWidget.class);
//        refreshIntent.setAction("refresh");
//        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
//                context,0,refreshIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.widget_list_btn,refreshPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (action.equals("refresh")) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context,HomeScreenWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),R.id.widget_remind_task_list);
        } else if (action.equals(CLICK_ACTION)) {
            Toast.makeText(context,intent.getStringExtra("name"),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG,"remove from the desktop");
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG,"add to the desktop");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG,"moving the widget");
    }
}

