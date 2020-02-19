package com.olympics.easypay.ui.qrcode.scan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;
import com.olympics.easypay.R;
import com.olympics.easypay.models.BalanceModel;
import com.olympics.easypay.models.PasswordCheckModel;
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
import static com.olympics.easypay.utils.Constants.BUS_TIME;
import static com.olympics.easypay.utils.Constants.EMAIL;
import static com.olympics.easypay.utils.Constants.SHARED_PREFS;

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
    private Dialog dialog;
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

        initQR();
    }

    @Override
    public void onStart() {
        super.onStart();
        initBalance();
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
                    case "personal":
                        if (balance < 10) {
                            Toast.makeText(activity, "You must have at least 10 EGP in your balance, please recharge then try again", Toast.LENGTH_LONG).show();
                            return;
                        }
                        initTransfer(root.getString("Easypay_Account_Name"), root.getString("Account_Email"));
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

    private void initTransfer(final String name, final String email) {
        if (dialog == null) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_transfer);
            dialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        dialog.show();
        dialog.findViewById(R.id.conf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout pass = dialog.findViewById(R.id.pass);
                TextInputLayout amount = dialog.findViewById(R.id.amount);
                if (pass.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amount.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Enter amount to transfer", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int i = Integer.parseInt(amount.getEditText().getText().toString().trim());
                if (i >= balance) {
                    Toast.makeText(activity, "Transfer amount shouldn't exceed your balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyRetroFitHelper.getInstance().checkPassword(getActivity().getSharedPreferences(SHARED_PREFS, 0).getString(EMAIL, ""), pass.getEditText().getText().toString().trim()).enqueue(new Callback<List<PasswordCheckModel>>() {
                    @Override
                    public void onResponse(Call<List<PasswordCheckModel>> call, Response<List<PasswordCheckModel>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().get(0).getPassword().equals("password successful")) {
                                MyRetroFitHelper.getInstance().transferCredits(name, email, i, id).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                if (response.body().string().equals("Succssfully Transfered Money")) {
                                                    Toast.makeText(getContext(), "Transfer Successful", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d(TAG, "onFailureTransfer: " + t.toString());
                                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), response.body().get(0).getPassword(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PasswordCheckModel>> call, Throwable t) {
                        Log.d(TAG, "onFailureCheckPass: " + t.toString());
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void error() {
        Toast.makeText(activity, "Wrong QR", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, 2000);
    }

    private void initBus(final int lineNumber) {
        MyRetroFitHelper.getInstance().isMetroPending(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body().string().equals("pending ticket")) {
                            Toast.makeText(activity, "Can't reserve bus while in metro", Toast.LENGTH_SHORT).show();
                        } else {
                            MyRetroFitHelper.getInstance().reserveBus(id, lineNumber).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            Toast.makeText(activity, response.body().string(), Toast.LENGTH_SHORT).show();
                                            getActivity().setResult(RESULT_OK);
                                            getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).edit().putLong(BUS_TIME, Calendar.getInstance().getTimeInMillis()).apply();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailurePending: " + t.toString());
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
                        getActivity().getSharedPreferences(SHARED_PREFS, 0).edit().remove(BUS_TIME).apply();
                        MyRetroFitHelper.getInstance().show(id).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                                getActivity().setResult(RESULT_OK);
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
