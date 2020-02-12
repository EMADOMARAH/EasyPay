package com.olympics.easypay.ui.home.wallet;

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
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.WalletModel;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.ui.card.CardActivity;
import com.olympics.easypay.ui.history.HistoryActivity;
import com.olympics.easypay.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WalletFragment extends Fragment {

    private static final String TAG = "MyTag";
    private TextView myBalanceTxt;
    private TextView lastChargeTxt;
    private TextView chargeDateTxt;
    private Retrofit retrofit;
    private RetroHelper helper;
    private SharedPreferences sharedPreferences;
    private int myId;

    public WalletFragment() {
        super(R.layout.fragment_wallet);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView paymentHistoryBtn = view.findViewById(R.id.pay_history);
        paymentHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });

        myBalanceTxt = view.findViewById(R.id.myBalance);
        lastChargeTxt = view.findViewById(R.id.lastCharge);
        chargeDateTxt = view.findViewById(R.id.chargeDate);

        LinearLayout payCardBtn = view.findViewById(R.id.payWithCard);
        payCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CardActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSharedPrefs();
        initRetroFit();
        getData();
    }

    private void getData() {
        helper.getWallet(myId).enqueue(new Callback<List<WalletModel>>() {
            @Override
            public void onResponse(Call<List<WalletModel>> call, Response<List<WalletModel>> response) {
                if (response.isSuccessful()) {
                    int balance = response.body().get(0).getCurrentBalance();
                    int lastCharge = response.body().get(0).getLastCharge();
                    String chargeDate = response.body().get(0).getChargeDate();

                    myBalanceTxt.setText(balance + " EGP");
                    lastChargeTxt.setText("Last Charge: " + lastCharge + " EGP");
                    chargeDateTxt.setText("Charge Date: " + chargeDate);
                }
            }

            @Override
            public void onFailure(Call<List<WalletModel>> call, Throwable t) {
                Log.d(TAG, "onFailureWallet: " + t.toString());
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

    private void initSharedPrefs() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
        myId = sharedPreferences.getInt(Constants.TOKEN, 0);
    }
}
