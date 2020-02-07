package com.example.easypay.ui.services.train.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class TrainFragmentReservation extends Fragment {
    private TrainFragmentReservationForm reservation1;
    private TrainFragmentReservationCheck reservation2;
    private TrainFragmentReservationSuccess reservation3;

    public TrainFragmentReservation() {
        super(R.layout.fragment_train_reservation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadFragment(0);
    }

    public void loadFragment(int page) {
        Fragment fragment = null;
        switch (page) {
            case 0:
                if (reservation1 == null) {
                    reservation1 = new TrainFragmentReservationForm();
                }
                fragment = reservation1;
                break;
            case 1:
                if (reservation2 == null) {
                    reservation2 = new TrainFragmentReservationCheck();
                }
                fragment = reservation2;
                break;
            case 2:
                if (reservation3 == null) {
                    reservation3 = new TrainFragmentReservationSuccess();
                }
                fragment = reservation3;
                break;
        }
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_train, fragment)
                .commit();
    }
}
