package com.olympics.easypay.ui.services.train;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.TrainTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainTicketFragment extends Fragment {

    private static final String TAG = "MyTag";
    private TextView start, end, date, time, quantity, payDate, ticketCost, chairNumber;
    private ImageView qr;

    public TrainTicketFragment() {
        super(R.layout.fragment_train_reservation_success);
    }

    public static TrainTicketFragment getInstance(String ticketNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("ticket", ticketNumber);
        TrainTicketFragment fragment = new TrainTicketFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        start = view.findViewById(R.id.startStation);
        end = view.findViewById(R.id.endStation);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        quantity = view.findViewById(R.id.quantity);
        payDate = view.findViewById(R.id.payDate);
        ticketCost = view.findViewById(R.id.cost);
        chairNumber = view.findViewById(R.id.chairNumber);
        qr = view.findViewById(R.id.qr);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
//        final int id = getActivity().getSharedPreferences(SHARED_PREFS, 0).getInt(TOKEN, 0);
        int no = Integer.valueOf(getArguments().getString("ticket"));
        MyRetroFitHelper.getInstance().getTrainTicketByNumber(no).enqueue(new Callback<List<TrainTicketModel>>() {
            @Override
            public void onResponse(Call<List<TrainTicketModel>> call, Response<List<TrainTicketModel>> response) {
                if (response.isSuccessful()) {
                    updateViews(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<TrainTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureTrainTicketByNumber: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateViews(TrainTicketModel trainTicketModel) {
        start.setText(trainTicketModel.getStartStation());
        end.setText(trainTicketModel.getEndStation());
        date.setText(trainTicketModel.getTicketTime().split(" ")[0]);
        time.setText(trainTicketModel.getTicketTime().split(" ")[1]);
        quantity.setText(trainTicketModel.getQuantity() + "");
        payDate.setText(trainTicketModel.getReserveTime());
        ticketCost.setText(trainTicketModel.getCost() + " EGP");
        chairNumber.setText(trainTicketModel.getChairNumber() + "");
    }
}
