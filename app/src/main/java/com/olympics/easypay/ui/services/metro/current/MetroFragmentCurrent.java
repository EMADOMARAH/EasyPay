package com.olympics.easypay.ui.services.metro.current;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.MetroTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.qrcode.QrActivity;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetroFragmentCurrent extends Fragment {

    public MetroFragmentCurrent() {
        super(R.layout.fragment_metro_current);
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
                    if (metroTicketModel.getMetroCost() == 0) {
                        loadFragment(MetroFragmentCurrentPending.getInstance(metroTicketModel));
                    } else {
                        loadFragment(MetroFragmentCurrentSuccess.getInstance(metroTicketModel));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MetroTicketModel>> call, Throwable t) {
                startActivity(new Intent(getContext(), QrActivity.class));
                getActivity().finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_metro, fragment)
                .commit();
    }
}
