package com.example.easypay.ui.services.metro.history;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easypay.R;
import com.example.easypay.models.MetroHistoryModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetroFragmentHistory extends Fragment implements MetroHistoryAdapter.MetroHistoryListener {

    private static final String TAG = "MyTag";
    private MetroHistoryAdapter adapter;
    private int myId;

    public MetroFragmentHistory() {
        super(R.layout.fragment_metro_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MetroHistoryAdapter(new ArrayList<MetroHistoryModel>(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_metro);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getData();
    }

    private void getData() {
        myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getMetroHistory(myId).enqueue(new Callback<List<MetroHistoryModel>>() {
            @Override
            public void onResponse(Call<List<MetroHistoryModel>> call, Response<List<MetroHistoryModel>> response) {
                if (response.isSuccessful()) {
                    adapter.setMetroHistoryModelList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MetroHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(int position) {

    }
}
