package com.example.danco.bonus2.h25b2danco.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.danco.bonus2.R;

import java.util.ArrayList;
import java.util.Arrays;


public class ContactListFragment extends Fragment
        implements AdapterView.OnItemClickListener {

    private static final String ARG_VALUES = "initialValues";
    private static final String ARGS_CHOICE_MODE = "listChoiceMode";
    private static final String STATE_VALUES = ContactListFragment.class.getSimpleName() + ".values";
    private static final String STATE_SELECTION = ContactListFragment.class.getSimpleName() + ".selection";

    // Initialize to null.
    private ArrayList<String> values = new ArrayList<>();

    private int listChoiceMode = ListView.CHOICE_MODE_NONE;
    private int selectedItem = 0;
    private ContactListFragmentListener listener;

    public interface ContactListFragmentListener {
        public void onContactListItemSelected(String name);
    }

    public static ContactListFragment newInstance(String[] initialValues, boolean highlightList) {
        ContactListFragment contactListFragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_VALUES, initialValues);
        args.putInt(ARGS_CHOICE_MODE, highlightList ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
        contactListFragment.setArguments(args);
        return contactListFragment;
    }

    public ContactListFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            listChoiceMode = args.getInt(ARGS_CHOICE_MODE, ListView.CHOICE_MODE_NONE);
            if (savedInstanceState == null) {
                values.addAll(Arrays.asList(args.getStringArray(ARG_VALUES)));
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parent = getParentFragment();
        Object objectToCast = parent != null ? parent : activity;
        try {
            listener = (ContactListFragmentListener) objectToCast;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(objectToCast.getClass().getSimpleName()
                    + " must implement ContactListFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View holder here is not necessarily required since not accessing the
        // list view after this method. But including for example purposes
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        if (savedInstanceState != null) {
            values = savedInstanceState.getStringArrayList(STATE_VALUES);
            selectedItem = savedInstanceState.getInt(STATE_SELECTION, 0);
        }

        holder.list.setOnItemClickListener(this);

        if (values != null) {
            configureAdapter(holder.list);
        }
    }


    private void configureAdapter(final ListView list) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.contact_list_item,
                R.id.text,
                // clone list so frag can track values as well because we don't
                // know what the adapter is doing...
                new ArrayList<>(values));

        list.setAdapter(adapter);
        list.setChoiceMode(listChoiceMode);
        list.setItemChecked(selectedItem, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_VALUES, values);
        outState.putInt(STATE_SELECTION, selectedItem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
        ListView list = (ListView) parent;
        list.setItemChecked(position, true);
        listener.onContactListItemSelected(values.get(position));
    }

    public void addName(String name) {
        values.add(name);

        ViewHolder holder = getViewHolder();
        if (holder != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) holder.list.getAdapter();
            // adapter shouldn't be null, but lets be safe.
            if (adapter != null) {
                adapter.add(name);
            }
        }
    }

    private ViewHolder getViewHolder() {
        View view = getView();
        return view != null ? (ViewHolder) view.getTag() : null;
    }

    /* package */ static class ViewHolder {
        final ListView list;

        ViewHolder(View view) {
            list = (ListView) view.findViewById(R.id.list);
            list.setEmptyView(view.findViewById(R.id.empty));
        }
    }
}
