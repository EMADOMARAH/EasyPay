package com.olympics.easypay.ui.qrcode.my;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.olympics.easypay.R;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.BASE_QR;
import static com.olympics.easypay.utils.Constants.BASE_URL;

public class MyQrFragment extends Fragment {

    private static final String TAG = "MyTag";
    private ImageView imageView;

    public MyQrFragment() {
        super(R.layout.fragment_qr_my);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.qrImg);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRetro();
    }

    private void initRetro() {
        int id = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().getMyQR(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String s = response.body().string();
                        s = s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\""));
                        Log.d(TAG, "onResponse: " + s);
                        Glide.with(getContext()).load(BASE_URL + BASE_QR + s).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "not found", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailureMyQR: " + t.toString());
            }
        });
    }
}
