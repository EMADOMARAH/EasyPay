package com.olympics.easypay.ui.qrcode.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.olympics.easypay.R;
import com.olympics.easypay.models.BalanceModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class ScanQrFragment extends Fragment {
    private static final String TAG = "MyTag";
    private Vibrator vibrator;
    private CodeScanner codeScanner;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            codeScanner.startPreview();
        }
    };
    private Handler handler;
    private CodeScannerView codeScannerView;
    private AppCompatActivity activity;
    private int id;
    private int balance;

    public ScanQrFragment() {
        super(R.layout.fragment_qr_scan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        codeScannerView = view.findViewById(R.id.qrCam);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (codeScanner != null)
            codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (AppCompatActivity) getActivity();
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        handler = new Handler();

        id = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);

        initBalance();
        initQR();
    }

    private void initBalance() {
        MyRetroFitHelper.getInstance().getBalance(id).enqueue(new Callback<List<BalanceModel>>() {
            @Override
            public void onResponse(Call<List<BalanceModel>> call, Response<List<BalanceModel>> response) {
                if (response.isSuccessful()) {
                    balance = response.body().get(0).getCurrentBalance();
                }
            }

            @Override
            public void onFailure(Call<List<BalanceModel>> call, Throwable t) {
                Log.d(TAG, "onFailureBalance: " + t.toString());
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            initQR();
        }
    }

    private void initQR() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 111);
        } else {
            codeScanner = new CodeScanner(activity, codeScannerView);
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vibrate();
                            jsonParse(result.getText());
                        }
                    });
                }
            });
        }
    }

    private void jsonParse(String json) {
        Log.d(TAG, "jsonParse: " + json);
        try {
            JSONObject root = new JSONArray(json).getJSONObject(0);
            if (root.has("Type")) {
                switch (root.getString("Type")) {
                    case "bus":
                        if (balance < 4) {
                            Toast.makeText(activity, "You must have at least 4 EGP in your balance, please recharge then try again", Toast.LENGTH_LONG).show();
                            return;
                        }
                        initBus(root.getInt("line_number"));
                        break;
                    case "metro":
                        if (balance < 10) {
                            Toast.makeText(activity, "You must have at least 10 EGP in your balance, please recharge then try again", Toast.LENGTH_LONG).show();
                            return;
                        }
                        initMetro(root.getInt("station_number"));
                        break;
                    default:
                        error();
                        break;
                }
            } else {
                error();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void error() {
        Toast.makeText(activity, "Wrong QR", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, 2000);
    }

    private void initBus(int lineNumber) {
        MyRetroFitHelper.getInstance().reserveBus(id, lineNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(activity, response.body().string(), Toast.LENGTH_SHORT).show();
                        getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).edit().putLong(Constants.BUS_TIME, Calendar.getInstance().getTimeInMillis()).apply();
                        activity.onBackPressed();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureBus: " + t.toString());
            }
        });
    }

    private void initMetro(int metroStation) {
        MyRetroFitHelper.getInstance().reserveMetro(id, metroStation).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(activity, response.body().string(), Toast.LENGTH_SHORT).show();
                        MyRetroFitHelper.getInstance().show(id).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                                activity.onBackPressed();
                            }

                            @Override
                            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailureShow: " + t.toString());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureMetro: " + t.toString());
            }
        });
    }

    private void vibrate() {
        vibrator.vibrate(100);
    }
}
