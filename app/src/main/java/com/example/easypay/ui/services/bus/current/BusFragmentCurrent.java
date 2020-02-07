package com.example.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class BusFragmentCurrent extends Fragment {

    public BusFragmentCurrent() {
        super(R.layout.fragment_bus_current);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadFragment(new BusFragmentCurrentSuccess());
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_bus, fragment)
                .commit();
    }
}
