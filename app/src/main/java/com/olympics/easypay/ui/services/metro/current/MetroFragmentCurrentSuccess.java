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

public class MetroFragmentCurrentSuccess extends Fragment {

    private TextView ticket, start, end, date, cost;

    public MetroFragmentCurrentSuccess() {
        super(R.layout.fragment_metro_current_success);
    }

    public static MetroFragmentCurrentSuccess getInstance(MetroTicketModel metroTicketModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MODEL_KEY, metroTicketModel);

        MetroFragmentCurrentSuccess metroFragmentCurrentSuccess = new MetroFragmentCurrentSuccess();
        metroFragmentCurrentSuccess.setArguments(bundle);

        return metroFragmentCurrentSuccess;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticket = view.findViewById(R.id.ticketNumber);
        start = view.findViewById(R.id.startStation);
        end = view.findViewById(R.id.endStation);
        date = view.findViewById(R.id.payDate);
        cost = view.findViewById(R.id.cost);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateData((MetroTicketModel) getArguments().getParcelable(Constants.MODEL_KEY));
    }

    private void updateData(MetroTicketModel metroTicketModel) {
        ticket.setText(Spacify.take(metroTicketModel.getTicketNumber() + ""));
        start.setText(metroTicketModel.getStartStation());
        end.setText(metroTicketModel.getEndStation());
        date.setText(metroTicketModel.getTicketDate());
        cost.setText(metroTicketModel.getMetroCost() + " EGP");
    }
}
