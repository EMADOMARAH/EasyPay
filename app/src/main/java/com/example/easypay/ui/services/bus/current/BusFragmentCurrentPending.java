package com.example.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.BusTicketModel;
import com.example.easypay.utils.Constants;
import com.example.easypay.utils.Spacify;

public class BusFragmentCurrentPending extends Fragment {

    private TextView ticket, line;

    public BusFragmentCurrentPending() {
        super(R.layout.fragment_bus_current_pending);
    }

    public static BusFragmentCurrentPending getInstance(BusTicketModel busTicketModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MODEL_KEY, busTicketModel);

        BusFragmentCurrentPending busFragmentCurrentPending = new BusFragmentCurrentPending();
        busFragmentCurrentPending.setArguments(bundle);

        return busFragmentCurrentPending;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticket = view.findViewById(R.id.ticketNumber);
        line = view.findViewById(R.id.lineNumber);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateViews((BusTicketModel) getArguments().getParcelable(Constants.MODEL_KEY));
    }

    private void updateViews(BusTicketModel busTicketModel) {
        ticket.setText(Spacify.take(busTicketModel.getTicketNumber() + ""));
        line.setText(busTicketModel.getLineNumber() + "");
    }
}
