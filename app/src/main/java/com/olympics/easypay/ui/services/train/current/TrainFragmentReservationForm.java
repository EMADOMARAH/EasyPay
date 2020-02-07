package com.olympics.easypay.ui.services.train.current;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.olympics.easypay.R;
import com.olympics.easypay.models.AvailableTrainsModel;
import com.olympics.easypay.models.LineModel;
import com.olympics.easypay.models.StationModel;
import com.olympics.easypay.models.TrainTicketModel;
import com.olympics.easypay.network.MyRetroFitHelper;
import com.olympics.easypay.network.RetroHelper;
import com.olympics.easypay.utils.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainFragmentReservationForm extends Fragment {

    private static final String TAG = "MyTag";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy");
    private TrainListener listener;
    private Spinner line, start, end, time;
    private TextView ticket, date;
    private RetroHelper helper;
    private Calendar calendar;

    private String selectedDate;
    private String selectedTime;
    private String lineNumber;
    private String startStation;
    private String endStation;
    private int quantity = 1;

    public TrainFragmentReservationForm() {
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

        line = view.findViewById(R.id.lineNumber);
        start = view.findViewById(R.id.startStation);
        end = view.findViewById(R.id.endStation);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        ticket = view.findViewById(R.id.quantity);

        initLine(new ArrayList<LineModel>());
        initStations(new ArrayList<StationModel>());
        initTime(new ArrayList<AvailableTrainsModel>());

        calendar = Calendar.getInstance();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date.setText(simpleDateFormat1.format(calendar.getTime()));
                        selectedDate = simpleDateFormat.format(calendar.getTime());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lineNumber = parent.getItemAtPosition(position).toString();
                getStations(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startStation = parent.getItemAtPosition(position).toString();
                if (end.getSelectedItemPosition() != 0) {
                    checkAvailableTrains(parent.getItemAtPosition(position).toString(), end.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endStation = parent.getItemAtPosition(position).toString();
                if (start.getSelectedItemPosition() != 0) {
                    checkAvailableTrains(start.getSelectedItem().toString(), parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getItemAtPosition(position).toString().split(" train number ")[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ImageButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(ticket.getText().toString().split(" ")[0]);
                i++;
                if (i <= 10) {
                    quantity = i;
                    ticket.setText(i + " Ticket");
                }
            }
        });
        ImageButton sub = view.findViewById(R.id.sub);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(ticket.getText().toString().split(" ")[0]);
                i--;
                if (i > 0) {
                    quantity = i;
                    ticket.setText(i + " Ticket");
                }
            }
        });

        Button checkOutBtn = view.findViewById(R.id.checkOut_train);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    gotoCheck();
                }
            }
        });
    }

    private void initLine(List<LineModel> lineModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.select_line));
        for (LineModel lineModel : lineModelList) {
            strings.add(lineModel.getLineName());
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }
        };
        line.setAdapter(adapter);
    }

    private void gotoCheck() {
        TrainTicketModel trainTicketModel = new TrainTicketModel(startStation, endStation, selectedDate + " " + selectedTime, "", quantity, 0, 0);
        getActivity()
                .getSharedPreferences(Constants.SHARED_PREFS, 0)
                .edit()
                .putString(Constants.TRAIN_TICKET, new Gson().toJson(trainTicketModel))
                .apply();
        listener.gotoFragment(1);
    }

    private void checkAvailableTrains(String from, String to) {
        helper.getAvailableTrains(from, to).enqueue(new Callback<List<AvailableTrainsModel>>() {
            @Override
            public void onResponse(Call<List<AvailableTrainsModel>> call, Response<List<AvailableTrainsModel>> response) {
                if (response.isSuccessful()) {
                    initTime(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AvailableTrainsModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTime(List<AvailableTrainsModel> availableTrainsModelList) {
        final List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.select_time));
        for (AvailableTrainsModel availableTrainsModel : availableTrainsModelList) {
            strings.add(availableTrainsModel.getTime() + " train number " + availableTrainsModel.getTrainNumber());
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }
        };
        time.setAdapter(adapter);
    }

    private boolean check() {
        if (startStation.equals(start.getItemAtPosition(0).toString())) {
            Toast.makeText(getContext(), "select start station", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endStation.equals(end.getItemAtPosition(0).toString())) {
            Toast.makeText(getContext(), "select end station", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(getContext(), "enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedTime.equals(time.getItemAtPosition(0).toString())) {
            Toast.makeText(getContext(), "enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        helper = MyRetroFitHelper.getInstance();
        helper.getLines().enqueue(new Callback<List<LineModel>>() {
            @Override
            public void onResponse(Call<List<LineModel>> call, Response<List<LineModel>> response) {
                if (response.isSuccessful()) {
                    initLine(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<LineModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStations(String line) {
        helper.getStations(line).enqueue(new Callback<List<StationModel>>() {
            @Override
            public void onResponse(Call<List<StationModel>> call, Response<List<StationModel>> response) {
                if (response.isSuccessful()) {
                    initStations(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<StationModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initStations(List<StationModel> stationModelList) {
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
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
                    textView.setTextColor(getResources().getColor(R.color.greyish));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return textView;
            }
        };
        start.setAdapter(adapter);
        end.setAdapter(adapter);
    }

    public interface TrainListener {
        void gotoFragment(int page);
    }
}
