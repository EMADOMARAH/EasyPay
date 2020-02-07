package com.example.easypay.ui.services.train.current;

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

import com.example.easypay.R;
import com.example.easypay.models.TrainTicketModel;
import com.example.easypay.network.MyRetroFitHelper;
import com.example.easypay.utils.Constants;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainFragmentReservationCheck extends Fragment {

    private static final String TAG = "MyTag";
    private TextView from, to, date, time, quantity, cost;
    private TrainListener listener;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private TrainTicketModel trainTicketModel;

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

        Button confirmBtn = view.findViewById(R.id.confirm_train);
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

        initPrefs();
    }

    private void initPrefs() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
        trainTicketModel = new Gson().fromJson(sharedPreferences.getString(Constants.TRAIN_TICKET, ""), TrainTicketModel.class);
        updateViews(trainTicketModel);
    }

    private void updateViews(TrainTicketModel trainTicketModel) {
        from.setText(trainTicketModel.getStartStation());
        to.setText(trainTicketModel.getEndStation());
        date.setText(formateDate(trainTicketModel.getTicketTime()));
        time.setText(formateTime(trainTicketModel.getTicketTime()));
        quantity.setText(trainTicketModel.getQuantity() + " Ticket");
        cost.setText(trainTicketModel.getCost() + " EGP");
    }

    private String formateDate(String ticketTime) {
        String s = "";
        try {
            simpleDateFormat.parse(ticketTime);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("eeee, dd MMM yyyy");
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
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
            s = simpleDateFormat1.format(simpleDateFormat.getCalendar().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void saveTicket() {
        int myId = sharedPreferences.getInt(Constants.TOKEN, 0);
        MyRetroFitHelper.getInstance().saveTrainTicket(myId, trainTicketModel.getStartStation(), trainTicketModel.getEndStation(), trainTicketModel.getTicketTime(), trainTicketModel.getQuantity()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.gotoFragment(2);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public interface TrainListener {
        void gotoFragment(int page);
    }
}
