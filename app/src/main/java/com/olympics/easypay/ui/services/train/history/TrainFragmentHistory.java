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
import com.olympics.easypay.ui.services.train.ErrorTrainFragment;
import com.olympics.easypay.ui.services.train.TrainTicketFragment;
import com.olympics.easypay.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        final int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getLastTrainTrip(myId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONArray array=new JSONArray(response.body().string());
                        if (array.get(0) instanceof JSONArray){
                            showError();
                            return;
                        }
                        MyRetroFitHelper.getInstance().getTrainHistory(myId).enqueue(new Callback<List<TrainHistoryModel>>() {
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
                                    if (list.get(0).getStartStation().equals("null")) {
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
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailureTrainHistory: " + t.toString());
                                getActivity().finish();
                            }
                        });
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureLastTrain: " + t.toString());
                getActivity().finish();
            }
        });
    }

    private void showError() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorTrainFragment())
                .commit();
    }

    @Override
    public void onItemSelected(int position) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, TrainTicketFragment.getInstance(adapter.getTrainHistoryModelList().get(position).getTicketNumber()),"ticket")
                .commit();
    }
}
