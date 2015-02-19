package com.example.danco.bonus2.h25b2danco.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.activity.TopLevelActivity;

/**
 * Implementation of App Widget functionality.
 */
public class MyAppWidget extends AppWidgetProvider {
    private static final String TAG = MyAppWidget.class.getSimpleName() + ".tag";

    // Using fully qualified names since it may be invoked from outside the app...
    private static final String ACTION_ITEM_PRESSED = MyAppWidget.class.getName() + ".itemPressed";
    private static final String EXTRA_POSITION = MyAppWidget.class.getName() + ".position";

    // Create a fill in intent for the list row. This is used to provide the "extras". When
    // the item is clicked the system will place these "extras" in the pending intent
    // template setup for the list and then invoke the intent.
    public static Intent buildFillInIntent(int position) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }


    // Build a pending intent without setting the "extras". This will be used as a template
    // for when the item was pressed in our list.
    private static PendingIntent buildPendingIntentTemplate(Context context) {
        Intent intent = new Intent(context, MyAppWidget.class);
        // Installing the app also sends a broadcast to the same receiver.
        // Using our own action, will help us differentiate this broadcast in the receiver
        intent.setAction(ACTION_ITEM_PRESSED);
        return PendingIntent.getBroadcast(context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText1 = context.getString(R.string.appwidget_text1);
        CharSequence widgetText2 = context.getString(R.string.appwidget_text2);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        views.setTextViewText(R.id.appwidget_text1, widgetText1);
        views.setTextViewText(R.id.appwidget_text2, widgetText2);
        views.setTextViewText(R.id.appwidget_button, context.getString(R.string.appwidget_button_text));

        views.setOnClickPendingIntent(R.id.appwidget_button, buildPendingIntentTemplate(context));

        // Set title on widget. Note because the widget host does not have access to our resources
        // we need to fetch the string manually first, vs being able to provide the resource id.
        views.setTextViewText(R.id.title, context.getString(R.string.appwidget_title));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    // Called when the BroadcastReceiver receives an Intent broadcast from the widget.
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive called with intent action: " + intent.getAction());
        // If this is an item pressed broadcast, then start the TopLevelActivity using
        // the extra provided via the fill-in intent and appended to the pending
        // intent template.
        if (ACTION_ITEM_PRESSED.equals(intent.getAction())) {
            int position = intent.getIntExtra(EXTRA_POSITION, 1);
            Intent mainIntent = TopLevelActivity.buildIntent(context, position);
            // Since launching activity here, we may or may not have a task, so amend the
            // intent with the new task/clear task flags. See the MainActivity.buildPendingIntent
            // for more details on what these flags do.
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(mainIntent);
            return;
        }

        // let the base class process everything else. This includes
        // installing, updating, removing the widget...
        super.onReceive(context, intent);
    }
}


