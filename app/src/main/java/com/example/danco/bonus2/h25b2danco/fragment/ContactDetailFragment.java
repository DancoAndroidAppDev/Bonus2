package com.example.danco.bonus2.h25b2danco.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.example.danco.bonus2.R;

import java.util.Date;


public class ContactDetailFragment extends Fragment
        implements DatePickerDialogFragment.DatePickerDialogFragmentListener,
        View.OnClickListener {

    private static final String ARG_NAME = ContactDetailFragment.class.getName() + ".name";

    private static final int BIRTH_REQUEST_CODE = 100;
    private static final String EXTRA_ADDRESS = ContactDetailFragment.class.getName() + ".address";
    private static final String EXTRA_BIRTH_DATE = ContactDetailFragment.class.getName() + ".birthDate";
    private static final String DEFAULT_ADDRESS = "123 Demo Street\nSeattle, WA 98101";

    private String name;
    private String address;
    private Date birthDate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name the user's name
     * @return A new instance of fragment ContactDetailFragment.
     */
    public static ContactDetailFragment newInstance(String name) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactDetailFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
        // Also initialize address/birth date if no save state
        if (savedInstanceState == null) {
            address = DEFAULT_ADDRESS;
            birthDate = new Date();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        holder.birthDateView.setOnClickListener(this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // If have saved state, restore our model here.
        if (savedInstanceState != null) {
            address = savedInstanceState.getString(EXTRA_ADDRESS);
            birthDate = new Date(savedInstanceState.getLong(EXTRA_BIRTH_DATE));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewHolder holder = getViewHolder();
        // holder better not be null or there is a bug earlier in the code.
        // onResume always will have a view.

        // Refresh our views. Primarily because we will know our model is restored
        // if we had saved state by this point.
        //
        // Also side benefit is if user changes date format via settings
        // and comes back to app we will honor the new format. Try it...
        updateView(holder);
    }

    @Override
    public void onPause() {
        ViewHolder holder = getViewHolder();
        // holder should not be null as this method is called on UI thread and before
        // destroy view.

        // Update model value for the address so we can save it later in onSaveInstanceState
        address = holder.addressView.getText().toString();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_ADDRESS, address);
        outState.putLong(EXTRA_BIRTH_DATE, birthDate.getTime());
    }

    private void updateView(ViewHolder holder) {
        if (holder == null) {
            return;
        }

        holder.nameView.setText(name);
        holder.addressView.setText(address);
        updateBirthDateView(holder.birthDateView, birthDate);
    }

    private void updateBirthDateView(TextView birthDateView, Date newDate) {
        // This is using the user's preferred date format and locale info to
        // format the date. Always best to show date/time in user's preferred format.

        // If also showing time there is a getTimeFormat() method as well
        birthDateView.setText(DateFormat.getDateFormat(getActivity()).format(newDate));
    }

    @Override
    public void onDateSet(int requestId, @NonNull Date date) {
        if (requestId == BIRTH_REQUEST_CODE) {
            // update our model...
            birthDate = date;

            // update the view as well...
            ViewHolder holder = getViewHolder();
            if (holder != null) {
                updateBirthDateView(holder.birthDateView, birthDate);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.birthDate) {
            DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(BIRTH_REQUEST_CODE, birthDate);
            fragment.show(getChildFragmentManager(), "DIALOG");
        }
    }

    private ViewHolder getViewHolder() {
        View view = getView();
        return view != null ? (ViewHolder) view.getTag() : null;
    }

    static class ViewHolder {
        final TextView nameView;
        final EditText addressView;
        final TextView birthDateView;

        ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.name);
            addressView = (EditText) view.findViewById(R.id.address);
            birthDateView = (TextView) view.findViewById(R.id.birthDate);
        }
    }
}
