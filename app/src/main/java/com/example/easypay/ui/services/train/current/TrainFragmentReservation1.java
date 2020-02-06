package com.example.easypay.ui.services.train.current;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class TrainFragmentReservation1 extends Fragment {

    private TrainListener listener;

    public TrainFragmentReservation1() {
        super(R.layout.fragment_train_reservation_form);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (TrainListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button checkOutBtn = view.findViewById(R.id.checkOut_train);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoFragment(new TrainFragmentReservation2());
            }
        });
    }

    public interface TrainListener {
        void gotoFragment(Fragment fragment);
    }
}
