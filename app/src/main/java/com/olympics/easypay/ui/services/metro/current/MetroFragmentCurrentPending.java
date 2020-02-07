package com.olympics.easypay.ui.services.metro.current;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.MetroTicketModel;
import com.olympics.easypay.utils.Constants;
import com.olympics.easypay.utils.Spacify;

public class MetroFragmentCurrentPending extends Fragment {

    private TextView ticket, start;

    public MetroFragmentCurrentPending() {
        super(R.layout.fragment_metro_current_pending);
    }

    public static MetroFragmentCurrentPending getInstance(MetroTicketModel metroTicketModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MODEL_KEY, metroTicketModel);

        MetroFragmentCurrentPending metroFragmentCurrentPending = new MetroFragmentCurrentPending();
        metroFragmentCurrentPending.setArguments(bundle);

        return metroFragmentCurrentPending;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticket = view.findViewById(R.id.ticketNumber);
        start = view.findViewById(R.id.startStation);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateData((MetroTicketModel) getArguments().getParcelable(Constants.MODEL_KEY));
    }

    private void updateData(MetroTicketModel metroTicketModel) {
        ticket.setText(Spacify.take(metroTicketModel.getTicketNumber() + ""));
        start.setText(metroTicketModel.getStartStation());
    }
}
