package com.olympics.easypay.ui.home.payment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;

public class PaymentFragment extends Fragment {

    public PaymentFragment() {
        super(R.layout.fragment_payment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadFragment(new PaymentFragmentNeutral());
    }

    void loadFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container_payment, fragment)
                .commit();
    }
}
