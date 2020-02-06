package com.example.easypay.ui.wizard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;

public class WizardFragment extends Fragment {

    private int mode;
    private WizardListener wizardListener;

    WizardFragment(int mode) {
        this.mode = mode;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        wizardListener = (WizardListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        wizardListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizard_1, container, false);
        switch (mode) {
            case 0:
                view = inflater.inflate(R.layout.fragment_wizard_1, container, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.fragment_wizard_2, container, false);
                break;

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (mode) {
            case 0:
                Button nextBtn = view.findViewById(R.id.button);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wizardListener.onNext();
                    }
                });
                break;
            case 1:
                nextBtn = view.findViewById(R.id.button);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wizardListener.onFinish();
                    }
                });
                break;

        }
    }

    public interface WizardListener {
        void onNext();

        void onFinish();
    }
}
