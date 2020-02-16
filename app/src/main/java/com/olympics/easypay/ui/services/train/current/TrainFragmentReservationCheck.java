package com.olympics.easypay.ui.services.train.current;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.olympics.easypay.R;
import com.olympics.easypay.models.TrainCostModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.olympics.easypay.utils.Constants.TOKEN;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class TrainFragmentReservationCheck extends Fragment {

    private static final String TAG = "MyTag";
    private TextView from, to, date, time, quantity, cost;
    private TrainListener listener;
    private SharedPreferences sharedPreferences;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private Map<String, String> map;
    private Button confirmBtn;

    public TrainFragmentReservationCheck() {
        super(R.layout.fragment_train_reservation_check);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (TrainListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        quantity = view.findViewById(R.id.quantity);
        cost = view.findViewById(R.id.cost);

        ImageButton editBtn = view.findViewById(R.id.edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.gotoFragment(0);
            }
        });

        confirmBtn = view.findViewById(R.id.confirm_train);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTicket();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
        map = new Gson().fromJson(sharedPreferences.getString("map", ""), Map.class);
        int id = sharedPreferences.getInt(TOKEN, 0);
        MyRetroFitHelper.getInstance().getTrainCost(id, Integer.parseInt(map.get("train"))).enqueue(new Callback<List<TrainCostModel>>() {
            @Override
            public void onResponse(Call<List<TrainCostModel>> call, Response<List<TrainCostModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Log.d(TAG, "onResponse: " + response.body().toString());
                        return;
                    }
                    if (response.body().get(0) == null) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).toString());
                        return;
                    }
                    if (response.body().get(0).getMyBalance() == null) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getMyBalance());
                        return;
                    }
                    if (response.body().get(0).getMyBalance().equals("null")) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getMyBalance());
                        return;
                    }
                    if (response.body().get(0).getMyBalance().equals("NULL")) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getMyBalance());
                        return;
                    }
                    if (response.body().get(0).getTrainCost() == null) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getTrainCost());
                        return;
                    }
                    if (response.body().get(0).getTrainCost().equals("null")) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getTrainCost());
                        return;
                    }
                    if (response.body().get(0).getTrainCost().equals("NULL")) {
                        Log.d(TAG, "onResponse: " + response.body().get(0).getTrainCost());
                        return;
                    }
                    TrainCostModel trainCostModel = response.body().get(0);
                    if (Integer.valueOf(trainCostModel.getTrainCost()) > Integer.valueOf(trainCostModel.getMyBalance())) {
                        Toast.makeText(getContext(), "You don't have enough credits please charge your balance", Toast.LENGTH_SHORT).show();
                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "You don't have enough credits please charge your balance", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    int total = Integer.valueOf(trainCostModel.getTrainCost()) * Integer.valueOf(map.get("quantity"));
                    map.put("cost", total + "");
                    updateViews();
                }
            }

            @Override
            public void onFailure(Call<List<TrainCostModel>> call, Throwable t) {
                Log.d(TAG, "onFailureTrainCost: " + t.toString());
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateViews() {
        from.setText(map.get("start"));
        to.setText(map.get("end"));
        date.setText(formateDate(map.get("time")));
        time.setText(formateTime(map.get("time")));
        quantity.setText(map.get("quantity") + " Ticket");
        cost.setText(map.get("cost") + " EGP");
    }

    private String formateDate(String ticketTime) {
        String s = "";
        try {
            simpleDateFormat.parse(ticketTime);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy");
            s = simpleDateFormat1.format(simpleDateFormat.getCalendar().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String formateTime(String ticketTime) {
        String s = "";
        try {
            simpleDateFormat.parse(ticketTime);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
            s = simpleDateFormat1.format(simpleDateFormat.getCalendar().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void saveTicket() {
        int myId = sharedPreferences.getInt(TOKEN, 0);
        MyRetroFitHelper.getInstance().saveTrainTicket(
                myId,
                map.get("start"),
                map.get("end"),
                map.get("time"),
                Integer.valueOf(map.get("quantity")),
                Integer.valueOf(map.get("train")),
                Integer.valueOf(map.get("cost")))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            listener.gotoFragment(2);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailureSaveTrainTicket: " + t.toString());
                    }
                });
    }

    public interface TrainListener {
        void gotoFragment(int page);
    }
}
