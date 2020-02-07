package com.example.easypay.ui.services.bus.current;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.BusTicketModel;
import com.example.easypay.network.RetroHelper;
import com.example.easypay.ui.qrcode.QrActivity;
import com.example.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusFragmentCurrent extends Fragment {

    public static BusFragmentCurrent getInstance() {
        return new BusFragmentCurrent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_current, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
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
                    if (busTicketModel.getLineCost() == 0) {
                        loadFragment(BusFragmentCurrentPending.getInstance(busTicketModel));
                    } else {
                        loadFragment(BusFragmentCurrentSuccess.getInstance(busTicketModel));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BusTicketModel>> call, Throwable t) {
                startActivity(new Intent(getContext(), QrActivity.class));
                getActivity().finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_bus, fragment)
                .commit();
    }
}
