package com.olympics.easypay.ui.services.bus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.BusTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Spacify;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusTicketFragment extends Fragment {

    private static final String TAG = "MyTag";
    private TextView ticket, line, date, cost;

    public BusTicketFragment() {
        super(R.layout.fragment_bus_current_success);
    }

    public static BusTicketFragment getInstance(String ticketNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("ticket", ticketNumber);
        BusTicketFragment fragment = new BusTicketFragment();
        fragment.setArguments(bundle);
        return fragment;
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
    }

    @Override
    public void onStart() {
        super.onStart();

        initData();
    }

    private void initData() {
//        final int id = getActivity().getSharedPreferences(SHARED_PREFS, 0).getInt(TOKEN, 0);
        int no = Integer.valueOf(getArguments().getString("ticket"));
        MyRetroFitHelper.getInstance().getBusTicketByNumber(no).enqueue(new Callback<List<BusTicketModel>>() {
            @Override
            public void onResponse(Call<List<BusTicketModel>> call, Response<List<BusTicketModel>> response) {
                if (response.isSuccessful()) {
                    updateViews(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<BusTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureBusTicketByNumber: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateViews(BusTicketModel busTicketModel) {
        ticket.setText(Spacify.take(busTicketModel.getTicketNumber() + ""));
        line.setText(busTicketModel.getLineNumber() + "");
        date.setText(busTicketModel.getTicketDate());
        cost.setText(busTicketModel.getLineCost() + " EGP");
    }
}
