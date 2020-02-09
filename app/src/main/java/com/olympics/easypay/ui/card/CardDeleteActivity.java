package com.olympics.easypay.ui.card;

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
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.utils.Constants;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDeleteActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    Spinner spinner;
    Button button;
    float dp;
    RetroHelper helper;
    List<BigInteger> bigIntegerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_delete);

        initViews();
        initData();
    }

    private void initData() {
        helper = MyRetroFitHelper.getInstance();

        int id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        helper.getMyCardNumbers(id).enqueue(new Callback<List<CardNumberModel>>() {
            @Override
            public void onResponse(Call<List<CardNumberModel>> call, Response<List<CardNumberModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        Toast.makeText(CardDeleteActivity.this, "you don't have any cards", Toast.LENGTH_SHORT).show();
                    } else {
                        initCards(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardNumberModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(CardDeleteActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCards(List<CardNumberModel> cardNumberModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.choose_card));
        bigIntegerList = new ArrayList<>();
        for (CardNumberModel cardNumberModel : cardNumberModelList) {
            bigIntegerList.add(cardNumberModel.getCardNumber());
            StringBuilder s = new StringBuilder(cardNumberModel.getCardNumber().toString());
            int t = 16 - s.length();
            for (int i = 0; i < t; i++) {
                s.insert(0, "0");
            }
            s.replace(0, 12, "XXXX XXXX XXXX ");
            strings.add(s.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (32 * dp), 0, (int) (32 * dp), 0);
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (8 * dp), (int) (8 * dp), (int) (8 * dp), (int) (8 * dp));
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                return textView;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void initViews() {
        dp = getResources().getDisplayMetrics().density;

        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.delete);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() != 0) {
                    deleteCard(bigIntegerList.get(spinner.getSelectedItemPosition() - 1));
                }
            }
        });
    }

    private void deleteCard(BigInteger cardNo) {
        int id = getSharedPreferences(Constants.SHARED_PREFS, 0).getInt(Constants.TOKEN, 0);
        helper.deleteCard(id, cardNo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CardDeleteActivity.this, "Card Deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(CardDeleteActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(CardDeleteActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
