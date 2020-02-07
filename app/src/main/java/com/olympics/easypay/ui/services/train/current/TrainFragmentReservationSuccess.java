package com.olympics.easypay.ui.services.train.current;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.TrainTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainFragmentReservationSuccess extends Fragment {

    private static final String TAG = "MyTag";
    private TextView start, end, date, time, quantity, payDate, ticketCost, chairNumber;

    public TrainFragmentReservationSuccess() {
        super(R.layout.fragment_train_reservation_success);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getTrainTicket(myId).enqueue(new Callback<List<TrainTicketModel>>() {
            @Override
            public void onResponse(Call<List<TrainTicketModel>> call, Response<List<TrainTicketModel>> response) {
                if (response.isSuccessful()) {
                    updateViews(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<TrainTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateViews(TrainTicketModel trainTicketModel) {
        start.setText(trainTicketModel.getStartStation());
        end.setText(trainTicketModel.getEndStation());
        date.setText(trainTicketModel.getTicketTime());//TODO date time
        time.setText(trainTicketModel.getTicketTime());
        quantity.setText(trainTicketModel.getQuantity() + "");
        payDate.setText(trainTicketModel.getReserveTime());
        ticketCost.setText(trainTicketModel.getCost() + " EGP");
        chairNumber.setText(trainTicketModel.getChairNumber() + "");
    }
}
