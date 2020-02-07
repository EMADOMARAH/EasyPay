package com.example.easypay.ui.services.metro.current;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.MetroTicketModel;
import com.example.easypay.network.MyRetroFitHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetroFragmentCurrent extends Fragment {

    private static final String TAG = "MyTag";

    public MetroFragmentCurrent() {
        super(R.layout.fragment_metro_current);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        int myId = 102;
//      int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
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
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
