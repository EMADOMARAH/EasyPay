package com.olympics.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;

import java.util.Calendar;

import static com.olympics.easypay.utils.Constants.BUS_TIME;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;

public class BusFragmentCurrent extends Fragment {

    private static final String TAG = "MyTag";

    public static BusFragmentCurrent getInstance() {
        return new BusFragmentCurrent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_current, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        long l = getActivity().getSharedPreferences(SHARED_PREFS, 0).getLong(BUS_TIME, 0);
        if (Calendar.getInstance().getTimeInMillis() - l < 7200000) {
            loadFragment(new BusFragmentCurrentPending());
        } else {
            loadFragment(new BusFragmentCurrentSuccess());
        }
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
