package com.example.easypay.ui.home.payment;

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

import com.example.easypay.R;
import com.example.easypay.models.BalanceModel;
import com.example.easypay.network.RetroHelper;
import com.example.easypay.ui.qrcode.QrActivity;
import com.example.easypay.ui.services.bus.BusPaymentActivity;
import com.example.easypay.ui.services.metro.MetroPaymentActivity;
import com.example.easypay.ui.services.train.TrainPaymentActivity;
import com.example.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentFragment2 extends Fragment {
    private static final String TAG = "MyTag";
    private TextView myBalanceTxt;
    private Retrofit retrofit;
    private RetroHelper helper;
    private SharedPreferences sharedPreferences;
    private int myId;

    public PaymentFragment2() {
        super(R.layout.fragment_payment_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myBalanceTxt = view.findViewById(R.id.myBalance);

        CardView cardView1 = view.findViewById(R.id.bus_card);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BusPaymentActivity.class));
            }
        });

        CardView cardView2 = view.findViewById(R.id.metro_card);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MetroPaymentActivity.class));
            }
        });

        CardView cardView3 = view.findViewById(R.id.train_card);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrainPaymentActivity.class));
            }
        });

        LinearLayout openQrBtn = view.findViewById(R.id.openQR);
        openQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QrActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSharedPrefs();
        initRetroFit();
        getBalance();
    }

    private void initSharedPrefs() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
        myId = sharedPreferences.getInt(Constants.TOKEN, 0);
    }

    private void getBalance() {
        helper.getBalance(myId).enqueue(new Callback<List<BalanceModel>>() {
            @Override
            public void onResponse(Call<List<BalanceModel>> call, Response<List<BalanceModel>> response) {
                if (response.isSuccessful()) {
                    int balance = response.body().get(0).getCurrentBalance();
                    myBalanceTxt.setText(balance + " EGP");
                }
            }

            @Override
            public void onFailure(Call<List<BalanceModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRetroFit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helper = retrofit.create(RetroHelper.class);
    }
}
