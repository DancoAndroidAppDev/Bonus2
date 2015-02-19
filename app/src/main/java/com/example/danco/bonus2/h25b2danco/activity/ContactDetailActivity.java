package com.example.danco.bonus2.h25b2danco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.fragment.ContactDetailFragment;


public class ContactDetailActivity extends ActionBarActivity {

    private static final String EXTRA_NAME = ContactDetailActivity.class.getName() + ".name";

    public static Intent buildIntent(Context context, String name) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a boolean to indicate multi fragment possible. If so, finish and
        // quit processing.
        boolean multiFragment = getResources().getBoolean(R.bool.multiFragment);
        if (multiFragment) {
            finish();
            return;
        }

        // Must be phone, so load our hierarchy now.
        setContentView(R.layout.activity_contact_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.contactDetailToolBar));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Only add a dynamic fragment if the saved instance state is null.
        // Otherwise it already has been added to the view hierarchy
        if (savedInstanceState == null) {
            String name = getIntent().getStringExtra(EXTRA_NAME);
            ContactDetailFragment fragment = ContactDetailFragment.newInstance(name);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contactDetailContainer, fragment, "DETAILS")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
