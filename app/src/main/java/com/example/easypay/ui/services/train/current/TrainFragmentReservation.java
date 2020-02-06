package com.example.easypay.ui.services.train.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class TrainFragmentReservation extends Fragment {

    public TrainFragmentReservation() {
        super(R.layout.fragment_train_reservation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadFragment(new TrainFragmentReservation1());
    }

    public void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_train, fragment)
                .commit();
    }
}
