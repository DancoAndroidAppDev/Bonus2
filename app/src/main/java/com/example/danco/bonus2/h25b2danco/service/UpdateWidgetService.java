package com.example.danco.bonus2.h25b2danco.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.widget.MyAppWidget;

public class UpdateWidgetService extends RemoteViewsService {

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        return intent;
    }


    public UpdateWidgetService() {
    }


    // Primary method. All it does is create a new widget list factory that acts like
    // the adapter

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListFactory(this.getApplicationContext(), intent);
    }

    /* package */ class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;

        // Using fixed list of values, so constructor is appropriate here.
        WidgetListFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            // Do nothing long running here. Only have ~20 sec to complete...
        }

        @Override
        public void onDataSetChanged() {
            // Do any long running ops here.  If loading data from content provider, etc,
            // this is the method to use. Basically old data will be shown until this
            // method completes.
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            // Build a remote view for the position
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
            rv.setTextViewText(R.id.appwidget_button, getString(R.string.appwidget_button_text));

            // Set the onclick lister to be a fill in intent to communicate back to the Widget.
            // Essentially the "extras" will be added to the pending intent added to the Widget
            // Provider
            rv.setOnClickFillInIntent(R.id.appwidget_button, MyAppWidget.buildFillInIntent(position));
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
