package com.olympics.easypay.ui.services.metro.current;

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
import com.olympics.easypay.ui.services.metro.ErrorMetroFragment;
import com.olympics.easypay.utils.Constants;
import com.olympics.easypay.utils.Spacify;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetroFragmentCurrentPending extends Fragment {

    private static final String TAG = "MyTag";
    private TextView ticket, start;

    public MetroFragmentCurrentPending() {
        super(R.layout.fragment_metro_current_pending);
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

        initData();
    }

    private void initData() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getMetroTicket(myId).enqueue(new Callback<List<MetroTicketModel>>() {
            @Override
            public void onResponse(Call<List<MetroTicketModel>> call, Response<List<MetroTicketModel>> response) {
                if (response.isSuccessful()) {
                    MetroTicketModel metroTicketModel = response.body().get(0);
                    if (metroTicketModel == null) {
                        Log.d(TAG, "onResponse: metroTicketModel");
                        showError();
                        return;
                    }
                    if (metroTicketModel.getTicketNumber() == null) {
                        Log.d(TAG, "onResponse: getTicketNumber");
                        showError();
                        return;
                    }
                    if (metroTicketModel.getTicketNumber().equals("null")) {
                        showError();
                        return;
                    }
                    if (metroTicketModel.getTicketNumber().equals("NULL")) {
                        showError();
                        return;
                    }
                    updateData(metroTicketModel);
                }
            }

            @Override
            public void onFailure(Call<List<MetroTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureMetroTicket: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showError() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorMetroFragment())
                .commit();
    }

    private void updateData(MetroTicketModel metroTicketModel) {
        ticket.setText(Spacify.take(metroTicketModel.getTicketNumber() + ""));
        start.setText(metroTicketModel.getStartStation());
    }
}
