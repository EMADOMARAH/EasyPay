package com.example.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.BusTicketModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;
import com.example.easypay.utils.Spacify;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusFragmentCurrent1 extends Fragment {

    private static final String TAG = "MyTag";
    private TextView ticket, line, date, cost;

    public BusFragmentCurrent1() {
        super(R.layout.fragment_bus_current_success);
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

        initData();
    }

    private void initData() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getBusTicket(myId).enqueue(new Callback<List<BusTicketModel>>() {
            @Override
            public void onResponse(Call<List<BusTicketModel>> call, Response<List<BusTicketModel>> response) {
                if (response.isSuccessful()) {
                    updateViews(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<BusTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateViews(BusTicketModel busTicketModel) {
        ticket.setText(Spacify.take(busTicketModel.getTicketNumber() + ""));
        line.setText(busTicketModel.getLineNumber());
        date.setText(busTicketModel.getTicketDate());
        cost.setText(busTicketModel.getLineCost());
    }
}
