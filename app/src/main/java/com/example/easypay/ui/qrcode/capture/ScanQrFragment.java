package com.example.easypay.ui.qrcode.capture;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.otaliastudios.cameraview.CameraView;

public class ScanQrFragment extends Fragment {
    private CameraView cameraView;

    public ScanQrFragment() {
        super(R.layout.fragment_qr_scan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraView = view.findViewById(R.id.qrCam);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
