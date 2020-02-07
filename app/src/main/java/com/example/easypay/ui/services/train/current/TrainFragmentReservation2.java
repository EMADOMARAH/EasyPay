package com.example.easypay.ui.services.train.current;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class TrainFragmentReservation2 extends Fragment {

    private TrainListener listener;

    public TrainFragmentReservation2() {
        super(R.layout.fragment_train_reservation_check);
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

        ImageButton editBtn = view.findViewById(R.id.edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoFragment(0);
            }
        });

        Button confirmBtn = view.findViewById(R.id.confirm_train);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoFragment(2);
            }
        });
    }

    public interface TrainListener {
        void gotoFragment(int page);
    }
}
