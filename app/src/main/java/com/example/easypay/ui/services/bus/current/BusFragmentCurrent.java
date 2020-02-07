package com.example.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.BusTicketModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusFragmentCurrent extends Fragment {

    private static final String TAG = "MyTag";

    public BusFragmentCurrent() {
        super(R.layout.fragment_bus_current);
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
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
