package com.example.easypay.ui.services.train.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class TrainFragmentReservation extends Fragment {
    private TrainFragmentReservation1 reservation1;
    private TrainFragmentReservation2 reservation2;
    private TrainFragmentReservation3 reservation3;

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
                    reservation1 = new TrainFragmentReservation1();
                }
                fragment = reservation1;
                break;
            case 1:
                if (reservation2 == null) {
                    reservation2 = new TrainFragmentReservation2();
                }
                fragment = reservation2;
                break;
            case 2:
                if (reservation3 == null) {
                    reservation3 = new TrainFragmentReservation3();
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
