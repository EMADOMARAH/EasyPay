package com.example.easypay.ui.services.train.current;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easypay.R;
import com.example.easypay.models.StationModel;
import com.example.easypay.network.MyRetroFitHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainFragmentReservation1 extends Fragment {

    private static final String TAG = "MyTag";
    private TrainListener listener;
    private Spinner start, end;
    private EditText date, time;
    private TextView ticket;

    public TrainFragmentReservation1() {
        super(R.layout.fragment_train_reservation_form);
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

        start = view.findViewById(R.id.startStation);
        end = view.findViewById(R.id.endStation);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        ticket = view.findViewById(R.id.quantity);

        ImageButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(ticket.getText().toString().split(" ")[0]);
                i++;
                ticket.setText(i + " Ticket");
            }
        });
        ImageButton sub = view.findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(ticket.getText().toString().split(" ")[0]);
                i--;
                if (i > 0) {
                    ticket.setText(i + " Ticket");
                }
            }
        });

        Button checkOutBtn = view.findViewById(R.id.checkOut_train);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = start.getSelectedItem().toString();
                String to = end.getSelectedItem().toString();
                String da = date.getText().toString().trim();
                String ti = time.getText().toString().trim();
                int qu = Integer.parseInt(ticket.getText().toString().split(" ")[0]);
                if (check(from, to, da, ti)) {
                    listener.gotoFragment(1);
                }
            }
        });
    }

    private boolean check(String from, String to, String da, String ti) {
        if (from.equals(start.getItemAtPosition(0).toString())) {
            Toast.makeText(getContext(), "select start station", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (to.equals(end.getItemAtPosition(0).toString())) {
            Toast.makeText(getContext(), "select end station", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (da.isEmpty()) {
            Toast.makeText(getContext(), "enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ti.isEmpty()) {
            Toast.makeText(getContext(), "enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//TODO saveInstance
        initData();
    }

    private void initData() {
        MyRetroFitHelper.getInstance().getStations().enqueue(new Callback<List<StationModel>>() {
            @Override
            public void onResponse(Call<List<StationModel>> call, Response<List<StationModel>> response) {
                if (response.isSuccessful()) {
                    updateViews(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<StationModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateViews(List<StationModel> stationModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.select_location));
        for (StationModel stationModel : stationModelList) {
            strings.add(stationModel.getStation());
        }
        final float dp = getResources().getDisplayMetrics().density;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = new TextView(parent.getContext());
                textView.setText(strings.get(position));
                textView.setPadding((int) (8 * dp), (int) (8 * dp), (int) (8 * dp), (int) (8 * dp));
                textView.setGravity(Gravity.LEFT);
                if (position == 0) {
                    textView.setTextColor(Color.LTGRAY);
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
                    textView.setTextColor(Color.LTGRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }

            //            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
//                textView.setGravity(Gravity.CENTER);
//                if (position == 0) {
//                    textView.setTextColor(Color.LTGRAY);
//                } else {
//                    textView.setTextColor(Color.BLACK);
//                }
//                notifyDataSetChanged();
//                return super.getDropDownView(position, convertView, parent);
//            }

//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                float dp = getResources().getDisplayMetrics().density;
//                TextView textView = (TextView) super.getView(position, convertView, parent);
//                textView.setPadding((int) (32 * dp), 0, (int) (32 * dp), 0);
//                if (position == 0) {
//                    textView.setTextColor(Color.LTGRAY);
//                } else {
//                    textView.setTextColor(Color.LTGRAY);
//                }
//                notifyDataSetChanged();
//                return super.getView(position, convertView, parent);
//            }
        };
        start.setAdapter(adapter);
        end.setAdapter(adapter);
    }

    public interface TrainListener {
        void gotoFragment(int page);
    }
}
