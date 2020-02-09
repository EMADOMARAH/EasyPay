package com.olympics.easypay.ui.card;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.olympics.easypay.R;
import com.olympics.easypay.models.CardNumberModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    Spinner spinner;
    Button add, del;
    float dp;
    int col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initViews();
        initData();
    }

    private void initData() {
        int id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance()
                .getMyCardNumbers(id)
                .enqueue(new Callback<List<CardNumberModel>>() {
                    @Override
                    public void onResponse(Call<List<CardNumberModel>> call, Response<List<CardNumberModel>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().isEmpty()) {
                                addCard();
                            } else {
                                initSpinner(response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CardNumberModel>> call, Throwable t) {
                        Toast.makeText(CardActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.toString());
                    }
                });
    }

    private void initSpinner(List<CardNumberModel> cardNumberModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.choose_card));
        for (CardNumberModel cardNumberModel : cardNumberModelList) {
            StringBuilder s = new StringBuilder(cardNumberModel.getCardNumber().toString());
            int t = 16 - s.length();
            for (int i = 0; i < t; i++) {
                s.insert(0, "0");
            }
            s.replace(0, 12, "XXXX XXXX XXXX ");
            strings.add(s.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (8 * dp), (int) (8 * dp), (int) (8 * dp), (int) (8 * dp));
                if (position == 0) {
                    textView.setTextColor(col);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (32 * dp), 0, (int) (32 * dp), 0);
                if (position == 0) {
                    textView.setTextColor(col);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void initViews() {
        dp = getResources().getDisplayMetrics().density;
        col = getResources().getColor(R.color.greyish);

        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.add);
        del = findViewById(R.id.delete);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCard();
            }
        });
    }

    private void addCard() {
        startActivity(new Intent(getApplicationContext(), CardAddActivity.class));
    }

    private void delCard() {
        startActivity(new Intent(getApplicationContext(), CardDeleteActivity.class));
    }
}
