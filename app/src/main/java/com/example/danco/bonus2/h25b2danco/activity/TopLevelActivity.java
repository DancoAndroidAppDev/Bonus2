package com.example.danco.bonus2.h25b2danco.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.adapter.DrawerAdapter;
import com.example.danco.bonus2.h25b2danco.fragment.AnimationFragment;
import com.example.danco.bonus2.h25b2danco.fragment.ContactsFragment;
import com.example.danco.bonus2.h25b2danco.fragment.FloatingActionFragment;
import com.example.danco.bonus2.h25b2danco.fragment.GridViewFragment;


public class TopLevelActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int NOTIFICATION_ID = 100;

    private static final String TAG = TopLevelActivity.class.getSimpleName() + ".tag";
    private static final String STATE_SELECTED_POSITION = "selectedPosition";
    private static final int BASE_REQUEST_CODE = 100;
    private static final String ACTION_NOTIFICATION = TopLevelActivity.class.getName() + ".notification";
    private static final String EXTRA_POSITION = TopLevelActivity.class.getName() + ".position";

    public static final int FLOATING_ACTION_BUTTON_POS = 3;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout drawerContent;

    private String[] titleArray;
    private int selectedPosition = 1;


    /**
     * Helpers to build various intents and pending intents
     * @param context
     * @return
     */
    public static Intent buildIntent(Context context, int position) {
        Intent intent = new Intent(context, TopLevelActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    public static PendingIntent buildNotificationIntent(Context context, int position) {
        return buildNotificationIntent(context, BASE_REQUEST_CODE + position, buildIntent(context, position));
    }


    private static PendingIntent buildNotificationIntent(Context context, int requestCode, Intent intent) {
        // This extra is to help inform main activity it should "clear" the notification.
        intent.setAction(ACTION_NOTIFICATION);
        return buildPendingIntent(context, requestCode, intent);
    }


    private static PendingIntent buildPendingIntent(Context context, int requestCode, Intent intent) {
        // Since this is a pending intent, the app may or may not be currently running.
        // NEW_TASK ensures the app is started if it is not currently running
        // CLEAR_TASK removes all current activities in the task, making this activity the only activity
        // in the back stack.

        // Note: CLEAR_TASK is new in API 11, if you were supporting 2.3, you may need to use CLEAR_TOP
        // instead. However the difference is there may be other activities in the back stack.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        titleArray = getResources().getStringArray(R.array.drawerItems);

        Toolbar toolbar = (Toolbar) findViewById(R.id.topLevelToolBar);
        setSupportActionBar(toolbar);

        selectedPosition = getIntent().getIntExtra(EXTRA_POSITION, selectedPosition);
        boolean isNotificationStart = ACTION_NOTIFICATION.equals(getIntent().getAction());
        if (isNotificationStart) {
            SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(
                    getString(R.string.sharedPreferencesKey), Activity.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
            prefsEditor.putInt(getString(R.string.notificationCount), 0);
            prefsEditor.commit();
            Log.i(TAG, "Notification count reset to 0" );

            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        }

        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, selectedPosition);
        }

        setupDrawer(toolbar);
        setupDrawerContent();

        if (savedInstanceState == null) {
            updateContentView();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        drawerContent = null;
        drawerToggle = null;
        drawerLayout = null;
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawer(drawerContent);

        if (selectedPosition != position) {
            selectedPosition = position;
            updateContentView();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settingsView) {
            drawerLayout.closeDrawer(drawerContent);
            startActivity(SettingsActivity.buildIntent(this));
        }
    }

    private void setupDrawer(Toolbar toolbar) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setStatusBarBackground(getPrimaryColorDarkResId());

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Enable Nav Button (menu) to be shown
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * helper to pull the color resource id from the theme
     *
     * @return resource id from theme
     */
    private int getPrimaryColorDarkResId() {
        int attributes[] = new int[]{R.attr.colorPrimaryDark};
        TypedArray attrs = getTheme().obtainStyledAttributes(attributes);
        int primaryColorResId = attrs.getResourceId(0, R.color.primary_dark_material_light);
        attrs.recycle();

        return primaryColorResId;
    }

    private void setupDrawerContent() {
        drawerContent = (FrameLayout) findViewById(R.id.drawerContent);

        int statusBarHeight = getStatusBarHeight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addStatusBarOverlay(drawerContent, statusBarHeight);
        }

        // The drawer content by default will not draw under the status bar, the app needs to offset the top
        // of the list so it is "under" status bar. To do this we need to adjust the top margin
        // to be negative. Negative margins are not used often, but primarily used when you want
        // a view to encroach on the space of another view.
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) drawerContent.getLayoutParams();
        params.topMargin = -1 * statusBarHeight;

        setupDrawerList();
    }

    private void setupDrawerList() {
        ListView drawerList = (ListView) drawerContent.findViewById(R.id.drawerList);

        RelativeLayout header = (RelativeLayout)
                LayoutInflater.from(this).inflate(R.layout.drawer_header, drawerList, false);
        drawerList.addHeaderView(header, null, false);

        View footer = LayoutInflater.from(this).inflate(R.layout.drawer_footer, drawerList, false);
        drawerList.addFooterView(footer, null, false);

        initializeFooter(footer);

        BaseAdapter adapter = new DrawerAdapter(this);

        drawerList.setAdapter(adapter);
        drawerList.setItemChecked(selectedPosition, true);
        drawerList.setOnItemClickListener(this);
    }

    /**
     * Helper to add an overlay to provide depth to status bar and allow icons to show.
     *
     * @param header          container for the new status overlay
     * @param statusBarHeight height of the status bar in pixels
     */
    private static void addStatusBarOverlay(FrameLayout header, int statusBarHeight) {
        View statusOverlay = new View(header.getContext());
        statusOverlay.setBackgroundResource(R.color.statusOverlay);
        FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(header.getLayoutParams());
        overlayParams.height = statusBarHeight;
        //overlayParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        statusOverlay.setLayoutParams(overlayParams);
        header.addView(statusOverlay);
    }

    /**
     * Retrieve the status bar height. By default this is a dimen that is not public, so
     * have to manually retrieve. Use a default fallback if fails.
     *
     * @return height of status bar
     */
    private int getStatusBarHeight() {
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId == 0) {
            resId = R.dimen.default_status_bar_height;
        }
        return getResources().getDimensionPixelSize(resId);
    }

    private void initializeFooter(View footer) {
        TextView settings = (TextView) footer.findViewById(R.id.settingsView);
        settings.setText(R.string.settings);
        settings.setOnClickListener(this);
    }

    private void updateContentView() {

        Fragment fragment = null;

        // subtracting 1 to account for "header" view in list
        int adjustedPosition = selectedPosition - 1;
        switch (adjustedPosition) {
            case 0:
                fragment = ContactsFragment.newInstance();
                break;
            case 1:
                fragment = GridViewFragment.newInstance();
                break;
            case 2:
                fragment = FloatingActionFragment.newInstance();
                break;
            case 3:
                fragment = AnimationFragment.newInstance();
                break;
        }
        getSupportActionBar().setTitle(titleArray[adjustedPosition]);

        // Theoretically you will never have a null fragment, but this
        // can help by avoiding a crash if you add an item in list, but
        // forget to add to this method.
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, "FRAG")
                    .commit();
        }
    }
}
