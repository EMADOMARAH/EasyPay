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

public class BusFragmentCurrentSuccess extends Fragment {

    private TextView ticket, line, date, cost;

    public BusFragmentCurrentSuccess() {
        super(R.layout.fragment_bus_current_success);
    }

    public static BusFragmentCurrentSuccess getInstance(BusTicketModel busTicketModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MODEL_KEY, busTicketModel);

        BusFragmentCurrentSuccess busFragmentCurrentSuccess = new BusFragmentCurrentSuccess();
        busFragmentCurrentSuccess.setArguments(bundle);

        return busFragmentCurrentSuccess;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ticket = view.findViewById(R.id.ticketNumber);
        line = view.findViewById(R.id.lineNumber);
        date = view.findViewById(R.id.payDate);
        cost = view.findViewById(R.id.cost);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateViews((BusTicketModel) getArguments().getParcelable(Constants.MODEL_KEY));
    }

    private void updateViews(BusTicketModel busTicketModel) {
        ticket.setText(Spacify.take(busTicketModel.getTicketNumber() + ""));
        line.setText(busTicketModel.getLineNumber() + "");
        date.setText(busTicketModel.getTicketDate());
        cost.setText(busTicketModel.getLineCost() + " EGP");
    }
}
