package com.olympics.easypay.ui.home.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.BalanceModel;
import com.olympics.easypay.models.BusHistoryModel;
import com.olympics.easypay.models.MetroHistoryModel;
import com.olympics.easypay.models.TrainHistoryModel;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.qrcode.QrActivity;
import com.olympics.easypay.ui.services.bus.BusPaymentActivity;
import com.olympics.easypay.ui.services.metro.MetroPaymentActivity;
import com.olympics.easypay.ui.services.train.TrainPaymentActivity;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class PaymentFragmentNeutral extends Fragment {
    private static final String TAG = "MyTag";
    private TextView myBalanceTxt, lastBus, lastMetro, lastTrain;
    private RetroHelper helper;
    private int myId;

    public PaymentFragmentNeutral() {
        super(R.layout.fragment_payment_neutral);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myBalanceTxt = view.findViewById(R.id.myBalance);

        lastBus = view.findViewById(R.id.lastBus);
        lastMetro = view.findViewById(R.id.lastMetro);
        lastTrain = view.findViewById(R.id.lastTrain);
        CardView cardView1 = view.findViewById(R.id.bus_card);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BusPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                getActivity().finishAfterTransition();
            }
        });

        CardView cardView2 = view.findViewById(R.id.metro_card);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MetroPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                getActivity().finishAfterTransition();
            }
        });

        CardView cardView3 = view.findViewById(R.id.train_card);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrainPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                getActivity().finishAfterTransition();
            }
        });

        LinearLayout openQrBtn = view.findViewById(R.id.openQR);
        openQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QrActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
                getActivity().finishAfterTransition();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSharedPrefs();
        initRetroFit();
        getBalance();
        getLastTrips();
    }

    private void getLastTrips() {
        helper.getBusHistory(myId).enqueue(new Callback<List<BusHistoryModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<BusHistoryModel>> call, Response<List<BusHistoryModel>> response) {
                if (response.isSuccessful()) {
                    String s = response.body().get(response.body().size() - 1).getTicketDate();
                    if (s.equals("NULL")) {
                        lastBus.setText("No trips made yet");
                    } else {
                        lastBus.setText("Last trip: " + s);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BusHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailureBusHistory: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        helper.getMetroHistory(myId).enqueue(new Callback<List<MetroHistoryModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<MetroHistoryModel>> call, Response<List<MetroHistoryModel>> response) {
                String s = response.body().get(response.body().size() - 1).getTicketDate();
                if (s.equals("NULL")) {
                    lastMetro.setText("No trips made yet");
                } else {
                    lastMetro.setText("Last trip: " + s);
                }
            }

            @Override
            public void onFailure(Call<List<MetroHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailureMetroHistory: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        helper.getTrainHistory(myId).enqueue(new Callback<List<TrainHistoryModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<TrainHistoryModel>> call, Response<List<TrainHistoryModel>> response) {
                String s = response.body().get(response.body().size() - 1).getReserveTime();
                if (s.equals("NULL")) {
                    lastTrain.setText("No trips made yet");
                } else {
                    lastTrain.setText("Last trip: " + s);
                }
            }

            @Override
            public void onFailure(Call<List<TrainHistoryModel>> call, Throwable t) {
                Log.d(TAG, "onFailureTrainHistory: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSharedPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
        myId = sharedPreferences.getInt(Constants.TOKEN, 0);
    }

    private void getBalance() {
        helper.getBalance(myId).enqueue(new Callback<List<BalanceModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<BalanceModel>> call, Response<List<BalanceModel>> response) {
                if (response.isSuccessful()) {
                    int balance = response.body().get(0).getCurrentBalance();
                    myBalanceTxt.setText(balance + " EGP");
                }
            }

            @Override
            public void onFailure(Call<List<BalanceModel>> call, Throwable t) {
                Log.d(TAG, "onFailureBalance: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRetroFit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helper = retrofit.create(RetroHelper.class);
    }
}
