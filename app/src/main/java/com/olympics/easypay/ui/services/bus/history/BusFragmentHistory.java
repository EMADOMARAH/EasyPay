package com.olympics.easypay.ui.services.bus.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olympics.easypay.R;
import com.olympics.easypay.models.BusHistoryModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.services.ErrorFragment;
import com.olympics.easypay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusFragmentHistory extends Fragment implements BusHistoryAdapter.BusHistoryListener {
    private static final String TAG = "MyTag";
    private BusHistoryAdapter adapter;

    public static BusFragmentHistory getInstance() {
        return new BusFragmentHistory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BusHistoryAdapter(new ArrayList<BusHistoryModel>(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_bus);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRetro();
    }

    private void initRetro() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper
                .getInstance()
                .getBusHistory(myId).enqueue(new Callback<List<BusHistoryModel>>() {
            @Override
            public void onResponse(Call<List<BusHistoryModel>> call, Response<List<BusHistoryModel>> response) {
                if (response.isSuccessful()) {
                    adapter.setBusHistoryModelList(response.body());
                    if (adapter.getBusHistoryModelList().get(0).getTicketDate().equals("NULL"))
                        showError();
                    if (adapter.getBusHistoryModelList().isEmpty()) {
                        showError();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<BusHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                showError();
            }
        });
    }

    private void showError() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorFragment())
                .commit();
    }

    @Override
    public void onItemSelected(int position) {

    }
}
