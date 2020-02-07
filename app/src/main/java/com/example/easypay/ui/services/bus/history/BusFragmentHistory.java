package com.example.easypay.ui.services.bus.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easypay.R;
import com.example.easypay.models.BusHistoryModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;

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
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BusHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(int position) {

    }
}
