package com.olympics.easypay.ui.services.train.current;

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

import com.bumptech.glide.Glide;
import com.olympics.easypay.R;
import com.olympics.easypay.models.TrainTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.BASE_URL;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class TrainFragmentReservationSuccess extends Fragment {

    private static final String TAG = "MyTag";
    private TextView start, end, date, time, quantity, payDate, ticketCost, chairNumber;
    private ImageView qr;

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
        qr = view.findViewById(R.id.qr);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        final int myId = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getTrainTicket(myId).enqueue(new Callback<List<TrainTicketModel>>() {
            @Override
            public void onResponse(Call<List<TrainTicketModel>> call, Response<List<TrainTicketModel>> response) {
                Log.d(TAG, "onResponse: " + response.body().get(0).toString());
                if (response.isSuccessful()) {
                    updateViews(response.body().get(0));
                    MyRetroFitHelper.getInstance().getTrainQR(myId).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String s = response.body().string();
                                    Log.d(TAG, "onResponse: " + s);
                                    if (!s.isEmpty()) {
                                        Glide.with(getContext()).load(BASE_URL + s).into(qr);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "onFailureGetTrainQR: " + t.toString());
                            Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TrainTicketModel>> call, Throwable t) {
                Log.d(TAG, "onFailureGetTrainTicket: " + t.toString());
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
