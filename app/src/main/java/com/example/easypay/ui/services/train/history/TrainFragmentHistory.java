package com.example.easypay.ui.services.train.history;

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
import com.example.easypay.models.TrainHistoryModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainFragmentHistory extends Fragment implements TrainHistoryAdapter.TrainHistoryListener {

    private static final String TAG = "MyTag";
    private TrainHistoryAdapter adapter;

    public TrainFragmentHistory() {
        super(R.layout.fragment_train_history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TrainHistoryAdapter(new ArrayList<TrainHistoryModel>(), this);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_train);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getData();
    }

    private void getData() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance()
                .getTrainHistory(myId)
                .enqueue(new Callback<List<TrainHistoryModel>>() {
                    @Override
                    public void onResponse(Call<List<TrainHistoryModel>> call, Response<List<TrainHistoryModel>> response) {
                        if (response.isSuccessful()) {
                            adapter.setTrainHistoryModelList(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TrainHistoryModel>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.toString());
                    }
                });
    }

    @Override
    public void onItemSelected(int position) {

    }
}
