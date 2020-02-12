package com.olympics.easypay.ui.services.bus.current;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.BusTicketModel;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusFragmentCurrent extends Fragment {

    private static final String TAG = "MyTag";

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
                Log.d(TAG, "onFailureBusTicket: " + t.toString());
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
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
