package com.olympics.easypay.ui.qrcode.scan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ScanQrFragment extends Fragment {
    private static final String TAG = "MyTag";
    private Vibrator vibrator;
    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private AppCompatActivity activity;
    private int id;

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

        id = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);

        initQR();
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
        try {
            JSONObject root = new JSONArray(json).getJSONObject(0);
            switch (root.getString("Type")) {
                case "bus":
                    initBus(root.getInt("line_number"));
                    break;
                case "metro":
                    initMetro(root.getInt("ticket_number"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initBus(int lineNumber) {
        MyRetroFitHelper.getInstance().reserveBus(id, lineNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(activity, response.body().string(), Toast.LENGTH_SHORT).show();
                        activity.finish();
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

    private void initMetro(int ticketNumber) {
        MyRetroFitHelper.getInstance().reserveMetro(id, ticketNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Toast.makeText(activity, response.body().string(), Toast.LENGTH_SHORT).show();
                        activity.finish();
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
