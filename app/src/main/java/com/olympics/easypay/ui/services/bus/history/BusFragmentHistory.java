package com.olympics.easypay.ui.services.bus.history;

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

import com.olympics.easypay.R;
import com.olympics.easypay.models.BusHistoryModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.ui.services.bus.BusTicketFragment;
import com.olympics.easypay.ui.services.bus.ErrorBusFragment;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        initRetro();
    }

    private void initRetro() {
        final int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getLastBusTrip(myId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        if (array.get(0) instanceof JSONArray) {
                            showError();
                            return;
                        }
                        MyRetroFitHelper.getInstance().getBusHistory(myId).enqueue(new Callback<List<BusHistoryModel>>() {
                            @Override
                            public void onResponse(Call<List<BusHistoryModel>> call, Response<List<BusHistoryModel>> response) {
                                if (response.isSuccessful()) {
                                    List<BusHistoryModel> list = response.body();
                                    if (list == null) {
                                        showError();
                                        return;
                                    }
                                    if (list.get(0) == null) {
                                        showError();
                                        return;
                                    }
                                    if (list.get(0).getTicketNumber() == null) {
                                        showError();
                                        return;
                                    }
                                    if (list.get(0).getTicketNumber().equals("null")) {
                                        showError();
                                        return;
                                    }
                                    if (list.get(0).getTicketNumber().equals("NULL")) {
                                        showError();
                                        return;
                                    }
                                    if (list.isEmpty()) {
                                        showError();
                                        return;
                                    }
                                    adapter.setBusHistoryModelList(list);
                                } else {
                                    showError();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<BusHistoryModel>> call, Throwable t) {
                                Log.d(TAG, "onFailureBusHistory: " + t.toString());
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, "onFailureLastBus: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }

    private void showError() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ErrorBusFragment())
                .commit();
    }

    @Override
    public void onItemSelected(int position) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, BusTicketFragment.getInstance(adapter.getBusHistoryModelList().get(position).getTicketNumber()),"ticket")
                .commit();
    }
}
