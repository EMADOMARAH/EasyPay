package com.olympics.easypay.ui.home.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class PaymentFragment extends Fragment {
    private static final String TAG = "MyTag";
    private SharedPreferences sharedPreferences;
    private TextView myBalanceTxt, lastBus, lastMetro, lastTrain;
    private ImageView pendingBus, pendingMetro;
    private RetroHelper helper;
    private int myId;

    public PaymentFragment() {
        super(R.layout.fragment_payment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myBalanceTxt = view.findViewById(R.id.myBalance);

        lastBus = view.findViewById(R.id.lastBus);
        lastMetro = view.findViewById(R.id.lastMetro);
        lastTrain = view.findViewById(R.id.lastTrain);
        pendingBus = view.findViewById(R.id.pendingBus);
        pendingMetro = view.findViewById(R.id.pendingMetro);

        CardView cardView1 = view.findViewById(R.id.bus_card);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFragment.this.getContext(), BusPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
            }
        });

        CardView cardView2 = view.findViewById(R.id.metro_card);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFragment.this.getContext(), MetroPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
            }
        });

        CardView cardView3 = view.findViewById(R.id.train_card);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFragment.this.getContext(), TrainPaymentActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
            }
        });

        LinearLayout openQrBtn = view.findViewById(R.id.openQR);
        openQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFragment.this.getContext(), QrActivity.class));
                getActivity().overridePendingTransition(R.anim.right_zero, R.anim.zero_left);
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
        getPending();
    }

    private void getPending() {
        helper.isMetroPending(myId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().string().equals("pending ticket")) {
                            pendingMetro.setVisibility(View.VISIBLE);
                        } else {
                            pendingMetro.setVisibility(View.INVISIBLE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailureIsPending: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
        long l = sharedPreferences.getLong(Constants.BUS_TIME, 0);
        if (Calendar.getInstance().getTimeInMillis() - l < 7200000) {
            pendingBus.setVisibility(View.VISIBLE);
        } else {
            pendingBus.setVisibility(View.INVISIBLE);
        }
    }

    private void getLastTrips() {
        helper.getLastBusTrip(myId).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        try {
                            if (array.get(0) instanceof JSONArray) {
                                lastBus.setText("No trips made yet");
                            } else {
                                helper.getBusHistory(myId).enqueue(new Callback<List<BusHistoryModel>>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(Call<List<BusHistoryModel>> call, Response<List<BusHistoryModel>> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() == null) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            if (response.body().isEmpty()) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            if (response.body().get(response.body().size() - 1) == null) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            String s = response.body().get(response.body().size() - 1).getTicketDate();
                                            if (s == null) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            if (s.equals("null")) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            if (s.equals("NULL")) {
                                                lastBus.setText("No trips made yet");
                                                return;
                                            }
                                            lastBus.setText("Last trip: " + s);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<BusHistoryModel>> call, Throwable t) {
                                        Log.d(TAG, "onFailureBusHistory: " + t.toString());
                                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailureLastBus: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        helper.getLastMetroTrip(myId).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        if (array.get(0) instanceof JSONArray) {
                            lastMetro.setText("No trips made yet");
                        } else {
                            helper.getMetroHistory(myId).enqueue(new Callback<List<MetroHistoryModel>>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(Call<List<MetroHistoryModel>> call, Response<List<MetroHistoryModel>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() == null) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        if (response.body().isEmpty()) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        if (response.body().get(response.body().size() - 1) == null) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        String s = response.body().get(response.body().size() - 1).getTicketDate();
                                        if (s == null) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        if (s.equals("null")) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        if (s.equals("NULL")) {
                                            lastMetro.setText("No trips made yet");
                                            return;
                                        }
                                        lastMetro.setText("Last trip: " + s);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<MetroHistoryModel>> call, Throwable t) {
                                    Log.d(TAG, "onFailureMetroHistory: " + t.toString());
                                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailureLastMetro: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        helper.getLastTrainTrip(myId).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        try {
                            if (array.get(0) instanceof JSONArray) {
                                lastTrain.setText("No trips made yet");
                            } else {
                                helper.getTrainHistory(myId).enqueue(new Callback<List<TrainHistoryModel>>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onResponse(Call<List<TrainHistoryModel>> call, Response<List<TrainHistoryModel>> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() == null) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
                                            if (response.body().isEmpty()) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
                                            if (response.body().get(response.body().size() - 1) == null) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
                                            String s = response.body().get(response.body().size() - 1).getReserveTime();
                                            if (s == null) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
                                            if (s.equals("null")) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
                                            if (s.equals("NULL")) {
                                                lastTrain.setText("No trips made yet");
                                                return;
                                            }
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailureLastTrain: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSharedPrefs() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
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
