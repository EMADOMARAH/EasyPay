package com.olympics.easypay.ui.services.bus.current;

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
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.services.bus.ErrorBusFragment;
import com.olympics.easypay.utils.Constants;
import com.olympics.easypay.utils.Spacify;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusFragmentCurrentPending extends Fragment {

    private static final String TAG = "MyTag";
    private TextView ticket, line;

    public BusFragmentCurrentPending() {
        super(R.layout.fragment_bus_current_pending);
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

        initRetro();
    }

    private void initRetro() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetroHelper.class)
                .getBusTicket(myId).enqueue(new Callback<List<BusTicketModel>>() {
            @Override
            public void onResponse(Call<List<BusTicketModel>> call, Response<List<BusTicketModel>> response) {
                if (response.isSuccessful()) {
                    BusTicketModel busTicketModel = response.body().get(0);
                    if (busTicketModel == null) {
                        showError();
                        return;
                    }
                    if (busTicketModel.getTicketNumber() == null) {
                        showError();
                        return;
                    }
                    if (busTicketModel.getTicketNumber().equals("null")) {
                        showError();
                        return;
                    }
                    if (busTicketModel.getTicketNumber().equals("NULL")) {
                        showError();
                        return;
                    }
                    updateViews(busTicketModel);
                }
            }

            @Override
            public void onFailure(Call<List<BusTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureBusTicket: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showError() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorBusFragment())
                .commit();
    }

    private void updateViews(BusTicketModel busTicketModel) {
        ticket.setText(Spacify.take(busTicketModel.getTicketNumber() + ""));
        line.setText(busTicketModel.getLineNumber() + "");
    }
}
