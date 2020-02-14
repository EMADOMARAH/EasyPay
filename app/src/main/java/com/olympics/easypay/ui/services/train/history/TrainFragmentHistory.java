package com.olympics.easypay.ui.services.train.history;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olympics.easypay.R;
import com.olympics.easypay.models.TrainHistoryModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.services.ErrorFragment;
import com.olympics.easypay.utils.Constants;

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
                            List<TrainHistoryModel> list = response.body();
                            if (list == null) {
                                showError();
                                return;
                            }
                            if (list.get(0) == null) {
                                showError();
                                return;
                            }
                            if (list.get(0).getStartStation() == null) {
                                showError();
                                return;
                            }
                            if (list.get(0).getStartStation().equals("NULL")) {
                                showError();
                                return;
                            }
                            if (list.isEmpty()) {
                                showError();
                                return;
                            }
                            adapter.setTrainHistoryModelList(list);
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TrainHistoryModel>> call, Throwable t) {
                        showError();
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailureTrainHistory: " + t.toString());
                    }
                });
    }

    private void showError() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorFragment())
                .commit();
    }

    @Override
    public void onItemSelected(int position) {

    }
}
