package com.olympics.easypay.ui.services.metro;

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
import com.olympics.easypay.models.MetroTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Spacify;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetroTicketFragment extends Fragment {

    private static final String TAG = "MyTag";
    private TextView ticket, start, end, date, cost;

    public MetroTicketFragment() {
        super(R.layout.fragment_metro_current_success);
    }

    public static MetroTicketFragment getInstance(String ticketNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("ticket", ticketNumber);
        MetroTicketFragment fragment = new MetroTicketFragment();
        fragment.setArguments(bundle);
        return fragment;
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

        initData();
    }

    private void initData() {
//        final int id = getActivity().getSharedPreferences(SHARED_PREFS, 0).getInt(TOKEN, 0);
        int no = Integer.valueOf(getArguments().getString("ticket"));
        MyRetroFitHelper.getInstance().getMetroTicketByNumber(no).enqueue(new Callback<List<MetroTicketModel>>() {
            @Override
            public void onResponse(Call<List<MetroTicketModel>> call, Response<List<MetroTicketModel>> response) {
                if (response.isSuccessful()) {
                    updateData(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<MetroTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureMetroTicketByNumber: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateData(MetroTicketModel metroTicketModel) {
        ticket.setText(Spacify.take(metroTicketModel.getTicketNumber() + ""));
        start.setText(metroTicketModel.getStartStation());
        end.setText(metroTicketModel.getEndStation());
        date.setText(metroTicketModel.getTicketDate());
        cost.setText(metroTicketModel.getMetroCost() + " EGP");
    }
}
