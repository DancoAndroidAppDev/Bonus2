package com.example.danco.bonus2.h25b2danco.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.fragment.AddContactFragment;


public class AddContactActivity extends ActionBarActivity
        implements AddContactFragment.AddContactFragmentListener {

    public static final String EXTRA_NAME = AddContactActivity.class.getSimpleName() + ".name";

    public static Intent buildIntent(Context context) {
        return new Intent(context, AddContactActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setSupportActionBar((Toolbar) findViewById(R.id.addContactToolBar));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onContactAdded(String name) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
