package com.example.danco.bonus2.h25b2danco.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.danco.bonus2.R;
import com.example.danco.bonus2.h25b2danco.activity.AddContactActivity;
import com.example.danco.bonus2.h25b2danco.activity.ContactDetailActivity;


/**
 * Master/Detail fragment for Contacts
 */
public class ContactsFragment extends Fragment
implements ContactListFragment.ContactListFragmentListener {

    private static final String LIST_FRAG = "contactListFrag";
    private static final String DETAIL_FRAG = "contactDetailFrag";
    private static final int ADD_REQUEST = 105;

    private boolean hasDetailFragment = false;

    private static final String[] VALUES = new String[]{
            "John Doe",
            "Jane Doe",
            "Bob Smith",
            "Susan Shoemaker"
    };

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Must call this in onCreate to see action bar item
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hasDetailFragment = view.findViewById(R.id.contactDetailContainer) != null;

        if (savedInstanceState == null) {
            ContactListFragment contactListFragment = ContactListFragment.newInstance(VALUES, hasDetailFragment);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.contactListContainer, contactListFragment, LIST_FRAG)
                    .commit();
        }

        if (hasDetailFragment) {
            ContactDetailFragment contactDetailFragment = (ContactDetailFragment)
                    getChildFragmentManager().findFragmentByTag(DETAIL_FRAG);
            if (contactDetailFragment == null) {
                contactDetailFragment = ContactDetailFragment.newInstance(VALUES[0]);
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.contactDetailContainer, contactDetailFragment, DETAIL_FRAG)
                        .commit();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Adding add here since this fragment will always show the child. Also by placing
        // it here it limits the interaction with list fragment to just notifying
        // of the new item to add to the data set
        inflater.inflate(R.menu.menu_fragment_contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.add_contact:
                startActivityForResult(AddContactActivity.buildIntent(getActivity()), ADD_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_REQUEST && data != null) {
                String name = data.getStringExtra(AddContactActivity.EXTRA_NAME);

                // Pass the new name to the list. We want to append to existing list
                ContactListFragment contactListFragment =
                        (ContactListFragment) getChildFragmentManager().findFragmentByTag(LIST_FRAG);
                if (contactListFragment != null) {
                    contactListFragment.addName(name);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onContactListItemSelected(String name) {
        if (hasDetailFragment) {
            ContactDetailFragment contactDetailFragment = ContactDetailFragment.newInstance(name);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contactDetailContainer, contactDetailFragment, DETAIL_FRAG)
                    .commit();
        }
        else {
            startActivity(ContactDetailActivity.buildIntent(getActivity(), name));
        }
    }
}
