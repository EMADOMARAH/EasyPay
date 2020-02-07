package com.example.easypay.ui.services.metro.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class MetroFragmentCurrent extends Fragment {

    public MetroFragmentCurrent() {
        super(R.layout.fragment_metro_current);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadFragment(new MetroFragmentCurrentSuccess());
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_metro, fragment)
                .commit();
    }
}
